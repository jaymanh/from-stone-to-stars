package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.multiblock.ActiveMultiblocks;
import jaymanh.fsts.multiblock.FormedMultiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;

/**
 * Log intake or plank outfeed. Holds nothing itself: it exposes the saw arbor's shared
 * input (intakes) or output (outfeeds) while the sawmill is formed, and nothing otherwise.
 */
public class SawmillPortBlockEntity extends BlockEntity implements WorldlyContainer {
	private static final int[] NO_SLOTS = {};

	/** Log or plank shown travelling over the rollers. Synced to clients for the renderer. */
	private ItemStack displayItem = ItemStack.EMPTY;
	/** Blocks between this port and the blade wall. */
	private int travelDistance = 1;

	public SawmillPortBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.SAWMILL_PORT, pos, state);
	}

	public ItemStack displayItem() {
		return this.displayItem;
	}

	public int travelDistance() {
		return this.travelDistance;
	}

	public void setDisplay(ItemStack item, int distance) {
		if (ItemStack.isSameItem(this.displayItem, item) && this.travelDistance == distance) {
			return;
		}
		this.displayItem = item.isEmpty() ? ItemStack.EMPTY : item.copyWithCount(1);
		this.travelDistance = distance;
		super.setChanged();
		if (this.level != null) {
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (!this.displayItem.isEmpty()) {
			output.store("Display", ItemStack.CODEC, this.displayItem);
		}
		output.putInt("Travel", this.travelDistance);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.displayItem = input.read("Display", ItemStack.CODEC).orElse(ItemStack.EMPTY);
		this.travelDistance = input.getIntOr("Travel", 1);
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveCustomOnly(registries);
	}

	public boolean isIntake() {
		return this.getBlockState().is(ModBlocks.LOG_INTAKE);
	}

	public @Nullable SawArborBlockEntity arbor() {
		if (this.level == null) {
			return null;
		}
		FormedMultiblock formed = ActiveMultiblocks.get(this.level, this.worldPosition).orElse(null);
		if (formed == null) {
			return null;
		}
		if (this.level.getBlockEntity(formed.match().origin()) instanceof SawArborBlockEntity arbor && arbor.isFormed()) {
			return arbor;
		}
		return null;
	}

	@Override
	public int getContainerSize() {
		SawArborBlockEntity arbor = this.arbor();
		if (arbor == null) {
			return 0;
		}
		return this.isIntake() ? arbor.inputSlots() : arbor.outputSlots();
	}

	@Override
	public boolean isEmpty() {
		int size = this.getContainerSize();
		for (int slot = 0; slot < size; slot++) {
			if (!this.getItem(slot).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		SawArborBlockEntity arbor = this.arbor();
		if (arbor == null || slot < 0 || slot >= this.getContainerSize()) {
			return ItemStack.EMPTY;
		}
		return this.isIntake() ? arbor.getInput(slot) : arbor.getOutput(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack current = this.getItem(slot);
		if (current.isEmpty() || amount <= 0) {
			return ItemStack.EMPTY;
		}
		ItemStack removed = current.split(amount);
		this.setItem(slot, current.isEmpty() ? ItemStack.EMPTY : current);
		return removed;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack current = this.getItem(slot);
		this.setItem(slot, ItemStack.EMPTY);
		return current;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		SawArborBlockEntity arbor = this.arbor();
		if (arbor == null || slot < 0 || slot >= this.getContainerSize()) {
			return;
		}
		if (this.isIntake()) {
			arbor.setInput(slot, stack);
		} else {
			arbor.setOutput(slot, stack);
		}
	}

	@Override
	public int getMaxStackSize() {
		return this.isIntake() ? SawArborBlockEntity.LOGS_PER_INFEED_ROLLER : WorldlyContainer.super.getMaxStackSize();
	}

	/** Hoppers grow the arbor's stacks in place, so the arbor must be saved too. */
	@Override
	public void setChanged() {
		super.setChanged();
		SawArborBlockEntity arbor = this.arbor();
		if (arbor != null) {
			arbor.setChanged();
		}
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		SawArborBlockEntity arbor = this.arbor();
		return arbor != null && this.isIntake() && arbor.canInsertInput(slot, stack);
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent() {
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		int size = this.getContainerSize();
		if (size == 0) {
			return NO_SLOTS;
		}
		int[] slots = new int[size];
		for (int slot = 0; slot < size; slot++) {
			slots[slot] = slot;
		}
		return slots;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
		return this.canPlaceItem(slot, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return !this.isIntake();
	}
}
