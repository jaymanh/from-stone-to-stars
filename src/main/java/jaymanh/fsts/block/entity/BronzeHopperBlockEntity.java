package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.ModCustomBlockModels.BronzeHopperBlock;
import jaymanh.fsts.rotation.RotationConstants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Five slots, same push and pull as a hopper. Transfers only while the rotation network is spinning,
 * and waits twice as long between items as an iron hopper.
 */
public class BronzeHopperBlockEntity extends AbstractRotationBlockEntity implements Hopper, MenuProvider {
	public static final int CONTAINER_SIZE = HopperBlockEntity.HOPPER_CONTAINER_SIZE;

	private final NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
	private int cooldownTime = -1;

	public BronzeHopperBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.BRONZE_HOPPER, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, BronzeHopperBlockEntity entity) {
		entity.cooldownTime--;
		if (entity.cooldownTime > 0) {
			return;
		}
		entity.cooldownTime = 0;
		if (!entity.isSpinning()) {
			return;
		}

		boolean changed = false;
		if (!entity.isEmpty()) {
			changed = ejectItems(level, pos, entity);
		}
		if (!entity.inventoryFull()) {
			changed |= HopperBlockEntity.suckInItems(level, entity);
		}
		if (changed) {
			entity.cooldownTime = RotationConstants.BRONZE_HOPPER_TICKS_PER_MOVE;
			setChanged(level, pos, state);
		}
	}

	/**
	 * Picks up an item that falls into the bowl. Idle while the shaft is stopped or the hopper just moved an item.
	 */
	public void captureInside(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (level.isClientSide() || this.cooldownTime > 0 || !this.isSpinning()) {
			return;
		}
		if (!(entity instanceof ItemEntity itemEntity) || itemEntity.getItem().isEmpty()) {
			return;
		}
		if (!entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()).intersects(this.getSuckAabb())) {
			return;
		}
		if (HopperBlockEntity.addItem(this, itemEntity)) {
			this.cooldownTime = RotationConstants.BRONZE_HOPPER_TICKS_PER_MOVE;
			setChanged(level, pos, state);
		}
	}

	private static boolean ejectItems(Level level, BlockPos pos, BronzeHopperBlockEntity self) {
		Direction facing = self.getBlockState().getValue(BronzeHopperBlock.FACING);
		Container destination = HopperBlockEntity.getContainerAt(level, pos.relative(facing));
		if (destination == null) {
			return false;
		}

		Direction into = facing.getOpposite();
		for (int slot = 0; slot < self.getContainerSize(); slot++) {
			ItemStack stack = self.getItem(slot);
			if (stack.isEmpty()) {
				continue;
			}
			int originalCount = stack.getCount();
			ItemStack result = HopperBlockEntity.addItem(self, destination, self.removeItem(slot, 1), into);
			if (result.isEmpty()) {
				destination.setChanged();
				return true;
			}
			stack.setCount(originalCount);
			if (originalCount == 1) {
				self.setItem(slot, stack);
			}
		}
		return false;
	}

	private boolean inventoryFull() {
		for (ItemStack stack : this.items) {
			if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize()) {
				return false;
			}
		}
		return true;
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
		output.putInt("TransferCooldown", this.cooldownTime);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.items.clear();
		int slot = 0;
		for (ItemStack stack : input.listOrEmpty("Items", ItemStack.OPTIONAL_CODEC)) {
			if (slot < CONTAINER_SIZE) {
				this.items.set(slot++, stack);
			}
		}
		this.cooldownTime = input.getIntOr("TransferCooldown", -1);
	}

	@Override
	public int getContainerSize() {
		return CONTAINER_SIZE;
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
		return ContainerHelper.removeItem(this.items, slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(this.items, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		this.items.set(slot, stack);
		stack.limitSize(this.getMaxStackSize(stack));
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
	public double getLevelX() {
		return this.worldPosition.getX() + 0.5;
	}

	@Override
	public double getLevelY() {
		return this.worldPosition.getY() + 0.5;
	}

	@Override
	public double getLevelZ() {
		return this.worldPosition.getZ() + 0.5;
	}

	@Override
	public boolean isGridAligned() {
		return true;
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.from-stone-to-stars.bronze_hopper");
	}

	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
		return new HopperMenu(containerId, inventory, this);
	}
}
