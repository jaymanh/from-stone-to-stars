package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LevelAccessor;

/**
 * Common handle for exact pattern matches and fuzzy assembly matches.
 * Processes and {@link FormedMultiblock} only need roles and validity.
 */
public interface StructureMatch {
	Identifier id();

	BlockPos origin();

	List<MultiblockMatch.Member> members();

	MultiblockProcess.Factory processFactory();

	boolean stillValid(LevelAccessor level);

	default List<BlockPos> positionsWithRole(MultiblockRole role) {
		List<BlockPos> positions = new ArrayList<>();
		if (role == MultiblockRole.ORIGIN || role == MultiblockRole.CONTROLLER) {
			positions.add(this.origin());
		}
		for (MultiblockMatch.Member member : this.members()) {
			if (member.role() == role) {
				positions.add(member.pos());
			}
		}
		return positions;
	}

	default List<BlockPos> allPositions() {
		List<BlockPos> positions = new ArrayList<>(this.members().size() + 1);
		positions.add(this.origin());
		for (MultiblockMatch.Member member : this.members()) {
			positions.add(member.pos());
		}
		return Collections.unmodifiableList(positions);
	}
}
