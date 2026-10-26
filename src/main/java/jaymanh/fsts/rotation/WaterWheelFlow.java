package jaymanh.fsts.rotation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

public final class WaterWheelFlow {
	public enum Strength {
		NONE,
		WEAK,
		STRONG;

		public int capacity() {
			return switch (this) {
				case STRONG -> 2;
				case WEAK -> 1;
				case NONE -> 0;
			};
		}
	}

	private WaterWheelFlow() {
	}

	public static Strength evaluate(Level level, BlockPos pos) {
		int flowingSides = 0;
		int flowAmount = 0;

		for (Direction direction : Direction.values()) {
			BlockPos check = pos.relative(direction);
			FluidState fluid = level.getFluidState(check);
			if (hasActiveFlow(level, check, fluid)) {
				flowingSides++;
				flowAmount += fluid.getAmount();
			}
		}

		FluidState self = level.getFluidState(pos);
		if (hasActiveFlow(level, pos, self)) {
			flowingSides++;
			flowAmount += self.getAmount();
		}

		if (flowingSides == 0) {
			return Strength.NONE;
		}
		if (flowingSides >= 2 || flowAmount >= 12) {
			return Strength.STRONG;
		}
		return Strength.WEAK;
	}

	private static boolean hasActiveFlow(Level level, BlockPos pos, FluidState fluid) {
		if (!fluid.is(FluidTags.WATER)) {
			return false;
		}
		return fluid.getFlow(level, pos).lengthSqr() > 1.0E-6;
	}
}
