package jaymanh.fsts.shelter;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

/**
 * Beds need a roof: rain above either bed half blocks sleep and wakes sleepers.
 * Rain is checked at {@code pos.above()} — checking the bed block itself fails because
 * the bed is motion-blocking and {@link Level#isRainingAt} treats that as sheltered.
 */
public final class BedRainSleep {
	private static final Player.BedSleepingProblem RAINED_ON = new Player.BedSleepingProblem(
			Component.translatable("block.from-stone-to-stars.bed.rained_on")
	);

	private BedRainSleep() {
	}

	public static void initialize() {
		EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
			if (isBedRainedOn(player.level(), sleepingPos)) {
				return RAINED_ON;
			}
			return null;
		});

		ServerTickEvents.END_LEVEL_TICK.register(level -> {
			for (ServerPlayer player : level.players()) {
				if (!player.isSleeping()) {
					continue;
				}
				player.getSleepingPos().ifPresent(pos -> {
					if (isBedRainedOn(level, pos)) {
						player.stopSleepInBed(false, true);
						player.sendOverlayMessage(RAINED_ON.message());
					}
				});
			}
		});
	}

	public static boolean isBedRainedOn(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (!(state.getBlock() instanceof BedBlock)) {
			return level.isRainingAt(pos.above());
		}

		if (level.isRainingAt(pos.above())) {
			return true;
		}

		Direction facing = state.getValue(BedBlock.FACING);
		BedPart part = state.getValue(BedBlock.PART);
		BlockPos other = pos.relative(part == BedPart.HEAD ? facing.getOpposite() : facing);
		return level.getBlockState(other).getBlock() instanceof BedBlock
				&& level.isRainingAt(other.above());
	}
}
