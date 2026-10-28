package jaymanh.fsts.inventory;

import jaymanh.fsts.block.entity.ForgeStoreBlockEntity;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * One row like a chest, but the spout slots are take-only. Only the forge fills the spout.
 */
public class TappingSpoutMenu extends AbstractContainerMenu {
	public static final int SLOT_X = 8;
	public static final int SLOT_Y = 18;
	public static final int INVENTORY_Y = SLOT_Y + 18 + 13;

	private static final int SPOUT_END = ForgeStoreBlockEntity.SIZE;
	private static final int INV_START = SPOUT_END;
	private static final int INV_END = INV_START + 27;
	private static final int HOTBAR_END = INV_END + 9;

	private final Container container;

	public TappingSpoutMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, new SimpleContainer(ForgeStoreBlockEntity.SIZE));
	}

	public TappingSpoutMenu(int containerId, Inventory playerInventory, Container container) {
		super(ModMenus.TAPPING_SPOUT, containerId);
		checkContainerSize(container, ForgeStoreBlockEntity.SIZE);
		this.container = container;
		container.startOpen(playerInventory.player);
		for (int slot = 0; slot < ForgeStoreBlockEntity.SIZE; slot++) {
			this.addSlot(new OutputSlot(container, slot, SLOT_X + slot * 18, SLOT_Y));
		}
		this.addStandardInventorySlots(playerInventory, SLOT_X, INVENTORY_Y);
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = this.slots.get(index);
		if (slot == null || !slot.hasItem()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getItem();
		ItemStack original = stack.copy();
		if (index < SPOUT_END) {
			if (!this.moveItemStackTo(stack, INV_START, HOTBAR_END, true)) {
				return ItemStack.EMPTY;
			}
		} else if (index < INV_END) {
			if (!this.moveItemStackTo(stack, INV_END, HOTBAR_END, false)) {
				return ItemStack.EMPTY;
			}
		} else if (!this.moveItemStackTo(stack, INV_START, INV_END, false)) {
			return ItemStack.EMPTY;
		}

		if (stack.isEmpty()) {
			slot.setByPlayer(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}
		return original;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.container.stopOpen(player);
	}

	private static final class OutputSlot extends Slot {
		private OutputSlot(Container container, int slot, int x, int y) {
			super(container, slot, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return false;
		}
	}
}
