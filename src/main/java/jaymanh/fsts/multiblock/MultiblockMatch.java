package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;

public final class MultiblockMatch implements StructureMatch {
	private final MultiblockDefinition definition;
	private final BlockPos origin;
	private final Rotation rotation;
	private final List<Member> members;

	public MultiblockMatch(
			MultiblockDefinition definition,
			BlockPos origin,
			Rotation rotation,
			List<Member> members
	) {
		this.definition = definition;
		this.origin = origin.immutable();
		this.rotation = rotation;
		this.members = Collections.unmodifiableList(new ArrayList<>(members));
	}

	public MultiblockDefinition definition() {
		return this.definition;
	}

	public Rotation rotation() {
		return this.rotation;
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
	public List<Member> members() {
		return this.members;
	}

	@Override
	public MultiblockProcess.Factory processFactory() {
		return this.definition.processFactory();
	}

	@Override
	public boolean stillValid(LevelAccessor level) {
		return MultiblockMatcher.stillValid(level, this);
	}

	@Override
	public List<BlockPos> positionsWithRole(MultiblockRole role) {
		List<BlockPos> positions = new ArrayList<>();
		if (role == MultiblockRole.ORIGIN) {
			positions.add(this.origin);
		}
		for (Member member : this.members) {
			if (member.role() == role) {
				positions.add(member.pos());
			}
		}
		return positions;
	}

	public record Member(BlockPos pos, MultiblockRole role) {
		public Member {
			pos = pos.immutable();
		}
	}
}
