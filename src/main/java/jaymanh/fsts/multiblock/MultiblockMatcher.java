package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public final class MultiblockMatcher {
	private static final Rotation[] HORIZONTAL = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};

	private MultiblockMatcher() {
	}

	/**
	 * Tries to form {@code definition} with {@code clicked} as the structure origin
	 * or as any pattern cell labeled {@link MultiblockRole#ORIGIN}.
	 */
	public static Optional<MultiblockMatch> tryMatch(LevelAccessor level, BlockPos clicked, MultiblockDefinition definition) {
		BlockState clickedState = level.getBlockState(clicked);
		List<BlockPos> startOffsets = originStartOffsets(definition);

		for (Rotation rotation : HORIZONTAL) {
			for (BlockPos startOffset : startOffsets) {
				if (!matchesStartCell(definition, startOffset, clickedState)) {
					continue;
				}

				BlockPos trueOrigin = clicked.subtract(rotate(startOffset, rotation));
				if (!definition.originPredicate().test(level.getBlockState(trueOrigin))) {
					continue;
				}

				Optional<MultiblockMatch> match = tryMatchRotation(level, trueOrigin, definition, rotation);
				if (match.isPresent()) {
					return match;
				}
			}
		}
		return Optional.empty();
	}

	public static boolean stillValid(LevelAccessor level, MultiblockMatch match) {
		return tryMatchRotation(level, match.origin(), match.definition(), match.rotation()).isPresent();
	}

	private static List<BlockPos> originStartOffsets(MultiblockDefinition definition) {
		List<BlockPos> offsets = new ArrayList<>();
		offsets.add(BlockPos.ZERO);
		for (var entry : definition.pattern().parts().entrySet()) {
			if (entry.getValue().role() == MultiblockRole.ORIGIN) {
				offsets.add(entry.getKey());
			}
		}
		return offsets;
	}

	private static boolean matchesStartCell(MultiblockDefinition definition, BlockPos startOffset, BlockState clickedState) {
		if (startOffset.equals(BlockPos.ZERO)) {
			return definition.originPredicate().test(clickedState);
		}
		MultiblockPattern.Part part = definition.pattern().parts().get(startOffset);
		return part != null && part.predicate().test(clickedState);
	}

	private static Optional<MultiblockMatch> tryMatchRotation(
			LevelAccessor level,
			BlockPos origin,
			MultiblockDefinition definition,
			Rotation rotation
	) {
		List<MultiblockMatch.Member> members = new ArrayList<>();
		for (var entry : definition.pattern().parts().entrySet()) {
			BlockPos absolute = origin.offset(rotate(entry.getKey(), rotation));
			BlockState state = level.getBlockState(absolute);
			if (!entry.getValue().predicate().test(state)) {
				return Optional.empty();
			}
			members.add(new MultiblockMatch.Member(absolute, entry.getValue().role()));
		}
		return Optional.of(new MultiblockMatch(definition, origin, rotation, members));
	}

	public static BlockPos rotate(BlockPos offset, Rotation rotation) {
		return switch (rotation) {
			case CLOCKWISE_90 -> new BlockPos(-offset.getZ(), offset.getY(), offset.getX());
			case CLOCKWISE_180 -> new BlockPos(-offset.getX(), offset.getY(), -offset.getZ());
			case COUNTERCLOCKWISE_90 -> new BlockPos(offset.getZ(), offset.getY(), -offset.getX());
			default -> offset;
		};
	}
}
