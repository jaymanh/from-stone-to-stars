package jaymanh.fsts.multiblock.process;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.MultiblockProcess;
import jaymanh.fsts.multiblock.MultiblockRole;
import jaymanh.fsts.multiblock.StructureMatch;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock.LIT;

public final class CharcoalPileProcess implements MultiblockProcess, FormedMultiblock.ProgressAware, FormedMultiblock.DurationAware {
	public static final int DURATION_TICKS = 20 * 60;

	private int progress;

	public CharcoalPileProcess(StructureMatch match) {
	}

	@Override
	public int getProgress() {
		return this.progress;
	}

	@Override
	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public int getDurationTicks() {
		return DURATION_TICKS;
	}

	@Override
	public void onFormed(ServerLevel level, FormedMultiblock formed) {
		for (BlockPos pos : formed.match().positionsWithRole(MultiblockRole.ORIGIN)) {
			BlockState state = level.getBlockState(pos);
			level.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_ALL);
		}
	}

	@Override
	public boolean tick(ServerLevel level, FormedMultiblock formed) {
		this.progress++;

		if (this.progress % 20 == 0) {
			for (BlockPos origin : formed.match().positionsWithRole(MultiblockRole.ORIGIN)) {
				level.sendParticles(ParticleTypes.LARGE_SMOKE, origin.getX() + 0.5, origin.getY() + 1.0, origin.getZ() + 0.5, 3, 0.4, 0.2, 0.4, 0.0);
				level.sendParticles(ParticleTypes.FLAME, origin.getX() + 0.5, origin.getY() + 0.6, origin.getZ() + 0.5, 2, 0.25, 0.15, 0.25, 0.01);
			}
		}

		if (this.progress < DURATION_TICKS) {
			return true;
		}

		formed.deactivate();
		complete(level, formed);
		return false;
	}

	@Override
	public void onCancel(ServerLevel level, FormedMultiblock formed) {
		BlockPos controller = formed.match().origin();
		for (BlockPos pos : formed.match().positionsWithRole(MultiblockRole.ORIGIN)) {
			if (pos.equals(controller)) {
				continue;
			}
			BlockState state = level.getBlockState(pos);
			if (state.is(ModBlocks.KINDLING) && state.getValue(LIT)) {
				level.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_ALL);
			}
		}
	}

	private static void complete(ServerLevel level, FormedMultiblock formed) {
		for (BlockPos pos : formed.match().positionsWithRole(MultiblockRole.INPUT)) {
			level.setBlock(pos, ModBlocks.CHARCOAL_BLOCK.defaultBlockState(), 3);
		}

		BlockPos origin = formed.match().origin();
		level.playSound(null, origin, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.8F, 1.0F);

		for (BlockPos pos : formed.match().positionsWithRole(MultiblockRole.ORIGIN)) {
			if (pos.equals(origin)) {
				continue;
			}
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		}

		level.setBlock(origin, Blocks.AIR.defaultBlockState(), 3);
	}
}
