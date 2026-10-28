package jaymanh.fsts.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * One row of stacks on a forge fitting. Subclasses decide which faces hoppers may use.
 */
public abstract class ForgeStoreBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
	public static final int SIZE = 9;
	private static final int[] SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8};

	private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

	protected ForgeStoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	/**
	 * Merges into matching stacks first, then fills empty slots. Skips {@link #canPlaceItem},
	 * so the forge can fill a spout that players and hoppers cannot.
	 *
	 * @return what did not fit
	 */
	public ItemStack store(ItemStack stack) {
		if (stack.isEmpty()) {
			return stack;
		}
		ItemStack remaining = stack.copy();
		for (int slot = 0; slot < SIZE && !remaining.isEmpty(); slot++) {
			ItemStack held = this.items.get(slot);
			if (!held.isEmpty() && ItemStack.isSameItemSameComponents(held, remaining)) {
				int moved = Math.min(remaining.getCount(), this.getMaxStackSize(held) - held.getCount());
				if (moved > 0) {
					held.grow(moved);
					remaining.shrink(moved);
				}
			}
		}
		for (int slot = 0; slot < SIZE && !remaining.isEmpty(); slot++) {
			if (this.items.get(slot).isEmpty()) {
				this.items.set(slot, remaining.split(this.getMaxStackSize(remaining)));
			}
		}
		if (remaining.getCount() == stack.getCount()) {
			return stack;
		}
		this.setChanged();
		return remaining;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.items = items;
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new ChestMenu(MenuType.GENERIC_9x1, containerId, inventory, this, 1);
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOTS;
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(input, this.items);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		ContainerHelper.saveAllItems(output, this.items);
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level != null) {
			Containers.dropContents(this.level, pos, this.items);
		}
		super.preRemoveSideEffects(pos, state);
	}
}
