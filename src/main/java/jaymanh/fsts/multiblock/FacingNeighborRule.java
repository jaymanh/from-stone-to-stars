package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Finds blocks within {@code maxChebyshevDist} whose horizontal facing points toward the controller.
 * Requires at least {@code minCount} matches; returns all that qualify.
 */
public record FacingNeighborRule(
		String id,
		MultiblockRole role,
		boolean required,
		Predicate<BlockState> predicate,
		int minCount,
		int maxChebyshevDist,
		Component description
) implements ComponentRule {
	public static FacingNeighborRule ofBlock(
			String id,
			MultiblockRole role,
			Block block,
			int maxChebyshevDist,
			Component description
	) {
		return ofBlock(id, role, block, 1, maxChebyshevDist, description);
	}

	public static FacingNeighborRule ofBlock(
			String id,
			MultiblockRole role,
			Block block,
			int minCount,
			int maxChebyshevDist,
			Component description
	) {
		return new FacingNeighborRule(id, role, true, BlockPredicates.is(block), minCount, maxChebyshevDist, description);
	}

	public static FacingNeighborRule ofTag(
			String id,
			MultiblockRole role,
			TagKey<Block> tag,
			int minCount,
			int maxChebyshevDist,
			Component description
	) {
		return new FacingNeighborRule(id, role, true, BlockPredicates.is(tag), minCount, maxChebyshevDist, description);
	}

	@Override
	public Optional<ResolvedComponent> resolve(LevelAccessor level, BlockPos controller) {
		List<BlockPos> found = new ArrayList<>();
		int r = this.maxChebyshevDist;
		for (BlockPos pos : BlockPos.betweenClosed(
				controller.offset(-r, -r, -r),
				controller.offset(r, r, r)
		)) {
			if (pos.equals(controller)) {
				continue;
			}
			BlockState state = level.getBlockState(pos);
			if (!this.predicate.test(state) || !state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
				continue;
			}
			Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
			int dx = controller.getX() - pos.getX();
			int dy = controller.getY() - pos.getY();
			int dz = controller.getZ() - pos.getZ();
			if (chebyshev(dx, dy, dz) > this.maxChebyshevDist) {
				continue;
			}
			if (facesToward(pos, facing, controller)) {
				found.add(pos.immutable());
			}
		}
		if (found.size() < this.minCount) {
			return Optional.empty();
		}
		return Optional.of(new ResolvedComponent(found, this.role, this.id));
	}

	/**
	 * True when stepping along {@code facing} moves closer to the controller (Chebyshev).
	 */
	public static boolean facesToward(BlockPos from, Direction facing, BlockPos controller) {
		int here = chebyshev(
				controller.getX() - from.getX(),
				controller.getY() - from.getY(),
				controller.getZ() - from.getZ()
		);
		BlockPos ahead = from.relative(facing);
		int next = chebyshev(
				controller.getX() - ahead.getX(),
				controller.getY() - ahead.getY(),
				controller.getZ() - ahead.getZ()
		);
		return next < here;
	}

	private static int chebyshev(int dx, int dy, int dz) {
		return Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz)));
	}
}
