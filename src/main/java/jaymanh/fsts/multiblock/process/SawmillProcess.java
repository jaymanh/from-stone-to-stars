package jaymanh.fsts.multiblock.process;

import jaymanh.fsts.block.entity.SawArborBlockEntity;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.MultiblockProcess;
import jaymanh.fsts.multiblock.SawmillLayout;
import jaymanh.fsts.multiblock.StructureMatch;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;

/**
 * Runs for as long as the sawmill stays built. While the arbor turns, every lane
 * cuts one log per cycle. Taller blade walls shorten the cycle slightly.
 */
public final class SawmillProcess implements MultiblockProcess, FormedMultiblock.ProgressAware, FormedMultiblock.DurationAware {
	/** Cycle length for the minimum blade height. */
	public static final int BASE_CUT_TICKS = 40;
	/** Ticks shaved off the cycle per blade row above the minimum. */
	public static final int TICKS_SAVED_PER_EXTRA_ROW = 6;
	/** How often logs lying on the infeed rollers are pulled in. */
	private static final int PICKUP_INTERVAL = 10;

	private int progress;
	private int duration = BASE_CUT_TICKS;
	private int pickupTimer;

	public SawmillProcess(StructureMatch match) {
	}

	public static int cutTicks(int bladeHeight) {
		int extraRows = Math.max(0, bladeHeight - SawmillLayout.MIN_BLADE_HEIGHT);
		return Math.max(1, BASE_CUT_TICKS - extraRows * TICKS_SAVED_PER_EXTRA_ROW);
	}

	@Override
	public void onFormed(ServerLevel level, FormedMultiblock formed) {
		this.progress = 0;
	}

	@Override
	public boolean tick(ServerLevel level, FormedMultiblock formed) {
		if (!(level.getBlockEntity(formed.match().origin()) instanceof SawArborBlockEntity arbor)) {
			return true;
		}
		SawmillLayout layout = arbor.layout();
		if (layout == null) {
			return true;
		}

		if (++this.pickupTimer >= PICKUP_INTERVAL) {
			this.pickupTimer = 0;
			arbor.pullInDroppedLogs(layout);
		}

		this.duration = cutTicks(layout.bladeHeight());
		if (!arbor.isSpinning() || !arbor.isNetworkActive() || !arbor.canCut()) {
			return true;
		}

		if (++this.progress >= this.duration) {
			this.progress = 0;
			arbor.cut(level, layout);
		}
		return true;
	}

	@Override
	public void onCancel(ServerLevel level, FormedMultiblock formed) {
		this.progress = 0;
	}

	@Override
	public int getProgress() {
		return this.progress;
	}

	@Override
	public void setProgress(int progress) {
		this.progress = Mth.clamp(progress, 0, BASE_CUT_TICKS - 1);
	}

	@Override
	public int getDurationTicks() {
		return this.duration;
	}
}
