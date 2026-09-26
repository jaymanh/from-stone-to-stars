package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Requires at least {@code minCount} matching blocks inside an inclusive box relative to the controller.
 */
public record CountInVolumeRule(
		String id,
		MultiblockRole role,
		boolean required,
		Predicate<BlockState> predicate,
		int minCount,
		int minX,
		int minY,
		int minZ,
		int maxX,
		int maxY,
		int maxZ,
		Component description
) implements ComponentRule {
	public static CountInVolumeRule ofTag(
			String id,
			MultiblockRole role,
			TagKey<Block> tag,
			int minCount,
			int minX,
			int minY,
			int minZ,
			int maxX,
			int maxY,
			int maxZ,
			Component description
	) {
		return new CountInVolumeRule(
				id, role, true, BlockPredicates.is(tag), minCount,
				minX, minY, minZ, maxX, maxY, maxZ, description
		);
	}

	@Override
	public Optional<ResolvedComponent> resolve(LevelAccessor level, BlockPos controller) {
		List<BlockPos> found = new ArrayList<>();
		BlockPos min = controller.offset(this.minX, this.minY, this.minZ);
		BlockPos max = controller.offset(this.maxX, this.maxY, this.maxZ);
		for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
			if (this.predicate.test(level.getBlockState(pos))) {
				found.add(pos.immutable());
			}
		}
		if (found.size() < this.minCount) {
			return Optional.empty();
		}
		return Optional.of(new ResolvedComponent(found, this.role, this.id));
	}
}
