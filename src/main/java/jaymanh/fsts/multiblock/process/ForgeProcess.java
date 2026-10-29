package jaymanh.fsts.multiblock.process;

import java.util.ArrayList;
import java.util.List;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.ModCustomBlockModels.BellowsBlock;
import jaymanh.fsts.block.ModCustomBlockModels.ForgeHearthBlock;
import jaymanh.fsts.block.entity.PoweredBellowsBlockEntity;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.ForgeLayout;
import jaymanh.fsts.multiblock.MultiblockProcess;
import jaymanh.fsts.multiblock.MultiblockRole;
import jaymanh.fsts.multiblock.StructureMatch;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Forge heat that powers blasting smelting on the hearth cluster.
 * Burn time scales with coal (5 minutes for the minimum pad).
 * Hearth count speeds smelting, and each steel-lined hearth counts twice; bellows pumps refill toward the coal max.
 * Spinning powered bellows climb the burn to the coal max and hold it there.
 */
public final class ForgeProcess implements MultiblockProcess, FormedMultiblock.ProgressAware, FormedMultiblock.DurationAware {
	/** Minimum coal blocks for burn scaling. */
	public static final int MIN_COAL_BLOCKS = ForgeLayout.MIN_COAL * ForgeLayout.MIN_COAL;
	/** Total burn for {@link #MIN_COAL_BLOCKS}: 5 minutes. */
	public static final int BURN_TICKS_FOR_MIN_COAL = 20 * 60 * 5;
	/** Burn time granted per coal/charcoal block when the forge is lit. */
	public static final int TICKS_PER_COAL = BURN_TICKS_FOR_MIN_COAL / MIN_COAL_BLOCKS;
	/** Extra burn time per bellows when the player pumps (capped by coal max). */
	public static final int TICKS_PER_BELLOWS_PUMP = 20 * 8;
	/**
	 * Burn added each tick by each spinning powered bellows. The fire uses 1 a tick,
	 * so one powered bellows climbs at a net 1 a tick until it reaches the coal max.
	 */
	public static final int POWERED_BELLOWS_REFILL_PER_TICK = 2;
	/** How often the forge rechecks its fittings: bellows, lined hearths, inputs, and spouts. */
	private static final int FITTING_SCAN_INTERVAL = 10;

	private final int maxBurnTicks;
	private final int hearthCount;
	private int burnTicksRemaining;
	private int burnTicksTotal;
	private int bankBellows;
	private int poweredBellows;
	private int drivingBellows;
	private int crushers;
	private int linedHearths;
	private List<BlockPos> inputs = List.of();
	private List<BlockPos> spouts = List.of();

	public ForgeProcess(StructureMatch match) {
		int coal = Math.max(MIN_COAL_BLOCKS, match.positionsWithRole(MultiblockRole.CONSUMED).size());
		this.maxBurnTicks = coal * TICKS_PER_COAL;
		this.burnTicksTotal = this.maxBurnTicks;
		this.burnTicksRemaining = this.burnTicksTotal;
		this.hearthCount = Math.max(1, match.positionsWithRole(MultiblockRole.CONTROLLER).size());
	}

	public int hearthCount() {
		return this.hearthCount;
	}

	/** Lined hearths at the last scan. */
	public int linedHearths() {
		return this.linedHearths;
	}

	/** Cook steps per interval: one per hearth, plus one more for each lined hearth. */
	public int cookSpeed() {
		return this.hearthCount + this.linedHearths;
	}

	/** Forge inputs at the last scan, for the master hearth to draw from. */
	public List<BlockPos> inputs() {
		return this.inputs;
	}

	/** Tapping spouts at the last scan, for the master hearth to fill. */
	public List<BlockPos> spouts() {
		return this.spouts;
	}

	public void restoreBurn(int totalTicks, int elapsedProgress) {
		this.burnTicksTotal = Mth.clamp(totalTicks, TICKS_PER_COAL, this.maxBurnTicks);
		this.burnTicksRemaining = Mth.clamp(this.burnTicksTotal - elapsedProgress, 0, this.maxBurnTicks);
	}

	/**
	 * @return ticks actually added
	 */
	public int pumpBellows() {
		int bellows = Math.max(1, this.bankBellows);
		int add = bellows * TICKS_PER_BELLOWS_PUMP;
		int before = this.burnTicksRemaining;
		this.burnTicksRemaining = Mth.clamp(this.burnTicksRemaining + add, 0, this.maxBurnTicks);
		this.burnTicksTotal = this.maxBurnTicks;
		return this.burnTicksRemaining - before;
	}

	@Override
	public int getProgress() {
		return Math.max(0, this.maxBurnTicks - this.burnTicksRemaining);
	}

	@Override
	public void setProgress(int progress) {
		this.burnTicksRemaining = Mth.clamp(this.maxBurnTicks - progress, 0, this.maxBurnTicks);
		this.burnTicksTotal = this.maxBurnTicks;
	}

	@Override
	public int getDurationTicks() {
		return this.maxBurnTicks;
	}

	public int getBurnTicksRemaining() {
		return this.burnTicksRemaining;
	}

	/** Powered bellows in the bank that were spinning at the last scan. */
	public int drivingBellows() {
		return this.drivingBellows;
	}

	/** Powered bellows in the bank at the last scan, spinning or not. */
	public int poweredBellows() {
		return this.poweredBellows;
	}

	/** Ore crushers sitting on this forge's inputs at the last scan. */
	public int crushers() {
		return this.crushers;
	}

	/** Every fitting present: a powered bellows, an input with a crusher on it, a spout, and every hearth lined. */
	public boolean isIronworks() {
		return this.poweredBellows > 0
				&& !this.inputs.isEmpty()
				&& this.crushers > 0
				&& !this.spouts.isEmpty()
				&& this.linedHearths >= this.hearthCount;
	}

	private void scanInputRole(ServerLevel level, StructureMatch match) {
		int bellows = 0;
		int powered = 0;
		int driving = 0;
		int crushers = 0;
		List<BlockPos> inputs = new ArrayList<>();
		for (BlockPos pos : match.positionsWithRole(MultiblockRole.INPUT)) {
			BlockState state = level.getBlockState(pos);
			if (state.is(ModBlocks.FORGE_INPUT)) {
				inputs.add(pos);
				if (level.getBlockState(pos.above()).is(ModBlocks.ORE_CRUSHER)) {
					crushers++;
				}
				continue;
			}
			if (BellowsBlock.isBellows(state)) {
				bellows++;
			}
			if (level.getBlockEntity(pos) instanceof PoweredBellowsBlockEntity bellowsEntity) {
				powered++;
				if (bellowsEntity.isDriving()) {
					driving++;
				}
			}
		}
		this.bankBellows = bellows;
		this.poweredBellows = powered;
		this.drivingBellows = driving;
		this.crushers = crushers;
		this.inputs = List.copyOf(inputs);
	}

	private static List<BlockPos> findSpouts(ServerLevel level, StructureMatch match) {
		List<BlockPos> spouts = new ArrayList<>();
		for (BlockPos pos : match.positionsWithRole(MultiblockRole.OUTPUT)) {
			if (level.getBlockState(pos).is(ModBlocks.TAPPING_SPOUT)) {
				spouts.add(pos);
			}
		}
		return List.copyOf(spouts);
	}

	private static int countLinedHearths(ServerLevel level, StructureMatch match) {
		int lined = 0;
		for (BlockPos pos : match.positionsWithRole(MultiblockRole.CONTROLLER)) {
			BlockState state = level.getBlockState(pos);
			if (state.is(ModBlocks.FORGE_HEARTH) && state.getValue(ForgeHearthBlock.LINED)) {
				lined++;
			}
		}
		return lined;
	}

	private void scanFittings(ServerLevel level, StructureMatch match) {
		this.scanInputRole(level, match);
		this.spouts = findSpouts(level, match);
		this.linedHearths = countLinedHearths(level, match);
	}

	@Override
	public void onFormed(ServerLevel level, FormedMultiblock formed) {
		setAllHearthsLit(level, formed, true);
		this.scanFittings(level, formed.match());
	}

	@Override
	public boolean tick(ServerLevel level, FormedMultiblock formed) {
		if (level.getGameTime() % FITTING_SCAN_INTERVAL == 0L) {
			this.scanFittings(level, formed.match());
		}
		int refill = this.drivingBellows * POWERED_BELLOWS_REFILL_PER_TICK;
		this.burnTicksRemaining = Math.min(this.maxBurnTicks, this.burnTicksRemaining - 1 + refill);
		if (level.getGameTime() % 20 == 0L) {
			BlockPos origin = formed.match().origin();
			level.sendParticles(ParticleTypes.FLAME, origin.getX() + 0.5, origin.getY() + 0.6, origin.getZ() + 0.5, 2, 0.2, 0.1, 0.2, 0.01);
			level.sendParticles(ParticleTypes.LARGE_SMOKE, origin.getX() + 0.5, origin.getY() + 1.0, origin.getZ() + 0.5, 2, 0.25, 0.15, 0.25, 0.0);
			for (BlockPos fuel : formed.match().positionsWithRole(MultiblockRole.CONSUMED)) {
				if (level.getRandom().nextInt(3) == 0) {
					level.sendParticles(ParticleTypes.SMOKE, fuel.getX() + 0.5, fuel.getY() + 1.0, fuel.getZ() + 0.5, 1, 0.1, 0.05, 0.1, 0.0);
				}
			}
		}
		if (this.burnTicksRemaining > 0) {
			return true;
		}
		formed.deactivate();
		complete(level, formed);
		return false;
	}

	@Override
	public void onCancel(ServerLevel level, FormedMultiblock formed) {
		setAllHearthsLit(level, formed, false);
	}

	private static void complete(ServerLevel level, FormedMultiblock formed) {
		setAllHearthsLit(level, formed, false);
		level.playSound(null, formed.match().origin(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.7F, 1.0F);
	}

	public static void setAllHearthsLit(ServerLevel level, FormedMultiblock formed, boolean lit) {
		for (BlockPos pos : formed.match().positionsWithRole(MultiblockRole.CONTROLLER)) {
			BlockState state = level.getBlockState(pos);
			if (state.is(ModBlocks.FORGE_HEARTH) && state.hasProperty(ForgeHearthBlock.LIT) && state.getValue(ForgeHearthBlock.LIT) != lit) {
				level.setBlock(pos, state.setValue(ForgeHearthBlock.LIT, lit), Block.UPDATE_ALL);
			}
		}
	}
}
