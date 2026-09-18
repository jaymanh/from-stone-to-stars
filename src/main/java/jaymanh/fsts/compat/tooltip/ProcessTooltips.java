package jaymanh.fsts.compat.tooltip;

import java.util.ArrayList;
import java.util.List;

import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock.HideStage;
import jaymanh.fsts.block.entity.PlacedBoneSawBlockEntity;
import jaymanh.fsts.block.entity.TanningRackBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public final class ProcessTooltips {
	public static final String PROGRESS = "FstsProgress";
	public static final String DURATION = "FstsDuration";
	public static final String SAWING = "FstsSawing";
	public static final String STROKES = "FstsStrokes";

	private ProcessTooltips() {
	}

	public static void writeTanning(CompoundTag tag, TanningRackBlockEntity rack) {
		tag.putIntArray(PROGRESS, rack.copyProgress());
	}

	public static void writeKindling(CompoundTag tag, int progress, int duration) {
		tag.putInt(PROGRESS, progress);
		tag.putInt(DURATION, duration);
	}

	public static void writeBoneSaw(CompoundTag tag, PlacedBoneSawBlockEntity saw) {
		tag.putBoolean(SAWING, saw.isSawing());
		tag.putInt(STROKES, saw.getStrokes());
	}

	public static List<Component> tanningLines(BlockState state, CompoundTag data) {
		int[] progress = data.getIntArray(PROGRESS).orElse(new int[0]);
		List<Component> lines = new ArrayList<>();
		for (int slot = 0; slot < TanningRackBlock.SLOTS.length; slot++) {
			HideStage stage = state.getValue(TanningRackBlock.SLOTS[slot]);
			if (stage == HideStage.EMPTY) {
				continue;
			}
			int slotNumber = slot + 1;
			if (stage == HideStage.TANNED) {
				lines.add(Component.translatable("tooltip.from-stone-to-stars.tanning.ready", slotNumber));
				continue;
			}

			int current = slot < progress.length ? progress[slot] : 0;
			int remaining = TanningRackBlockEntity.TANNING_TICKS - current;
			lines.add(Component.translatable(
					"tooltip.from-stone-to-stars.tanning.slot",
					slotNumber,
					percent(current, TanningRackBlockEntity.TANNING_TICKS),
					formatTime(remaining)
			));
		}
		return lines;
	}

	public static List<Component> kindlingLines(boolean lit, CompoundTag data) {
		if (!lit) {
			return List.of();
		}

		int progress = data.getIntOr(PROGRESS, 0);
		int duration = data.getIntOr(DURATION, 0);
		if (duration <= 0 || progress <= 0) {
			return List.of(Component.translatable("tooltip.from-stone-to-stars.burning"));
		}

		List<Component> lines = new ArrayList<>();
		lines.add(Component.translatable("tooltip.from-stone-to-stars.progress", percent(progress, duration)));
		int remaining = duration - progress;
		if (remaining > 0) {
			lines.add(remainingTime(remaining));
		}
		return lines;
	}

	public static List<Component> boneSawLines(CompoundTag data) {
		if (!data.getBooleanOr(SAWING, false)) {
			return List.of();
		}

		int strokes = data.getIntOr(STROKES, 0);
		return List.of(Component.translatable(
				"tooltip.from-stone-to-stars.sawing",
				Math.min(strokes, PlacedBoneSawBlockEntity.STROKE_COUNT),
				PlacedBoneSawBlockEntity.STROKE_COUNT
		));
	}

	public static Component remainingTime(int remainingTicks) {
		return Component.translatable("tooltip.from-stone-to-stars.remaining", formatTime(remainingTicks));
	}

	public static Component duration(int ticks) {
		return formatTime(ticks);
	}

	public static Component formatTime(int ticks) {
		int seconds = Math.max(0, ticks) / 20;
		int minutes = seconds / 60;
		seconds %= 60;
		if (minutes > 0) {
			return Component.translatable("tooltip.from-stone-to-stars.time.min_sec", minutes, seconds);
		}
		return Component.translatable("tooltip.from-stone-to-stars.time.sec", seconds);
	}

	private static int percent(int current, int total) {
		if (total <= 0) {
			return 0;
		}
		return (int) Math.min(100L, Math.max(0L, current * 100L / total));
	}
}
