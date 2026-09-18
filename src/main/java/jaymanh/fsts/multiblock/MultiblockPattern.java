package jaymanh.fsts.multiblock;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Relative block layout keyed from the structure origin (0,0,0).
 * The origin cell itself is usually the igniter/controller and is matched separately.
 */
public final class MultiblockPattern {
	private final Map<BlockPos, Part> parts;

	private MultiblockPattern(Map<BlockPos, Part> parts) {
		this.parts = Collections.unmodifiableMap(parts);
	}

	public Map<BlockPos, Part> parts() {
		return this.parts;
	}

	public static Builder builder() {
		return new Builder();
	}

	public record Part(Predicate<BlockState> predicate, MultiblockRole role) {
	}

	public static final class Builder {
		private final Map<BlockPos, Part> parts = new LinkedHashMap<>();

		public Builder where(int x, int y, int z, Predicate<BlockState> predicate, MultiblockRole role) {
			BlockPos offset = new BlockPos(x, y, z);
			if (offset.equals(BlockPos.ZERO)) {
				throw new IllegalArgumentException("Origin (0,0,0) is implied; do not add it to the pattern");
			}
			this.parts.put(offset, new Part(predicate, role));
			return this;
		}

		public MultiblockPattern build() {
			if (this.parts.isEmpty()) {
				throw new IllegalStateException("Multiblock pattern needs at least one part");
			}
			return new MultiblockPattern(new LinkedHashMap<>(this.parts));
		}
	}
}
