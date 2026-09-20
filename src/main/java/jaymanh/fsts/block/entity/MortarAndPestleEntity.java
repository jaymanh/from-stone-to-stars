package jaymanh.fsts.block.entity;

import java.util.Optional;

import jaymanh.fsts.block.ModCustomBlockModels.MortarAndPestle;
import jaymanh.fsts.recipe.GrindingRecipe;
import jaymanh.fsts.recipe.ModRecipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;

/**
 * Exposes slots via {@link WorldlyContainer} so Jade/WTHIT can show contents.
 * Faces are closed so hoppers and pipes cannot insert or extract.
 */
public class MortarAndPestleEntity extends BlockEntity implements WorldlyContainer {
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int SLOT_COUNT = 2;
	public static final int GRIND_CLICKS = 4;

	private static final int[] NO_SLOTS = new int[0];

	private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private int progress;

	public MortarAndPestleEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.MORTAR_AND_PESTLE, pos, state);
	}

	public int getProgress() {
		return this.progress;
	}

	public boolean acceptsInput(ItemStack stack) {
		return this.findRecipe(stack).isPresent();
	}

	public boolean insertInput(ItemStack stack, Player player) {
		if (!this.acceptsInput(stack)) {
			return false;
		}

		ItemStack input = this.items.get(INPUT_SLOT);
		if (input.isEmpty()) {
			int amount = stack.getCount();
			this.items.set(INPUT_SLOT, stack.copyWithCount(amount));
			this.progress = 0;
			if (!player.hasInfiniteMaterials()) {
				stack.shrink(amount);
			}
			this.syncState();
			return true;
		}

		if (!ItemStack.isSameItem(input, stack)) {
			return false;
		}

		int space = input.getMaxStackSize() - input.getCount();
		if (space <= 0) {
			return false;
		}

		int moved = Math.min(space, stack.getCount());
		input.grow(moved);
		if (!player.hasInfiniteMaterials()) {
			stack.shrink(moved);
		}
		this.syncState();
		return true;
	}

	/**
	 * Grind one click of the input into its converted output. Output may accumulate in the output slot.
	 */
	public boolean grind() {
		ItemStack input = this.items.get(INPUT_SLOT);
		Optional<RecipeHolder<GrindingRecipe>> recipe = this.findRecipe(input);
		if (recipe.isEmpty()) {
			return false;
		}

		ItemStack result = recipe.get().value().assemble(new SingleRecipeInput(input));
		if (result.isEmpty()) {
			return false;
		}

		ItemStack output = this.items.get(OUTPUT_SLOT);
		boolean completing = this.progress + 1 >= GRIND_CLICKS;
		if (completing && !this.canAcceptResult(output, result)) {
			return false;
		}

		this.progress++;
		if (this.progress >= GRIND_CLICKS) {
			this.progress = 0;
			input.shrink(1);
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

	private Optional<RecipeHolder<GrindingRecipe>> findRecipe(ItemStack stack) {
		return ModRecipes.findGrinding(this.level, stack);
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

		BlockState state = this.getBlockState().setValue(MortarAndPestle.PROGRESS, this.progress);

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
		this.progress = Math.min(Math.max(input.getIntOr("Progress", 0), 0), GRIND_CLICKS - 1);
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
