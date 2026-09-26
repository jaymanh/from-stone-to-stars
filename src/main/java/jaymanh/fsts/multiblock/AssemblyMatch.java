package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LevelAccessor;

/**
 * Result of resolving an {@link AssemblyDefinition} at a controller position.
 */
public final class AssemblyMatch implements StructureMatch {
	private final AssemblyDefinition definition;
	private final BlockPos origin;
	private final List<MultiblockMatch.Member> members;
	private final List<ComponentRule.ResolvedComponent> resolved;

	public AssemblyMatch(
			AssemblyDefinition definition,
			BlockPos origin,
			List<ComponentRule.ResolvedComponent> resolved
	) {
		this.definition = definition;
		this.origin = origin.immutable();
		this.resolved = List.copyOf(resolved);
		List<MultiblockMatch.Member> memberList = new ArrayList<>();
		for (ComponentRule.ResolvedComponent component : this.resolved) {
			for (BlockPos pos : component.positions()) {
				if (pos.equals(this.origin)) {
					continue;
				}
				memberList.add(new MultiblockMatch.Member(pos, component.role()));
			}
		}
		this.members = Collections.unmodifiableList(memberList);
	}

	public AssemblyDefinition definition() {
		return this.definition;
	}

	public List<ComponentRule.ResolvedComponent> resolved() {
		return this.resolved;
	}

	@Override
	public Identifier id() {
		return this.definition.id();
	}

	@Override
	public BlockPos origin() {
		return this.origin;
	}

	@Override
	public List<MultiblockMatch.Member> members() {
		return this.members;
	}

	@Override
	public MultiblockProcess.Factory processFactory() {
		return this.definition.processFactory();
	}

	@Override
	public boolean stillValid(LevelAccessor level) {
		return AssemblyMatcher.stillValid(level, this);
	}

	/**
	 * Failed or successful attempt, with a fail message when empty.
	 */
	public record Attempt(Optional<AssemblyMatch> match, Optional<Component> failure) {
		public static Attempt success(AssemblyMatch match) {
			return new Attempt(Optional.of(match), Optional.empty());
		}

		public static Attempt fail(Component reason) {
			return new Attempt(Optional.empty(), Optional.of(reason));
		}

		public boolean ok() {
			return this.match.isPresent();
		}
	}
}
