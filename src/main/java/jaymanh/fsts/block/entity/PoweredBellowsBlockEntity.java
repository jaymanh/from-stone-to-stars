package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.ModCustomBlockModels.PoweredBellowsBlock;
import jaymanh.fsts.rotation.RotationConstants;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Works the bag while the shaft network spins. The forge reads {@link #isDriving()} to hold its burn.
 */
public class PoweredBellowsBlockEntity extends AbstractRotationBlockEntity {
	private int strokeTicks;

	public PoweredBellowsBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.POWERED_BELLOWS, pos, state);
	}

	public boolean isDriving() {
		return this.isSpinning() && this.isNetworkActive();
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, PoweredBellowsBlockEntity entity) {
		if (!entity.isDriving()) {
			entity.strokeTicks = 0;
			entity.setCompressed(level, pos, false);
			return;
		}

		int stroke = RotationConstants.POWERED_BELLOWS_TICKS_PER_STROKE;
		entity.strokeTicks = entity.strokeTicks % stroke + 1;
		boolean compressed = entity.strokeTicks > stroke / 2;
		if (compressed && entity.strokeTicks == stroke / 2 + 1) {
			level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.25F, 0.6F + level.getRandom().nextFloat() * 0.2F);
		}
		entity.setCompressed(level, pos, compressed);
	}

	private void setCompressed(Level level, BlockPos pos, boolean compressed) {
		BlockState current = level.getBlockState(pos);
		if (!current.is(this.getBlockState().getBlock()) || current.getValue(PoweredBellowsBlock.COMPRESSED) == compressed) {
			return;
		}
		level.setBlock(pos, current.setValue(PoweredBellowsBlock.COMPRESSED, compressed), Block.UPDATE_CLIENTS);
	}
}
