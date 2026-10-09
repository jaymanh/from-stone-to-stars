package jaymanh.fsts.block.entity;

import java.util.Objects;
import java.util.Optional;

import jaymanh.fsts.block.ModCustomBlockModels.ForgingStumpBlock;
import jaymanh.fsts.item.HeatedMetalItem;
import jaymanh.fsts.recipe.ForgingRecipe;
import jaymanh.fsts.recipe.ModRecipes;
import jaymanh.fsts.tags.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;

/**
 * Forging stump: heated metal in, one chosen forging recipe, then hammer hits.
 * Heat keeps cooling here. Progress resets if the metal cools, the recipe changes,
 * or the stack can no longer pay the selected recipe's ingot cost.
 */
public class ForgingStumpEntity extends BlockEntity implements WorldlyContainer {
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int SLOT_COUNT = 2;

	private static final int[] NO_SLOTS = new int[0];

	private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private int progress;
	private @Nullable Identifier selectedRecipe;

	public ForgingStumpEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FORGING_STUMP, pos, state);
	}

	public int getProgress() {
		return this.progress;
	}

	public @Nullable Identifier getSelectedRecipeId() {
		return this.selectedRecipe;
	}

	/** Changing the recipe starts the hammer count over. The same id leaves progress alone. */
	public void setSelectedRecipe(@Nullable Identifier id) {
		if (Objects.equals(this.selectedRecipe, id)) {
			return;
		}
		this.selectedRecipe = id;
		this.progress = 0;
		this.syncState();
	}

	/**
	 * Drops the selection when it no longer matches the input.
	 * Skips the check until forging recipes are actually loaded, so a chunk load cannot wipe a saved recipe.
	 */
	public void ensureSelectionValid() {
		if (this.selectedRecipe == null || this.level == null) {
			return;
		}
		if (!(this.level.recipeAccess() instanceof RecipeManager) || !ModRecipes.hasAnyForgingRecipe(this.level)) {
			return;
		}
		Optional<RecipeHolder<ForgingRecipe>> holder = ModRecipes.getForging(this.level, this.selectedRecipe);
		ItemStack input = this.items.get(INPUT_SLOT);
		if (holder.isEmpty() || !holder.get().value().matches(new SingleRecipeInput(input), this.level)) {
			this.selectedRecipe = null;
			this.progress = 0;
			this.syncState();
		}
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ForgingStumpEntity stump) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}
		ItemStack input = stump.items.get(INPUT_SLOT);
		if (HeatedMetalItem.isHeated(input)) {
			ItemStack cooled = HeatedMetalItem.coolIfExpired(input, serverLevel.getGameTime());
			if (cooled != input) {
				stump.setItem(INPUT_SLOT, cooled);
			}
		}
		stump.ensureSelectionValid();
	}

	public boolean acceptsInput(ItemStack stack) {
		if (!ModRecipes.isForgingMetal(this.level, stack)) {
			return false;
		}
		ItemStack input = this.items.get(INPUT_SLOT);
		if (input.isEmpty()) {
			return true;
		}
		if (HeatedMetalItem.isHeated(input) || HeatedMetalItem.isHeated(stack)) {
			return HeatedMetalItem.isSameHeatedMetal(input, stack);
		}
		return ItemStack.isSameItem(input, stack);
	}

	public boolean hammer(Player player, ItemStack hammer) {
		if (!hammer.is(ModTags.FORGING_HAMMERS) || this.level == null || this.selectedRecipe == null) {
			return false;
		}

		Optional<RecipeHolder<ForgingRecipe>> recipe = ModRecipes.getForging(this.level, this.selectedRecipe);
		if (recipe.isEmpty()) {
			return false;
		}

		ForgingRecipe forging = recipe.get().value();
		ItemStack input = this.items.get(INPUT_SLOT);
		if (!forging.matches(new SingleRecipeInput(input), this.level)) {
			return false;
		}

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

		this.ensureSelectionValid();
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
		if (this.selectedRecipe != null) {
			output.putString("Recipe", this.selectedRecipe.toString());
		}
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
		this.selectedRecipe = null;
		input.getString("Recipe").ifPresent(value -> this.selectedRecipe = Identifier.parse(value));
		// Saves from before recipe selection have hammer progress and no recipe. That progress belongs to no craft.
		if (this.selectedRecipe == null) {
			this.progress = 0;
		}
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
				this.ensureSelectionValid();
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
		ItemStack previous = this.items.get(slot);
		this.items.set(slot, stack);
		stack.limitSize(this.getMaxStackSize(stack));
		// More of the same heated metal keeps the hammer count. A different item starts over.
		if (slot == INPUT_SLOT && !ItemStack.isSameItem(previous, stack)) {
			this.selectedRecipe = null;
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
		this.selectedRecipe = null;
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
