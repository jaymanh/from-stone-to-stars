package jaymanh.fsts.block.entity;

import jaymanh.fsts.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;

/**
 * Sits on the drill head and holds the steel drill rods that have not gone down the hole yet.
 * Players and hoppers can add rods. Only the player takes them back out.
 */
public class RodRackBlockEntity extends BlockEntity implements WorldlyContainer {
	public static final int CAPACITY = 8;
	private static final int[] SLOTS = {0};

	private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

	public RodRackBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ROD_RACK, pos, state);
	}

	public int rods() {
		return this.items.getFirst().getCount();
	}

	/** Takes one rod for the string. */
	public boolean takeRod() {
		ItemStack held = this.items.getFirst();
		if (held.isEmpty()) {
			return false;
		}
		held.shrink(1);
		this.setChanged();
		return true;
	}

	/**
	 * Returns rods from a recall.
	 *
	 * @return rods that did not fit
	 */
	public int addRods(int count) {
		int fit = Math.max(0, Math.min(count, CAPACITY - this.rods()));
		if (fit > 0) {
			if (this.items.getFirst().isEmpty()) {
				this.items.set(0, new ItemStack(ModItems.STEEL_DRILL_ROD, fit));
			} else {
				this.items.getFirst().grow(fit);
			}
			this.setChanged();
		}
		return count - fit;
	}

	public boolean insertRods(ItemStack stack, Player player) {
		if (!stack.is(ModItems.STEEL_DRILL_ROD)) {
			return false;
		}
		int moved = Math.min(stack.getCount(), CAPACITY - this.rods());
		if (moved <= 0) {
			return false;
		}
		this.addRods(moved);
		stack.consume(moved, player);
		return true;
	}

	public boolean takeAll(Player player) {
		ItemStack held = this.items.getFirst();
		if (held.isEmpty()) {
			return false;
		}
		this.items.set(0, ItemStack.EMPTY);
		player.getInventory().placeItemBackInInventory(held);
		this.setChanged();
		return true;
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		ContainerHelper.saveAllItems(output, this.items);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.items.clear();
		ContainerHelper.loadAllItems(input, this.items);
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level != null) {
			Containers.dropContents(this.level, pos, this.items);
		}
		super.preRemoveSideEffects(pos, state);
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return this.items.getFirst().isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.items.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack removed = ContainerHelper.removeItem(this.items, slot, amount);
		if (!removed.isEmpty()) {
			this.setChanged();
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
		this.setChanged();
	}

	@Override
	public int getMaxStackSize() {
		return CAPACITY;
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return stack.is(ModItems.STEEL_DRILL_ROD) && this.rods() < CAPACITY;
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOTS;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
		return this.canPlaceItem(slot, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return false;
	}
}
