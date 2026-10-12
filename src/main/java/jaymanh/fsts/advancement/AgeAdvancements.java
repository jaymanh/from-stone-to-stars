package jaymanh.fsts.advancement;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Grants Age advancements to players standing near the work.
 */
public final class AgeAdvancements {
	private static final int RANGE_SQR = 16 * 16;

	private AgeAdvancements() {
	}

	public static void grantNearby(ServerLevel level, BlockPos pos, String path, String criterion) {
		var holder = level.getServer().getAdvancements().get(FromStoneToStars.id(path));
		if (holder == null) {
			return;
		}
		for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
			if (player.level() != level) {
				continue;
			}
			if (player.blockPosition().distSqr(pos) <= RANGE_SQR) {
				player.getAdvancements().award(holder, criterion);
			}
		}
	}
}
