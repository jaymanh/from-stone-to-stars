package jaymanh.fsts.rotation;

import jaymanh.fsts.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class RotationConnector {
	private RotationConnector() {
	}

	public static boolean isNetworkBlock(BlockState state) {
		return state.is(ModBlocks.WATER_WHEEL)
				|| state.is(ModBlocks.WOODEN_SHAFT)
				|| state.is(ModBlocks.ROTATION_GEAR)
				|| state.is(ModBlocks.POWERED_QUERN);
	}

	public static boolean canConnect(Level level, BlockPos from, BlockPos to, Direction direction) {
		BlockState fromState = level.getBlockState(from);
		BlockState toState = level.getBlockState(to);
		if (!isNetworkBlock(fromState) || !isNetworkBlock(toState)) {
			return false;
		}

		if (fromState.is(ModBlocks.ROTATION_GEAR) || toState.is(ModBlocks.ROTATION_GEAR)) {
			return true;
		}

		if (fromState.is(ModBlocks.WATER_WHEEL)) {
			return waterWheelConnects(fromState, direction);
		}
		if (toState.is(ModBlocks.WATER_WHEEL)) {
			return waterWheelConnects(toState, direction.getOpposite());
		}

		if (fromState.is(ModBlocks.POWERED_QUERN)) {
			return poweredQuernConnects(fromState, direction);
		}
		if (toState.is(ModBlocks.POWERED_QUERN)) {
			return poweredQuernConnects(toState, direction.getOpposite());
		}

		if (fromState.is(ModBlocks.WOODEN_SHAFT) && toState.is(ModBlocks.WOODEN_SHAFT)) {
			return shaftConnects(fromState, toState, direction);
		}

		if (fromState.is(ModBlocks.WOODEN_SHAFT)) {
			return shaftConnectsToNode(fromState, direction);
		}
		if (toState.is(ModBlocks.WOODEN_SHAFT)) {
			return shaftConnectsToNode(toState, direction.getOpposite());
		}

		return true;
	}

	private static boolean waterWheelConnects(BlockState state, Direction direction) {
		Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction.Axis axle = facing.getClockWise().getAxis();
		return direction.getAxis() == axle;
	}

	private static boolean poweredQuernConnects(BlockState state, Direction direction) {
		return direction == state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
	}

	private static boolean shaftConnects(BlockState from, BlockState to, Direction direction) {
		if (!shaftConnectsToNode(from, direction)) {
			return false;
		}
		return direction.getAxis() == from.getValue(BlockStateProperties.AXIS);
	}

	private static boolean shaftConnectsToNode(BlockState shaft, Direction direction) {
		return direction.getAxis() == shaft.getValue(BlockStateProperties.AXIS);
	}
}
