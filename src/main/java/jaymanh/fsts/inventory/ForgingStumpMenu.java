package jaymanh.fsts.inventory;

import java.util.List;

import org.jspecify.annotations.Nullable;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.entity.ForgingStumpEntity;
import jaymanh.fsts.item.HeatedMetalItem;
import jaymanh.fsts.recipe.ForgingRecipe;
import jaymanh.fsts.recipe.ModRecipes;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

/**
 * One input, one output, and a chosen forging recipe.
 * Hammering still happens on the block. Taking the output does not spend the input.
 * Items stay in the stump when this menu closes.
 *
 * The client level does not receive forging recipes, only stonecutter recipes.
 * The server writes each matching result into a hidden slot, and sends the ingot cost and hit count
 * in their own data slots. Container data values are signed shorts, so they cannot share one int.
 * The screen reads those, never the recipe manager.
 */
public class ForgingStumpMenu extends AbstractContainerMenu {
	private static final int INV_START = 2;
	private static final int INV_END = 29;
	private static final int HOTBAR_START = 29;
	private static final int HOTBAR_END = 38;
	private static final int ICON_START = HOTBAR_END;
	private static final int MAX_RECIPES = 24;

	private final ContainerLevelAccess access;
	private final Level level;
	private final @Nullable ForgingStumpEntity stump;
	/**
	 * Stored as index + 1. Zero means nothing is selected.
	 * Data slots only sync when the value differs from the remote copy, which starts at zero,
	 * so a raw index of zero would never reach the client when the menu opens.
	 */
	private final DataSlot selectedRecipeIndex = DataSlot.standalone();
	private final DataSlot progress = DataSlot.standalone();
	private final DataSlot[] recipeCost = new DataSlot[MAX_RECIPES];
	private final DataSlot[] recipeHits = new DataSlot[MAX_RECIPES];
	private final SimpleContainer icons = new SimpleContainer(MAX_RECIPES);
	private final Slot inputSlot;

	private List<RecipeHolder<ForgingRecipe>> recipes = List.of();
	private ItemStack trackedInput = ItemStack.EMPTY;
	private int trackedCount;
	private Runnable slotUpdateListener = () -> {};

	public ForgingStumpMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, null);
	}

	public ForgingStumpMenu(int containerId, Inventory playerInventory, @Nullable ForgingStumpEntity stump) {
		super(ModMenus.FORGING_STUMP, containerId);
		this.stump = stump;
		this.level = playerInventory.player.level();
		if (stump == null || stump.getLevel() == null) {
			this.access = ContainerLevelAccess.NULL;
		} else {
			this.access = ContainerLevelAccess.create(stump.getLevel(), stump.getBlockPos());
		}

		Container container = stump == null ? new SimpleContainer(ForgingStumpEntity.SLOT_COUNT) : stump;
		checkContainerSize(container, ForgingStumpEntity.SLOT_COUNT);
		this.inputSlot = this.addSlot(new InputSlot(container));
		this.addSlot(new OutputSlot(container));
		this.addStandardInventorySlots(playerInventory, 8, 84);
		for (int i = 0; i < MAX_RECIPES; i++) {
			this.addSlot(new IconSlot(i));
		}
		this.addDataSlot(this.selectedRecipeIndex);
		this.addDataSlot(this.progress);
		for (int i = 0; i < MAX_RECIPES; i++) {
			this.recipeCost[i] = DataSlot.standalone();
			this.addDataSlot(this.recipeCost[i]);
			this.recipeHits[i] = DataSlot.standalone();
			this.addDataSlot(this.recipeHits[i]);
		}
		this.onInputChanged(this.inputSlot.getItem());
	}

	public int getSelectedRecipeIndex() {
		return this.selectedRecipeIndex.get() - 1;
	}

	public int getProgress() {
		return this.progress.get();
	}

	public int getNumberOfVisibleRecipes() {
		int count = 0;
		for (int i = 0; i < MAX_RECIPES; i++) {
			if (this.icons.getItem(i).isEmpty()) {
				break;
			}
			count++;
		}
		return count;
	}

	public ItemStack getRecipeIcon(int index) {
		if (index < 0 || index >= MAX_RECIPES) {
			return ItemStack.EMPTY;
		}
		return this.icons.getItem(index);
	}

	public int getRecipeCost(int index) {
		if (index < 0 || index >= MAX_RECIPES) {
			return 0;
		}
		return Math.max(0, this.recipeCost[index].get());
	}

	public int getRecipeHits(int index) {
		if (index < 0 || index >= MAX_RECIPES) {
			return 0;
		}
		return Math.max(0, this.recipeHits[index].get());
	}

	public boolean hasInputItem() {
		return this.inputSlot.hasItem() && this.getNumberOfVisibleRecipes() > 0;
	}

	public boolean canCraft(int index) {
		if (index < 0 || index >= this.getNumberOfVisibleRecipes()) {
			return false;
		}
		if (this.stump != null) {
			return this.recipes.get(index).value().matches(new SingleRecipeInput(this.inputSlot.getItem()), this.level);
		}
		int cost = this.getRecipeCost(index);
		return cost > 0 && this.inputSlot.getItem().getCount() >= cost;
	}

	public void registerUpdateListener(Runnable listener) {
		this.slotUpdateListener = listener;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(this.access, player, ModBlocks.FORGING_STUMP);
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (!this.canCraft(id) || this.getSelectedRecipeIndex() == id) {
			return false;
		}
		this.setSelectedIndex(id);
		if (this.stump != null) {
			this.stump.setSelectedRecipe(this.recipes.get(id).id().identifier());
			this.progress.set(this.stump.getProgress());
		}
		return true;
	}

	@Override
	public void slotsChanged(Container container) {
		this.onInputChanged(this.inputSlot.getItem());
	}

	@Override
	public void broadcastChanges() {
		this.onInputChanged(this.inputSlot.getItem());
		if (this.stump != null) {
			this.setSelectedIndex(this.indexOf(this.stump.getSelectedRecipeId()));
			this.progress.set(this.stump.getProgress());
		}
		super.broadcastChanges();
	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return slot.index < ICON_START && super.canTakeItemForPickAll(stack, slot);
	}

	@Override
	public void clicked(int slotId, int button, ContainerInput clickType, Player player) {
		if (slotId >= ICON_START) {
			return;
		}
		if (clickType == ContainerInput.PICKUP && slotId == ForgingStumpEntity.INPUT_SLOT && (button == 0 || button == 1)) {
			ItemStack current = this.inputSlot.getItem();
			ItemStack carried = this.getCarried();
			if (!current.isEmpty()
					&& HeatedMetalItem.isSameHeatedMetal(current, carried)
					&& !ItemStack.isSameItemSameComponents(current, carried)) {
				int amount = button == 0 ? carried.getCount() : 1;
				this.setCarried(this.inputSlot.safeInsert(carried, amount));
				this.inputSlot.setChanged();
				return;
			}
		}
		super.clicked(slotId, button, clickType, player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		if (index >= ICON_START) {
			return ItemStack.EMPTY;
		}
		Slot slot = this.slots.get(index);
		if (slot == null || !slot.hasItem()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getItem();
		ItemStack original = stack.copy();
		if (index == ForgingStumpEntity.OUTPUT_SLOT) {
			if (!this.moveItemStackTo(stack, INV_START, HOTBAR_END, true)) {
				return ItemStack.EMPTY;
			}
		} else if (index == ForgingStumpEntity.INPUT_SLOT) {
			if (!this.moveItemStackTo(stack, INV_START, HOTBAR_END, false)) {
				return ItemStack.EMPTY;
			}
		} else if (this.inputSlot.mayPlace(stack)) {
			if (!this.moveHeatedIntoInput(stack)) {
				return ItemStack.EMPTY;
			}
		} else if (index >= INV_START && index < INV_END) {
			if (!this.moveItemStackTo(stack, HOTBAR_START, HOTBAR_END, false)) {
				return ItemStack.EMPTY;
			}
		} else if (index >= HOTBAR_START && index < HOTBAR_END) {
			if (!this.moveItemStackTo(stack, INV_START, INV_END, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			return ItemStack.EMPTY;
		}

		if (stack.isEmpty()) {
			slot.setByPlayer(ItemStack.EMPTY);
		}
		slot.setChanged();
		if (stack.getCount() == original.getCount()) {
			return ItemStack.EMPTY;
		}
		slot.onTake(player, stack);
		this.broadcastChanges();
		return original;
	}

	private boolean moveHeatedIntoInput(ItemStack stack) {
		ItemStack current = this.inputSlot.getItem();
		if (current.isEmpty()) {
			int move = Math.min(stack.getCount(), this.inputSlot.getMaxStackSize(stack));
			if (move <= 0) {
				return false;
			}
			this.inputSlot.setByPlayer(stack.split(move));
			return true;
		}
		if (!HeatedMetalItem.isSameHeatedMetal(current, stack)) {
			return false;
		}
		int room = this.inputSlot.getMaxStackSize(current) - current.getCount();
		if (room <= 0) {
			return false;
		}
		int move = Math.min(room, stack.getCount());
		HeatedMetalItem.keepEarlierHeat(current, stack);
		stack.shrink(move);
		current.grow(move);
		this.inputSlot.setByPlayer(current);
		return true;
	}

	private void onInputChanged(ItemStack stack) {
		boolean typeChanged = !ItemStack.isSameItem(stack, this.trackedInput);
		boolean hadItem = !this.trackedInput.isEmpty();
		boolean sameStack = !typeChanged && stack.getCount() == this.trackedCount;
		if (this.stump == null) {
			if (sameStack) {
				return;
			}
			this.trackedInput = stack.copy();
			this.trackedCount = stack.getCount();
			// The first fill must keep a selection the server already synced.
			// Replacing the metal drops it. Adding more of the same metal keeps it.
			if (hadItem && typeChanged) {
				this.setSelectedIndex(-1);
			}
			this.slotUpdateListener.run();
			return;
		}
		// An empty list only sticks when recipes were not loaded yet. Retry until they are.
		if (sameStack && (stack.isEmpty() || !this.recipes.isEmpty() || ModRecipes.hasAnyForgingRecipe(this.level))) {
			return;
		}
		this.trackedInput = stack.copy();
		this.trackedCount = stack.getCount();
		List<RecipeHolder<ForgingRecipe>> found = ModRecipes.listForgingForMetal(this.level, stack);
		if (found.size() > MAX_RECIPES) {
			found = List.copyOf(found.subList(0, MAX_RECIPES));
		}
		this.recipes = found;
		this.writeRecipeSync();
		this.stump.ensureSelectionValid();
		this.setSelectedIndex(this.indexOf(this.stump.getSelectedRecipeId()));
		this.progress.set(this.stump.getProgress());
		this.slotUpdateListener.run();
	}

	private void writeRecipeSync() {
		for (int i = 0; i < MAX_RECIPES; i++) {
			if (i < this.recipes.size()) {
				ForgingRecipe recipe = this.recipes.get(i).value();
				this.icons.setItem(i, recipe.resultStack());
				this.recipeCost[i].set(shortData(recipe.ingredientCount()));
				this.recipeHits[i].set(shortData(recipe.hits()));
			} else {
				this.icons.setItem(i, ItemStack.EMPTY);
				this.recipeCost[i].set(0);
				this.recipeHits[i].set(0);
			}
		}
	}

	private void setSelectedIndex(int index) {
		this.selectedRecipeIndex.set(index + 1);
	}

	/** Container data is a signed short. Larger numbers would arrive negative and look unaffordable. */
	private static int shortData(int value) {
		return Math.max(0, Math.min(value, Short.MAX_VALUE));
	}

	private int indexOf(@Nullable Identifier id) {
		if (id == null) {
			return -1;
		}
		for (int i = 0; i < this.recipes.size(); i++) {
			if (this.recipes.get(i).id().identifier().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	private final class InputSlot extends Slot {
		private InputSlot(Container container) {
			super(container, ForgingStumpEntity.INPUT_SLOT, 20, 33);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			ItemStack current = this.getItem();
			if (!current.isEmpty() && !HeatedMetalItem.isSameHeatedMetal(current, stack)) {
				return false;
			}
			if (!HeatedMetalItem.isHeated(stack)) {
				return false;
			}
			// The client has no forging recipe list, so it cannot know which heated metals are ingredients.
			// The server still refuses anything that has no forging recipe.
			if (ForgingStumpMenu.this.stump != null) {
				return ModRecipes.isForgingMetal(ForgingStumpMenu.this.level, stack);
			}
			return true;
		}

		/**
		 * Heated ingots carry a cool-down time, so two stacks of the same metal are not the same components.
		 * Merging keeps the earlier deadline and never adds heat.
		 */
		@Override
		public ItemStack safeInsert(ItemStack stack, int increment) {
			if (stack.isEmpty() || !this.mayPlace(stack)) {
				return stack;
			}
			ItemStack current = this.getItem();
			int room = this.getMaxStackSize(stack) - current.getCount();
			int move = Math.min(increment, Math.min(stack.getCount(), room));
			if (move <= 0) {
				return stack;
			}
			if (current.isEmpty()) {
				this.setByPlayer(stack.split(move));
			} else if (HeatedMetalItem.isSameHeatedMetal(current, stack)) {
				HeatedMetalItem.keepEarlierHeat(current, stack);
				stack.shrink(move);
				current.grow(move);
				this.setByPlayer(current);
			}
			return stack;
		}

		@Override
		public void setChanged() {
			super.setChanged();
			ForgingStumpMenu.this.slotsChanged(this.container);
		}
	}

	private static final class OutputSlot extends Slot {
		private OutputSlot(Container container) {
			super(container, ForgingStumpEntity.OUTPUT_SLOT, 143, 33);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return false;
		}
	}

	/**
	 * Result icons synced from the server. They are not a real inventory.
	 */
	private final class IconSlot extends Slot {
		private IconSlot(int index) {
			super(ForgingStumpMenu.this.icons, index, -999, -999);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return false;
		}

		@Override
		public boolean mayPickup(Player player) {
			return false;
		}

		@Override
		public boolean isActive() {
			return false;
		}

		@Override
		public void setChanged() {
			ForgingStumpMenu.this.slotUpdateListener.run();
		}
	}
}
