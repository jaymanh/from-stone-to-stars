package jaymanh.fsts.multiblock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

/**
 * Maps every block position that belongs to a running multiblock to that instance,
 * so HUD overlays can show progress when looking at any member block.
 */
public final class ActiveMultiblocks {
	private static final Map<ServerLevel, Map<BlockPos, FormedMultiblock>> BY_LEVEL = new HashMap<>();

	private ActiveMultiblocks() {
	}

	public static void register(ServerLevel level, FormedMultiblock formed) {
		Map<BlockPos, FormedMultiblock> byPos = BY_LEVEL.computeIfAbsent(level, ignored -> new HashMap<>());
		for (BlockPos pos : formed.match().allPositions()) {
			byPos.put(pos.immutable(), formed);
		}
	}

	public static void unregister(ServerLevel level, FormedMultiblock formed) {
		Map<BlockPos, FormedMultiblock> byPos = BY_LEVEL.get(level);
		if (byPos == null) {
			return;
		}
		for (BlockPos pos : formed.match().allPositions()) {
			if (byPos.get(pos) == formed) {
				byPos.remove(pos);
			}
		}
		if (byPos.isEmpty()) {
			BY_LEVEL.remove(level);
		}
	}

	public static Optional<FormedMultiblock> get(Level level, BlockPos pos) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return Optional.empty();
		}
		Map<BlockPos, FormedMultiblock> byPos = BY_LEVEL.get(serverLevel);
		if (byPos == null) {
			return Optional.empty();
		}
		FormedMultiblock formed = byPos.get(pos);
		if (formed == null || !formed.isActive()) {
			return Optional.empty();
		}
		return Optional.of(formed);
	}
}
