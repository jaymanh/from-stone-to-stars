package jaymanh.fsts.multiblock;

import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;

/**
 * One requirement of a fuzzy assembly (fuel count, bellows facing, chimney, …).
 */
public interface ComponentRule {
	/**
	 * Stable id for fail messages / JEI (e.g. {@code fuel_pit}, {@code bellows}).
	 */
	String id();

	MultiblockRole role();

	boolean required();

	/**
	 * Human-readable rule for JEI and light-fail tooltips.
	 */
	Component description();

	/**
	 * Resolve this rule around {@code controller}. Empty means the rule failed.
	 */
	Optional<ResolvedComponent> resolve(LevelAccessor level, BlockPos controller);

	record ResolvedComponent(List<BlockPos> positions, MultiblockRole role, String ruleId) {
		public ResolvedComponent {
			positions = List.copyOf(positions);
		}
	}
}
