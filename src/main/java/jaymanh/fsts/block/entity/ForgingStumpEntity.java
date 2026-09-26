package jaymanh.fsts.block.entity;

import java.util.Optional;

import jaymanh.fsts.block.ModCustomBlockModels.ForgingStumpBlock;
import jaymanh.fsts.item.HeatedMetalItem;
import jaymanh.fsts.recipe.ForgingRecipe;
import jaymanh.fsts.recipe.ModRecipes;
import jaymanh.fsts.tools.ModTools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;

/**
 * Forging stump: place heated metal, hammer with a bronze hammer into parts.
 * Heat continues to cool here; if the metal cools mid-work, progress resets.
 */
public class ForgingStumpEntity extends BlockEntity implements WorldlyContainer {
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int SLOT_COUNT = 2;

	private static final int[] NO_SLOTS = new int[0];

	private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private int progress;

	public ForgingStumpEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FORGING_STUMP, pos, state);
	}

	public int getProgress() {
		return this.progress;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ForgingStumpEntity stump) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}
		ItemStack input = stump.items.get(INPUT_SLOT);
		if (!HeatedMetalItem.isHeated(input)) {
			return;
		}
		ItemStack after = HeatedMetalItem.coolIfExpired(input, serverLevel.getGameTime());
		if (after != input && !ItemStack.isSameItemSameComponents(after, input)) {
			stump.items.set(INPUT_SLOT, after);
			stump.progress = 0;
			stump.syncState();
		}
	}

	public boolean acceptsInput(ItemStack stack) {
		if (!HeatedMetalItem.isHeated(stack)) {
			return false;
		}
		ItemStack input = this.items.get(INPUT_SLOT);
		if (input.isEmpty()) {
			return this.findAnyForgingIngredient(stack);
		}
		return HeatedMetalItem.isSameHeatedMetal(input, stack);
	}

	private boolean findAnyForgingIngredient(ItemStack stack) {
		if (this.level == null || !(this.level.recipeAccess() instanceof net.minecraft.world.item.crafting.RecipeManager recipes)) {
			return false;
		}
		for (RecipeHolder<?> holder : recipes.getRecipes()) {
			if (holder.value() instanceof ForgingRecipe forging && forging.ingredient().test(stack)) {
				return true;
			}
		}
		return false;
	}

	public boolean insertInput(ItemStack stack, Player player) {
		if (!this.acceptsInput(stack)) {
			return false;
		}

		ItemStack input = this.items.get(INPUT_SLOT);
		if (input.isEmpty()) {
			this.items.set(INPUT_SLOT, stack.copyWithCount(1));
			this.progress = 0;
			if (!player.hasInfiniteMaterials()) {
				stack.shrink(1);
			}
			this.syncState();
			return true;
		}

		if (input.getCount() >= input.getMaxStackSize()) {
			return false;
		}

		HeatedMetalItem.keepEarlierHeat(input, stack);
		input.grow(1);
		this.progress = 0;
		if (!player.hasInfiniteMaterials()) {
			stack.shrink(1);
		}
		this.syncState();
		return true;
	}

	public boolean hammer(Player player, ItemStack hammer) {
		if (!hammer.is(ModTools.BRONZE_HAMMER)) {
			return false;
		}

		ItemStack input = this.items.get(INPUT_SLOT);
		Optional<RecipeHolder<ForgingRecipe>> recipe = ModRecipes.findForging(this.level, input);
		if (recipe.isEmpty()) {
			return false;
		}

		ForgingRecipe forging = recipe.get().value();
		ItemStack result = forging.resultStack();
		if (result.isEmpty()) {
			return false;
		}

		ItemStack output = this.items.get(OUTPUT_SLOT);
		boolean completing = this.progress + 1 >= forging.hits();
		if (completing && !this.canAcceptResult(output, result)) {
			return false;
		}

		this.progress++;
		if (!player.hasInfiniteMaterials()) {
			hammer.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
		}

		if (this.progress >= forging.hits()) {
			this.progress = 0;
			input.shrink(forging.ingredientCount());
			if (input.isEmpty()) {
				this.items.set(INPUT_SLOT, ItemStack.EMPTY);
			}

			if (output.isEmpty()) {
				this.items.set(OUTPUT_SLOT, result.copy());
			} else {
				output.grow(result.getCount());
			}
		}

		this.syncState();
		return true;
	}

	private boolean canAcceptResult(ItemStack output, ItemStack result) {
		if (output.isEmpty()) {
			return true;
		}
		if (!ItemStack.isSameItemSameComponents(output, result)) {
			return false;
		}
		return output.getCount() + result.getCount() <= output.getMaxStackSize();
	}

	public boolean takeOutput(Player player) {
		ItemStack output = this.items.get(OUTPUT_SLOT);
		if (output.isEmpty()) {
			return false;
		}
		this.items.set(OUTPUT_SLOT, ItemStack.EMPTY);
		player.getInventory().placeItemBackInInventory(output);
		this.syncState();
		return true;
	}

	public boolean extractAll(Player player) {
		boolean removed = false;
		ItemStack output = this.items.get(OUTPUT_SLOT);
		if (!output.isEmpty()) {
			this.items.set(OUTPUT_SLOT, ItemStack.EMPTY);
			player.getInventory().placeItemBackInInventory(output);
			removed = true;
		}
		ItemStack input = this.items.get(INPUT_SLOT);
		if (!input.isEmpty()) {
			this.items.set(INPUT_SLOT, ItemStack.EMPTY);
			this.progress = 0;
			player.getInventory().placeItemBackInInventory(input);
			removed = true;
		}
		if (removed) {
			this.syncState();
		}
		return removed;
	}

	private void syncState() {
		if (this.level == null || this.level.isClientSide()) {
			return;
		}
		int visual = Math.min(this.progress, ForgingStumpBlock.MAX_PROGRESS);
		BlockState state = this.getBlockState().setValue(ForgingStumpBlock.PROGRESS, visual);
		if (state != this.getBlockState()) {
			this.level.setBlock(this.worldPosition, state, Block.UPDATE_ALL);
		}
		this.setChanged();
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level != null) {
			Containers.dropContents(this.level, pos, this.items);
		}
		super.preRemoveSideEffects(pos, state);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		ValueOutput.TypedOutputList<ItemStack> list = output.list("Items", ItemStack.OPTIONAL_CODEC);
		for (ItemStack stack : this.items) {
			list.add(stack);
		}
		output.putInt("Progress", this.progress);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.items.clear();
		int slot = 0;
		for (ItemStack stack : input.listOrEmpty("Items", ItemStack.OPTIONAL_CODEC)) {
			if (slot < SLOT_COUNT) {
				this.items.set(slot++, stack);
			}
		}
		this.progress = Math.max(0, input.getIntOr("Progress", 0));
	}

	@Override
	public int getContainerSize() {
		return SLOT_COUNT;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.items) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.items.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack removed = ContainerHelper.removeItem(this.items, slot, amount);
		if (!removed.isEmpty()) {
			if (slot == INPUT_SLOT) {
				this.progress = 0;
			}
			this.syncState();
		}
		return removed;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(this.items, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		this.items.set(slot, stack);
		stack.limitSize(this.getMaxStackSize(stack));
		if (slot == INPUT_SLOT) {
			this.progress = 0;
		}
		this.syncState();
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return slot == INPUT_SLOT && this.acceptsInput(stack);
	}

	@Override
	public void clearContent() {
		this.items.clear();
		this.progress = 0;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return NO_SLOTS;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return false;
	}
}
