package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.ModCustomBlockModels.TripHammerBlock;
import jaymanh.fsts.rotation.RotationConstants;
import jaymanh.fsts.rotation.RotationNetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Strikes the forging stump underneath while the shaft network is spinning.
 * The head stays raised, then drops for the last few ticks of each cycle.
 */
public class TripHammerBlockEntity extends AbstractRotationBlockEntity {
	/** How long the head stays down after each strike, in ticks. */
	private static final int FALL_TICKS = 4;

	private int poweredTicks;

	public TripHammerBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.TRIP_HAMMER, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, TripHammerBlockEntity entity) {
		if (!entity.isSpinning() || !entity.isNetworkActive()) {
			entity.poweredTicks = 0;
			entity.setLifted(level, pos, false);
			return;
		}

		int cycle = RotationConstants.TRIP_HAMMER_TICKS_PER_HIT;
		entity.poweredTicks++;
		if (entity.poweredTicks > cycle) {
			entity.poweredTicks = 1;
		}

		int fallAt = cycle - FALL_TICKS + 1;
		entity.setLifted(level, pos, entity.poweredTicks < fallAt);
		if (entity.poweredTicks != fallAt) {
			return;
		}

		if (!(level.getBlockEntity(pos.below()) instanceof ForgingStumpEntity stump) || !stump.strike()) {
			return;
		}

		level.playSound(null, pos.below(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.4F, 0.85F);
		if (stump.finishedRecipe() && level instanceof ServerLevel serverLevel) {
			RotationNetwork.grantUnderLoadAdvancement(serverLevel, pos);
		}
	}

	private void setLifted(Level level, BlockPos pos, boolean lifted) {
		BlockState current = level.getBlockState(pos);
		if (!current.is(this.getBlockState().getBlock()) || current.getValue(TripHammerBlock.LIFTED) == lifted) {
			return;
		}
		level.setBlock(pos, current.setValue(TripHammerBlock.LIFTED, lifted), Block.UPDATE_CLIENTS);
	}
}
