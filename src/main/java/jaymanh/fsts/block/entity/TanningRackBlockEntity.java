package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.TanningRackBlock;
import jaymanh.fsts.block.TanningRackBlock.HideStage;
import jaymanh.fsts.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TanningRackBlockEntity extends BlockEntity {
	public static final int SLOT_COUNT = 4;
	public static final int TANNING_TICKS = 5 * 60 * 20;

	private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private final int[] progress = new int[SLOT_COUNT];

	public TanningRackBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.TANNING_RACK, pos, state);
	}

	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	public boolean insertRawLeather() {
		for (int slot = 0; slot < SLOT_COUNT; slot++) {
			if (this.items.get(slot).isEmpty()) {
				this.items.set(slot, new ItemStack(ModItems.RAW_LEATHER));
				this.progress[slot] = 0;
				this.syncState();
				return true;
			}
		}
		return false;
	}

	public boolean removeOutput(Player player) {
		for (int slot = SLOT_COUNT - 1; slot >= 0; slot--) {
			ItemStack stack = this.items.get(slot);
			if (stack.is(Items.LEATHER)) {
				this.items.set(slot, ItemStack.EMPTY);
				this.progress[slot] = 0;
				player.getInventory().placeItemBackInInventory(stack);
				this.syncState();
				return true;
			}
		}
		return false;
	}

	public boolean removeAll(Player player) {
		boolean removed = false;
		for (int slot = SLOT_COUNT - 1; slot >= 0; slot--) {
			ItemStack stack = this.items.get(slot);
			if (!stack.isEmpty()) {
				this.items.set(slot, ItemStack.EMPTY);
				this.progress[slot] = 0;
				player.getInventory().placeItemBackInInventory(stack);
				removed = true;
			}
		}
		if (removed) {
			this.syncState();
		}
		return removed;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, TanningRackBlockEntity rack) {
		boolean changed = false;
		for (int slot = 0; slot < SLOT_COUNT; slot++) {
			ItemStack stack = rack.items.get(slot);
			if (!stack.is(ModItems.RAW_LEATHER)) {
				if (rack.progress[slot] != 0 && stack.isEmpty()) {
					rack.progress[slot] = 0;
					changed = true;
				}
				continue;
			}

			rack.progress[slot]++;
			if (rack.progress[slot] >= TANNING_TICKS) {
				rack.items.set(slot, new ItemStack(Items.LEATHER));
				rack.progress[slot] = 0;
				changed = true;
			}
		}

		if (changed) {
			rack.syncState();
		}
	}

	private void syncState() {
		if (this.level == null || this.level.isClientSide()) {
			return;
		}

		BlockState state = this.getBlockState();
		for (int slot = 0; slot < SLOT_COUNT; slot++) {
			state = state.setValue(TanningRackBlock.SLOTS[slot], stageOf(this.items.get(slot)));
		}

		if (state != this.getBlockState()) {
			this.level.setBlock(this.worldPosition, state, Block.UPDATE_ALL);
		}
		this.setChanged();
	}

	private static HideStage stageOf(ItemStack stack) {
		if (stack.is(ModItems.RAW_LEATHER)) {
			return HideStage.RAW;
		}
		if (stack.is(Items.LEATHER)) {
			return HideStage.TANNED;
		}
		return HideStage.EMPTY;
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
		output.putIntArray("Progress", this.progress);
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
		int[] loaded = input.getIntArray("Progress").orElse(new int[SLOT_COUNT]);
		System.arraycopy(loaded, 0, this.progress, 0, Math.min(loaded.length, SLOT_COUNT));
	}
}
