package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.ModCustomBlockModels.TappingSpoutBlock;
import jaymanh.fsts.item.HeatedMetalItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

/**
 * The forge's output. Only the forge fills it. Hoppers pull from any face, and it pushes one item
 * at a time into the block it faces. Heated metal stays hot while it waits here.
 */
public class TappingSpoutBlockEntity extends ForgeStoreBlockEntity {
	/** Same pace as a vanilla hopper. */
	public static final int TICKS_PER_PUSH = 8;

	private int cooldown;

	public TappingSpoutBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.TAPPING_SPOUT, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, TappingSpoutBlockEntity spout) {
		long gameTime = level.getGameTime();
		for (int slot = 0; slot < spout.getContainerSize(); slot++) {
			HeatedMetalItem.keepHot(spout.getItem(slot), gameTime);
		}
		if (--spout.cooldown > 0) {
			return;
		}
		spout.cooldown = 0;
		if (!spout.isEmpty() && pushOne(level, pos, state, spout)) {
			spout.cooldown = TICKS_PER_PUSH;
		}
	}

	private static boolean pushOne(Level level, BlockPos pos, BlockState state, TappingSpoutBlockEntity spout) {
		Direction facing = state.getValue(TappingSpoutBlock.FACING);
		Container destination = HopperBlockEntity.getContainerAt(level, pos.relative(facing));
		if (destination == null) {
			return false;
		}
		Direction into = facing.getOpposite();
		for (int slot = 0; slot < spout.getContainerSize(); slot++) {
			ItemStack stack = spout.getItem(slot);
			if (stack.isEmpty()) {
				continue;
			}
			ItemStack one = stack.copyWithCount(1);
			if (HopperBlockEntity.addItem(spout, destination, one, into).isEmpty()) {
				stack.shrink(1);
				spout.setChanged();
				destination.setChanged();
				return true;
			}
		}
		return false;
	}

	/** Moves everything into the player's inventory, dropping what does not fit. */
	public boolean takeAll(Player player) {
		boolean took = false;
		for (int slot = 0; slot < this.getContainerSize(); slot++) {
			ItemStack stack = this.getItem(slot);
			if (!stack.isEmpty()) {
				this.getItems().set(slot, ItemStack.EMPTY);
				player.getInventory().placeItemBackInInventory(stack);
				took = true;
			}
		}
		if (took) {
			this.setChanged();
		}
		return took;
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.from-stone-to-stars.tapping_spout");
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return true;
	}
}
