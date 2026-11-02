package jaymanh.fsts.advancement;

import jaymanh.fsts.FromStoneToStars;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;

/**
 * Grants Age advancements to players standing near the work.
 */
public final class AgeAdvancements {
	private static final int RANGE_SQR = 16 * 16;

	private AgeAdvancements() {
	}

	public static void initialize() {
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			if (player instanceof ServerPlayer serverPlayer && state.is(BlockTags.IRON_ORES)) {
				grant(serverPlayer, "age2/the_vein", "mined_iron_ore");
			}
		});
	}

	public static void grant(ServerPlayer player, String path, String criterion) {
		var holder = player.level().getServer().getAdvancements().get(FromStoneToStars.id(path));
		if (holder != null) {
			player.getAdvancements().award(holder, criterion);
		}
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
