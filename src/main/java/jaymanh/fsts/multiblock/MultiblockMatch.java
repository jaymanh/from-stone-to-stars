package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;

public final class MultiblockMatch {
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

	public BlockPos origin() {
		return this.origin;
	}

	public Rotation rotation() {
		return this.rotation;
	}

	public List<Member> members() {
		return this.members;
	}

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

	public List<BlockPos> allPositions() {
		List<BlockPos> positions = new ArrayList<>(this.members.size() + 1);
		positions.add(this.origin);
		for (Member member : this.members) {
			positions.add(member.pos());
		}
		return positions;
	}

	public record Member(BlockPos pos, MultiblockRole role) {
		public Member {
			pos = pos.immutable();
		}
	}
}
