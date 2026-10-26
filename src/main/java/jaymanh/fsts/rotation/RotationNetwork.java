package jaymanh.fsts.rotation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jaymanh.fsts.advancement.AgeAdvancements;
import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.entity.AbstractRotationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class RotationNetwork {
	private RotationNetwork() {
	}

	public static void update(Level level, BlockPos origin) {
		if (level.isClientSide()) {
			return;
		}

		Set<BlockPos> component = collectComponent(level, origin);
		if (component.isEmpty()) {
			return;
		}

		List<BlockPos> wheels = new ArrayList<>();
		List<BlockPos> machines = new ArrayList<>();
		for (BlockPos pos : component) {
			BlockState state = level.getBlockState(pos);
			if (state.is(ModBlocks.WATER_WHEEL)) {
				wheels.add(pos);
			} else if (isMachine(state)) {
				machines.add(pos);
			}
		}

		Map<BlockPos, Integer> distanceToWheel = distancesToNearestWheel(level, component, wheels);
		int totalCapacity = 0;
		boolean anyFlow = false;
		for (BlockPos wheel : wheels) {
			WaterWheelFlow.Strength strength = WaterWheelFlow.evaluate(level, wheel);
			if (strength != WaterWheelFlow.Strength.NONE) {
				anyFlow = true;
				totalCapacity += strength.capacity();
			}
		}

		int inRangeMachineCost = 0;
		for (BlockPos machine : machines) {
			int distance = distanceToWheel.getOrDefault(machine, Integer.MAX_VALUE);
			if (distance <= RotationConstants.MAX_SHAFT_LENGTH) {
				inRangeMachineCost += RotationConstants.MACHINE_COST;
			}
		}

		boolean overloaded = inRangeMachineCost > totalCapacity;
		boolean networkCanTurn = anyFlow && !overloaded && component.size() > 1;
		long gameTime = level.getGameTime();

		for (BlockPos pos : component) {
			BlockState state = level.getBlockState(pos);
			RotationStatus status = resolveStatus(pos, distanceToWheel, anyFlow, overloaded);
			boolean spinning = status == RotationStatus.SPINNING;

			if (state.hasProperty(BlockStateProperties.POWERED) && state.getValue(BlockStateProperties.POWERED) != spinning) {
				level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, spinning), 3);
			}

			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof AbstractRotationBlockEntity rotation) {
				rotation.applyNetworkState(spinning, status, networkCanTurn, gameTime);
			}

			if (spinning && state.is(ModBlocks.WATER_WHEEL) && level instanceof ServerLevel serverLevel) {
				AgeAdvancements.grantNearby(serverLevel, pos, "age1/wheel_and_water", "spinning");
			}
		}
	}

	private static RotationStatus resolveStatus(BlockPos pos, Map<BlockPos, Integer> distanceToWheel, boolean anyFlow, boolean overloaded) {
		int distance = distanceToWheel.getOrDefault(pos, Integer.MAX_VALUE);
		if (distance > RotationConstants.MAX_SHAFT_LENGTH) {
			return RotationStatus.OUT_OF_RANGE;
		}
		if (!anyFlow) {
			return RotationStatus.NO_FLOW;
		}
		if (overloaded) {
			return RotationStatus.OVERLOADED;
		}
		return RotationStatus.SPINNING;
	}

	private static boolean isMachine(BlockState state) {
		return state.is(ModBlocks.POWERED_QUERN) || state.is(ModBlocks.TRIP_HAMMER) || state.is(ModBlocks.BRONZE_HOPPER);
	}

	private static Set<BlockPos> collectComponent(Level level, BlockPos origin) {
		if (!RotationConnector.isNetworkBlock(level.getBlockState(origin))) {
			return Set.of();
		}

		Set<BlockPos> visited = new HashSet<>();
		Queue<BlockPos> queue = new ArrayDeque<>();
		queue.add(origin);
		visited.add(origin);

		while (!queue.isEmpty()) {
			BlockPos current = queue.remove();
			for (Direction direction : Direction.values()) {
				BlockPos neighbor = current.relative(direction);
				if (visited.contains(neighbor)) {
					continue;
				}
				if (!RotationConnector.canConnect(level, current, neighbor, direction)) {
					continue;
				}
				visited.add(neighbor);
				queue.add(neighbor);
			}
		}
		return visited;
	}

	private static Map<BlockPos, Integer> distancesToNearestWheel(Level level, Set<BlockPos> component, List<BlockPos> wheels) {
		Map<BlockPos, Integer> distances = new HashMap<>();
		if (wheels.isEmpty()) {
			for (BlockPos pos : component) {
				distances.put(pos, Integer.MAX_VALUE);
			}
			return distances;
		}

		Queue<BlockPos> queue = new ArrayDeque<>();
		for (BlockPos wheel : wheels) {
			distances.put(wheel, 0);
			queue.add(wheel);
		}

		while (!queue.isEmpty()) {
			BlockPos current = queue.remove();
			int currentDistance = distances.getOrDefault(current, Integer.MAX_VALUE);
			for (Direction direction : Direction.values()) {
				BlockPos neighbor = current.relative(direction);
				if (!component.contains(neighbor)) {
					continue;
				}
				if (!RotationConnector.canConnect(level, current, neighbor, direction)) {
					continue;
				}
				int next = currentDistance + 1;
				Integer existing = distances.get(neighbor);
				if (existing == null || next < existing) {
					distances.put(neighbor, next);
					queue.add(neighbor);
				}
			}
		}

		for (BlockPos pos : component) {
			distances.putIfAbsent(pos, Integer.MAX_VALUE);
		}
		return distances;
	}

	public static void grantUnderLoadAdvancement(ServerLevel level, BlockPos pos) {
		AgeAdvancements.grantNearby(level, pos, "age1/under_load", "powered_grind");
		AgeAdvancements.grantNearby(level, pos, "age1/bronze_yard", "powered_process");
	}
}
