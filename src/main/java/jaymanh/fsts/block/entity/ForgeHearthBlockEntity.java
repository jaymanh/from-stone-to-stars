package jaymanh.fsts.block.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jaymanh.fsts.component.CrucibleCharge;
import jaymanh.fsts.item.CrucibleItem;
import jaymanh.fsts.item.ModItems;
import jaymanh.fsts.recipe.CountedIngredient;
import jaymanh.fsts.recipe.CrucibleAlloyingRecipe;
import jaymanh.fsts.recipe.ModRecipes;
import jaymanh.fsts.multiblock.ActiveMultiblocks;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.StructureMatch;
import jaymanh.fsts.multiblock.process.ForgeProcess;
import jaymanh.fsts.item.HeatedMetalItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

/**
 * Blast-furnace smelting, including charged crucibles, while the fuzzy forge assembly is lit.
 * All hearths in a match share lit state; inventory/smelting live on the master (origin) hearth.
 * Base cook is slower than a blast furnace; extra hearths speed it back up. Item fuel is unused.
 */
public class ForgeHearthBlockEntity extends AbstractFurnaceBlockEntity {
	private static final int ASSEMBLY_LIT_BUFFER = 200;
	/** Game ticks between cook steps (higher = slower base speed). Extra hearths add steps per interval. */
	private static final int COOK_INTERVAL = 5;
	private static final int[] SLOTS_UP = {SLOT_INPUT};
	private static final int[] SLOTS_DOWN = {SLOT_RESULT};
	private static final int[] SLOTS_SIDE = {};

	private FormedMultiblock formed;
	private boolean participantsLinked;
	private int nextInput;
	private int nextSpout;
	private @Nullable CrucibleCharge alloy;
	private int alloyProgress;

	public ForgeHearthBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FORGE_HEARTH, pos, state, RecipeType.BLASTING);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.from-stone-to-stars.forge_hearth");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new BlastFurnaceMenu(containerId, inventory, this, this.dataAccess);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == SLOT_FUEL) {
			return false;
		}
		return super.canPlaceItem(slot, stack);
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_DOWN;
		}
		if (direction == Direction.UP) {
			return SLOTS_UP;
		}
		return SLOTS_SIDE;
	}

	@Override
	protected int getBurnDuration(FuelValues fuelValues, ItemStack stack) {
		return 0;
	}

	public FormedMultiblock getFormed() {
		if (this.formed != null && this.formed.isActive()) {
			return this.formed;
		}
		if (this.level != null) {
			return ActiveMultiblocks.get(this.level, this.worldPosition).orElse(null);
		}
		return null;
	}

	/**
	 * Hearth that owns inventory and the burn process (assembly origin).
	 */
	public ForgeHearthBlockEntity getMaster() {
		FormedMultiblock formed = this.getFormed();
		if (formed == null) {
			return this;
		}
		BlockPos origin = formed.match().origin();
		if (origin.equals(this.worldPosition)) {
			return this;
		}
		if (this.level != null && this.level.getBlockEntity(origin) instanceof ForgeHearthBlockEntity master) {
			return master;
		}
		return this;
	}

	public boolean isBurning() {
		FormedMultiblock formed = this.getFormed();
		return formed != null && formed.isActive();
	}

	public boolean isMaster() {
		FormedMultiblock formed = this.getFormed();
		return formed == null || formed.match().origin().equals(this.worldPosition);
	}

	public int getProcessProgress() {
		FormedMultiblock formed = this.getFormed();
		return formed != null && formed.isActive() ? formed.getProcessProgress() : 0;
	}

	public int getProcessDuration() {
		FormedMultiblock formed = this.getFormed();
		return formed != null ? formed.getProcessDuration() : 0;
	}

	public boolean start(StructureMatch match) {
		if (!(this.level instanceof ServerLevel serverLevel) || this.isBurning()) {
			return false;
		}
		FormedMultiblock formed = new FormedMultiblock(match, match.processFactory().create(match));
		this.formed = formed;
		this.participantsLinked = true;
		ActiveMultiblocks.register(serverLevel, formed);
		formed.onFormed(serverLevel);
		return true;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ForgeHearthBlockEntity hearth) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		tickAssembly(serverLevel, pos, state, hearth);
		if (hearth.isRemoved()) {
			return;
		}

		state = level.getBlockState(pos);
		if (!state.hasProperty(AbstractFurnaceBlock.LIT)) {
			return;
		}

		// Only the master hearth smelts; slaves share its lit visual / inventory via openMenu.
		if (!hearth.isMaster()) {
			return;
		}

		if (hearth.isBurning()) {
			hearth.dataAccess.set(DATA_LIT_TIME, Math.max(hearth.dataAccess.get(DATA_LIT_TIME), ASSEMBLY_LIT_BUFFER));
			hearth.dataAccess.set(DATA_LIT_DURATION, ASSEMBLY_LIT_BUFFER);
			if (serverLevel.getGameTime() % COOK_INTERVAL != 0L) {
				HeatedMetalItem.keepHot(hearth, serverLevel.getGameTime());
				return;
			}
			int speed = 1;
			FormedMultiblock formed = hearth.getFormed();
			if (formed != null && formed.process() instanceof ForgeProcess forge) {
				speed = Math.max(1, forge.cookSpeed());
				hearth.exchangeWithFittings(serverLevel, forge);
			}
			for (int i = 0; i < speed; i++) {
				if (hearth.alloy != null) {
					hearth.tickAlloy(serverLevel);
					continue;
				}
				AbstractFurnaceBlockEntity.serverTick(serverLevel, pos, state, hearth);
				state = level.getBlockState(pos);
				if (!state.hasProperty(AbstractFurnaceBlock.LIT)) {
					return;
				}
			}
			HeatedMetalItem.keepHot(hearth, serverLevel.getGameTime());

			BlockState after = level.getBlockState(pos);
			if (hearth.isBurning() && after.hasProperty(AbstractFurnaceBlock.LIT) && !after.getValue(AbstractFurnaceBlock.LIT)) {
				level.setBlock(pos, after.setValue(AbstractFurnaceBlock.LIT, true), Block.UPDATE_CLIENTS);
			}
		} else {
			if (hearth.dataAccess.get(DATA_LIT_TIME) > 0) {
				hearth.dataAccess.set(DATA_LIT_TIME, 0);
			}
			if (serverLevel.getGameTime() % COOK_INTERVAL != 0L) {
				HeatedMetalItem.keepHot(hearth, serverLevel.getGameTime());
				return;
			}
			AbstractFurnaceBlockEntity.serverTick(serverLevel, pos, state, hearth);
			HeatedMetalItem.keepHot(hearth, serverLevel.getGameTime());
		}
	}

	/**
	 * Results, and inputs the hearth cannot fire (such as a crucible after firing), go out to the
	 * tapping spouts in turn. With the input slot empty, a crucible alloy is tried first, then the
	 * input slot fills from the forge inputs in turn. No new input is taken while an alloy cooks.
	 */
	private void exchangeWithFittings(ServerLevel level, ForgeProcess forge) {
		List<TappingSpoutBlockEntity> spouts = fittings(level, forge.spouts(), TappingSpoutBlockEntity.class);
		if (!spouts.isEmpty()) {
			this.items.set(SLOT_RESULT, this.tapInto(spouts, this.items.get(SLOT_RESULT)));
			ItemStack input = this.items.get(SLOT_INPUT);
			if (!input.isEmpty() && !canFire(level, input)) {
				this.setItem(SLOT_INPUT, this.tapInto(spouts, input));
			}
		}
		List<ForgeInputBlockEntity> inputs = fittings(level, forge.inputs(), ForgeInputBlockEntity.class);
		if (!inputs.isEmpty() && this.alloy == null) {
			if (this.items.get(SLOT_INPUT).isEmpty() && this.startAlloy(level, inputs, forge)) {
				return;
			}
			this.feedFrom(level, inputs);
		}
	}

	/** The alloy being made from the forge inputs, or empty. */
	public ItemStack alloyResult() {
		return this.alloy == null ? ItemStack.EMPTY : this.alloy.createResult();
	}

	/**
	 * Starts the first crucible alloy whose ingredients each sit in a different input, taking them
	 * from those inputs. Alloys that make steel also need every hearth lined.
	 */
	private boolean startAlloy(ServerLevel level, List<ForgeInputBlockEntity> inputs, ForgeProcess forge) {
		boolean fullyLined = forge.linedHearths() >= forge.hearthCount();
		List<CrucibleAlloyingRecipe> recipes = ModRecipes.listAlloying(level);
		recipes.sort(Comparator.comparingInt((CrucibleAlloyingRecipe recipe) -> recipe.ingredients().size()).reversed());
		for (CrucibleAlloyingRecipe recipe : recipes) {
			List<CountedIngredient> ingredients = recipe.ingredients();
			if (ingredients.isEmpty() || ingredients.size() > inputs.size()) {
				continue;
			}
			if (!fullyLined && recipe.result().create().is(ModItems.STEEL_INGOT)) {
				continue;
			}
			if (!this.resultFits(recipe.result().create())) {
				continue;
			}
			int[] chosen = new int[ingredients.size()];
			if (!assignInputs(ingredients, inputs, 0, chosen, new boolean[inputs.size()])) {
				continue;
			}
			for (int i = 0; i < ingredients.size(); i++) {
				takeMatching(inputs.get(chosen[i]), ingredients.get(i));
			}
			this.alloy = recipe.createCharge();
			this.alloyProgress = 0;
			this.setChanged();
			return true;
		}
		return false;
	}

	private static boolean assignInputs(
			List<CountedIngredient> ingredients,
			List<ForgeInputBlockEntity> inputs,
			int index,
			int[] chosen,
			boolean[] used
	) {
		if (index == ingredients.size()) {
			return true;
		}
		CountedIngredient need = ingredients.get(index);
		for (int i = 0; i < inputs.size(); i++) {
			if (used[i] || countMatching(inputs.get(i), need) < need.count()) {
				continue;
			}
			used[i] = true;
			chosen[index] = i;
			if (assignInputs(ingredients, inputs, index + 1, chosen, used)) {
				return true;
			}
			used[i] = false;
		}
		return false;
	}

	private static int countMatching(ForgeInputBlockEntity input, CountedIngredient ingredient) {
		int count = 0;
		for (int slot = 0; slot < input.getContainerSize(); slot++) {
			ItemStack stack = input.getItem(slot);
			if (!stack.isEmpty() && ingredient.test(stack)) {
				count += stack.getCount();
			}
		}
		return count;
	}

	private static void takeMatching(ForgeInputBlockEntity input, CountedIngredient ingredient) {
		int left = ingredient.count();
		for (int slot = 0; slot < input.getContainerSize() && left > 0; slot++) {
			ItemStack stack = input.getItem(slot);
			if (!stack.isEmpty() && ingredient.test(stack)) {
				left -= input.removeItem(slot, left).getCount();
			}
		}
	}

	private boolean resultFits(ItemStack result) {
		ItemStack slot = this.items.get(SLOT_RESULT);
		return slot.isEmpty()
				|| (ItemStack.isSameItemSameComponents(slot, result)
						&& slot.getCount() + result.getCount() <= this.getMaxStackSize(slot));
	}

	/** One cook step of the alloy. A finished alloy waits until the result slot has room. */
	private void tickAlloy(ServerLevel level) {
		int total = this.alloy.cookingTime();
		if (this.alloyProgress < total) {
			this.alloyProgress++;
		}
		this.dataAccess.set(DATA_COOKING_PROGRESS, this.alloyProgress);
		this.dataAccess.set(DATA_COOKING_TOTAL_TIME, total);
		if (this.alloyProgress < total) {
			return;
		}
		ItemStack result = this.alloy.createResult();
		if (!this.resultFits(result)) {
			return;
		}
		ItemStack slot = this.items.get(SLOT_RESULT);
		if (slot.isEmpty()) {
			this.items.set(SLOT_RESULT, result);
		} else {
			slot.grow(result.getCount());
		}
		awardExperience(level, this.worldPosition, this.alloy.experience());
		this.alloy = null;
		this.alloyProgress = 0;
		this.dataAccess.set(DATA_COOKING_PROGRESS, 0);
		this.setChanged();
	}

	private static void awardExperience(ServerLevel level, BlockPos pos, float experience) {
		int reward = Mth.floor(experience);
		float fraction = Mth.frac(experience);
		if (fraction != 0.0F && level.getRandom().nextFloat() < fraction) {
			reward++;
		}
		if (reward > 0) {
			ExperienceOrb.award(level, Vec3.atCenterOf(pos), reward);
		}
	}

	private ItemStack tapInto(List<TappingSpoutBlockEntity> spouts, ItemStack stack) {
		ItemStack remaining = stack;
		for (int i = 0; i < spouts.size() && !remaining.isEmpty(); i++) {
			int index = (this.nextSpout + i) % spouts.size();
			ItemStack left = spouts.get(index).store(remaining);
			if (left != remaining) {
				this.nextSpout = (index + 1) % spouts.size();
				this.setChanged();
			}
			remaining = left;
		}
		return remaining;
	}

	private void feedFrom(ServerLevel level, List<ForgeInputBlockEntity> inputs) {
		ItemStack current = this.items.get(SLOT_INPUT);
		if (!current.isEmpty()) {
			int space = this.getMaxStackSize(current) - current.getCount();
			for (int i = 0; i < inputs.size() && space > 0; i++) {
				ForgeInputBlockEntity input = inputs.get(i);
				for (int slot = 0; slot < input.getContainerSize() && space > 0; slot++) {
					ItemStack stack = input.getItem(slot);
					if (!stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, current)) {
						int moved = Math.min(space, stack.getCount());
						current.grow(input.removeItem(slot, moved).getCount());
						space -= moved;
						this.setChanged();
					}
				}
			}
			return;
		}
		for (int i = 0; i < inputs.size(); i++) {
			int index = (this.nextInput + i) % inputs.size();
			ForgeInputBlockEntity input = inputs.get(index);
			for (int slot = 0; slot < input.getContainerSize(); slot++) {
				ItemStack stack = input.getItem(slot);
				if (!stack.isEmpty() && canFire(level, stack)) {
					this.setItem(SLOT_INPUT, input.removeItem(slot, this.getMaxStackSize(stack)));
					this.nextInput = (index + 1) % inputs.size();
					this.setChanged();
					return;
				}
			}
		}
	}

	private static boolean canFire(ServerLevel level, ItemStack stack) {
		return CrucibleItem.isCharged(stack)
				|| level.recipeAccess().getRecipeFor(RecipeType.BLASTING, new SingleRecipeInput(stack), level).isPresent();
	}

	private static <T> List<T> fittings(ServerLevel level, List<BlockPos> positions, Class<T> type) {
		List<T> found = new ArrayList<>(positions.size());
		for (BlockPos pos : positions) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (type.isInstance(entity)) {
				found.add(type.cast(entity));
			}
		}
		return found;
	}

	private static void tickAssembly(ServerLevel level, BlockPos pos, BlockState state, ForgeHearthBlockEntity hearth) {
		if (hearth.formed == null) {
			return;
		}

		FormedMultiblock formed = hearth.formed;
		if (!pos.equals(formed.match().origin())) {
			return;
		}

		if (!hearth.participantsLinked) {
			ActiveMultiblocks.register(level, formed);
			hearth.participantsLinked = true;
		}

		formed.tick(level);

		if (!formed.isActive()) {
			ActiveMultiblocks.unregister(level, formed);
			hearth.formed = null;
			hearth.participantsLinked = false;
			hearth.dataAccess.set(DATA_LIT_TIME, 0);
			ForgeProcess.setAllHearthsLit(level, formed, false);
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (this.formed != null && this.formed.isActive()) {
			this.formed.saveAdditional(output.child("Formed"));
		}
		if (this.alloy != null) {
			output.store("ForgeAlloy", CrucibleCharge.CODEC, this.alloy);
			output.putInt("ForgeAlloyProgress", this.alloyProgress);
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		input.child("Formed").flatMap(FormedMultiblock::load).ifPresent(loaded -> {
			this.formed = loaded;
			this.participantsLinked = false;
		});
		this.alloy = input.read("ForgeAlloy", CrucibleCharge.CODEC).orElse(null);
		this.alloyProgress = input.getIntOr("ForgeAlloyProgress", 0);
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level instanceof ServerLevel serverLevel) {
			FormedMultiblock formed = this.formed;
			if (formed == null) {
				formed = ActiveMultiblocks.get(serverLevel, pos).orElse(null);
			}
			if (formed != null) {
				if (formed.isActive()) {
					formed.cancel(serverLevel);
				}
				ActiveMultiblocks.unregister(serverLevel, formed);
				if (this.formed == formed) {
					this.formed = null;
					this.participantsLinked = false;
				}
			}
			if (this.alloy != null) {
				Containers.dropItemStack(serverLevel, pos.getX(), pos.getY(), pos.getZ(), this.alloy.createResult());
				this.alloy = null;
			}
		}
		super.preRemoveSideEffects(pos, state);
	}
}
