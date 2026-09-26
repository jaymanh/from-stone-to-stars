package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;

public final class AssemblyMatcher {
	private AssemblyMatcher() {
	}

	public static Optional<AssemblyMatch> tryMatch(LevelAccessor level, BlockPos controller, AssemblyDefinition definition) {
		return attempt(level, controller, definition).match();
	}

	/**
	 * Resolve all rules; on failure returns the first missing required rule's description.
	 */
	public static AssemblyMatch.Attempt attempt(LevelAccessor level, BlockPos controller, AssemblyDefinition definition) {
		if (!definition.controllerPredicate().test(level.getBlockState(controller))) {
			return AssemblyMatch.Attempt.fail(Component.translatable(
					"multiblock.from-stone-to-stars.fail.wrong_controller",
					definition.displayName()
			));
		}

		List<ComponentRule.ResolvedComponent> resolved = new ArrayList<>();
		for (ComponentRule rule : definition.rules()) {
			if (rule instanceof MultiRoleRule multi) {
				MultiRoleRule.Attempt multiAttempt = multi.attemptAll(level, controller);
				if (!multiAttempt.ok()) {
					if (rule.required()) {
						return AssemblyMatch.Attempt.fail(multiAttempt.failure().orElseGet(() -> Component.translatable(
								"multiblock.from-stone-to-stars.fail.missing_rule",
								definition.displayName(),
								rule.description()
						)));
					}
					continue;
				}
				resolved.addAll(multiAttempt.components().orElseThrow());
				continue;
			}

			Optional<ComponentRule.ResolvedComponent> component = rule.resolve(level, controller);
			if (component.isEmpty()) {
				if (rule.required()) {
					return AssemblyMatch.Attempt.fail(Component.translatable(
							"multiblock.from-stone-to-stars.fail.missing_rule",
							definition.displayName(),
							rule.description()
					));
				}
				continue;
			}
			resolved.add(component.get());
		}

		return AssemblyMatch.Attempt.success(new AssemblyMatch(definition, controller, resolved));
	}

	/**
	 * True while the controller remains and every originally resolved component position
	 * is still accepted by its rule (breaking matched coal / bellows cancels the process).
	 */
	public static boolean stillValid(LevelAccessor level, AssemblyMatch match) {
		AssemblyDefinition definition = match.definition();
		BlockPos origin = match.origin();
		if (!definition.controllerPredicate().test(level.getBlockState(origin))) {
			return false;
		}

		for (ComponentRule rule : definition.rules()) {
			if (rule instanceof MultiRoleRule multi) {
				Optional<List<ComponentRule.ResolvedComponent>> current = multi.resolveAll(level, origin);
				if (current.isEmpty()) {
					return false;
				}
				Set<BlockPos> currentPositions = new HashSet<>();
				for (ComponentRule.ResolvedComponent component : current.get()) {
					currentPositions.addAll(component.positions());
				}
				for (ComponentRule.ResolvedComponent original : match.resolved()) {
					if (!currentPositions.containsAll(original.positions())) {
						return false;
					}
				}
				continue;
			}

			for (ComponentRule.ResolvedComponent component : match.resolved()) {
				if (!component.ruleId().equals(rule.id())) {
					continue;
				}
				Optional<ComponentRule.ResolvedComponent> now = rule.resolve(level, origin);
				if (now.isEmpty() || !now.get().positions().containsAll(component.positions())) {
					return false;
				}
			}
		}
		return true;
	}
}
