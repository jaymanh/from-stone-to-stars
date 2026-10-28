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
				|| state.is(ModBlocks.POWERED_QUERN)
				|| state.is(ModBlocks.TRIP_HAMMER)
				|| state.is(ModBlocks.BRONZE_HOPPER)
				|| state.is(ModBlocks.SAW_ARBOR)
				|| state.is(ModBlocks.POWERED_BELLOWS)
				|| state.is(ModBlocks.ORE_CRUSHER);
	}

	public static boolean canConnect(Level level, BlockPos from, BlockPos to, Direction direction) {
		BlockState fromState = level.getBlockState(from);
		BlockState toState = level.getBlockState(to);
		if (!isNetworkBlock(fromState) || !isNetworkBlock(toState)) {
			return false;
		}

		boolean viaGear = fromState.is(ModBlocks.ROTATION_GEAR) || toState.is(ModBlocks.ROTATION_GEAR);
		return accepts(fromState, direction, viaGear) && accepts(toState, direction.getOpposite(), viaGear);
	}

	/** Shafts only ever connect along their axis; a gear neighbour lifts every other block's face restriction. */
	private static boolean accepts(BlockState state, Direction side, boolean viaGear) {
		if (state.is(ModBlocks.WOODEN_SHAFT)) {
			return shaftConnectsToNode(state, side);
		}
		if (viaGear || state.is(ModBlocks.ROTATION_GEAR)) {
			return true;
		}
		if (state.is(ModBlocks.BRONZE_HOPPER)) {
			return hopperAccepts(state, side);
		}
		if (state.is(ModBlocks.WATER_WHEEL)) {
			return waterWheelConnects(state, side);
		}
		if (isRearDriven(state)) {
			return driveConnects(state, side);
		}
		return true;
	}

	private static boolean waterWheelConnects(BlockState state, Direction direction) {
		Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction.Axis axle = facing.getClockWise().getAxis();
		return direction.getAxis() == axle;
	}

	/** Any face except the top intake and the spout. A gear still connects on those faces. */
	private static boolean hopperAccepts(BlockState hopper, Direction direction) {
		Direction facing = hopper.getValue(BlockStateProperties.FACING_HOPPER);
		return direction != Direction.UP && direction != facing;
	}

	private static boolean isRearDriven(BlockState state) {
		return state.is(ModBlocks.POWERED_QUERN)
				|| state.is(ModBlocks.TRIP_HAMMER)
				|| state.is(ModBlocks.SAW_ARBOR)
				|| state.is(ModBlocks.POWERED_BELLOWS)
				|| state.is(ModBlocks.ORE_CRUSHER);
	}

	/** Shaft enters the back of a facing machine, opposite the side the player looks at. */
	private static boolean driveConnects(BlockState state, Direction direction) {
		return direction == state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
	}

	private static boolean shaftConnectsToNode(BlockState shaft, Direction direction) {
		return direction.getAxis() == shaft.getValue(BlockStateProperties.AXIS);
	}
}
