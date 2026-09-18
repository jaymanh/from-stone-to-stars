package jaymanh.fsts.multiblock;

import java.util.function.Predicate;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockPredicates {
	private BlockPredicates() {
	}

	public static Predicate<BlockState> is(Block block) {
		return state -> state.is(block);
	}

	public static Predicate<BlockState> is(TagKey<Block> tag) {
		return state -> state.is(tag);
	}

	@SafeVarargs
	public static Predicate<BlockState> anyOf(Predicate<BlockState>... predicates) {
		return state -> {
			for (Predicate<BlockState> predicate : predicates) {
				if (predicate.test(state)) {
					return true;
				}
			}
			return false;
		};
	}
}
