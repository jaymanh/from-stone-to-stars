package jaymanh.fsts.multiblock;

import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;

/**
 * A rule that contributes several role-tagged position sets in one resolve pass
 * (used by the forge layout matcher).
 */
public interface MultiRoleRule extends ComponentRule {
	Optional<List<ResolvedComponent>> resolveAll(LevelAccessor level, BlockPos controller);

	/**
	 * Resolve with a specific failure reason when incomplete.
	 */
	default Attempt attemptAll(LevelAccessor level, BlockPos controller) {
		Optional<List<ResolvedComponent>> components = resolveAll(level, controller);
		if (components.isPresent()) {
			return Attempt.success(components.get());
		}
		return Attempt.fail(description());
	}

	@Override
	default Optional<ResolvedComponent> resolve(LevelAccessor level, BlockPos controller) {
		return resolveAll(level, controller).flatMap(list -> list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst()));
	}

	record Attempt(Optional<List<ResolvedComponent>> components, Optional<Component> failure) {
		public static Attempt success(List<ResolvedComponent> components) {
			return new Attempt(Optional.of(components), Optional.empty());
		}

		public static Attempt fail(Component reason) {
			return new Attempt(Optional.empty(), Optional.of(reason));
		}

		public boolean ok() {
			return this.components.isPresent();
		}
	}
}
