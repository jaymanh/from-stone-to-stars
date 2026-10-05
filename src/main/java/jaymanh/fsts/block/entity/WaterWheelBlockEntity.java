package jaymanh.fsts.block.entity;

import jaymanh.fsts.rotation.RotationNetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WaterWheelBlockEntity extends AbstractRotationBlockEntity {
	public WaterWheelBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.WATER_WHEEL, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, WaterWheelBlockEntity entity) {
		if (level.getGameTime() % 20L == 0L) {
			RotationNetwork.update(level, pos);
		}
	}
}
