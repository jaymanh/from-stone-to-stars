package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.ModCustomBlockModels.MortarAndPestle;
import jaymanh.fsts.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class MortarAndPestleEntity extends BlockEntity {
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int SLOT_COUNT = 2;
	public static final int GRIND_CLICKS = 4;

	private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private int progress;

	public MortarAndPestleEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.MORTAR_AND_PESTLE, pos, state);
	}

	public int getProgress() {
		return this.progress;
	}

	public boolean insertWheat(ItemStack stack, Player player) {
		if (!stack.is(Items.WHEAT)) {
			return false;
		}

		ItemStack input = this.items.get(INPUT_SLOT);
		if (input.isEmpty()) {
			int amount = stack.getCount();
			this.items.set(INPUT_SLOT, new ItemStack(Items.WHEAT, amount));
			this.progress = 0;
			if (!player.hasInfiniteMaterials()) {
				stack.shrink(amount);
			}
			this.syncState();
			return true;
		}

		if (!input.is(Items.WHEAT)) {
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
	 * Grind one click of wheat into flour. Flour may accumulate in the output slot.
	 */
	public boolean grind() {
		ItemStack wheat = this.items.get(INPUT_SLOT);
		if (!wheat.is(Items.WHEAT) || wheat.isEmpty()) {
			return false;
		}

		ItemStack flour = this.items.get(OUTPUT_SLOT);
		boolean completing = this.progress + 1 >= GRIND_CLICKS;
		if (completing && !flour.isEmpty() && (!flour.is(ModItems.FLOUR) || flour.getCount() >= flour.getMaxStackSize())) {
			return false;
		}

		this.progress++;
		if (this.progress >= GRIND_CLICKS) {
			this.progress = 0;
			wheat.shrink(1);
			if (wheat.isEmpty()) {
				this.items.set(INPUT_SLOT, ItemStack.EMPTY);
			}

			if (flour.isEmpty()) {
				this.items.set(OUTPUT_SLOT, new ItemStack(ModItems.FLOUR));
			} else {
				flour.grow(1);
			}
		}

		this.syncState();
		return true;
	}

	public boolean takeFlour(Player player) {
		ItemStack flour = this.items.get(OUTPUT_SLOT);
		if (flour.isEmpty()) {
			return false;
		}

		this.items.set(OUTPUT_SLOT, ItemStack.EMPTY);
		player.getInventory().placeItemBackInInventory(flour);
		this.syncState();
		return true;
	}

	public boolean extractAll(Player player) {
		boolean removed = false;

		ItemStack flour = this.items.get(OUTPUT_SLOT);
		if (!flour.isEmpty()) {
			this.items.set(OUTPUT_SLOT, ItemStack.EMPTY);
			player.getInventory().placeItemBackInInventory(flour);
			removed = true;
		}

		ItemStack wheat = this.items.get(INPUT_SLOT);
		if (!wheat.isEmpty()) {
			this.items.set(INPUT_SLOT, ItemStack.EMPTY);
			this.progress = 0;
			player.getInventory().placeItemBackInInventory(wheat);
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
}
