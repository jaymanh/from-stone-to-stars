package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.ModCustomBlockModels.PlacedBoneSawBlock;
import jaymanh.fsts.tools.ModTools;
import jaymanh.fsts.util.LogPlanks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class PlacedBoneSawBlockEntity extends BlockEntity {
	public static final int STROKE_COUNT = 4;

	private int strokes;

	public PlacedBoneSawBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.PLACED_BONE_SAW, pos, state);
	}

	public boolean isSawing() {
		return this.strokes > 0;
	}

	public int getStrokes() {
		return this.strokes;
	}

	/**
	 * Performs one manual saw stroke. The player must interact once for every
	 * stroke; the saw no longer continues automatically after the first click.
	 */
	public boolean sawOnce() {
		if (this.level == null || this.level.isClientSide()) {
			return false;
		}

		Level level = this.level;
		BlockPos pos = this.worldPosition;
		BlockState state = this.getBlockState();
		this.strokes++;
		boolean nextOffset = !state.getValue(PlacedBoneSawBlock.OFFSET);
		level.setBlock(pos, state.setValue(PlacedBoneSawBlock.OFFSET, nextOffset), Block.UPDATE_ALL);
		level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 0.8F, 1.0F);

		if (this.strokes >= STROKE_COUNT) {
			this.finishSawing(level, pos);
		} else {
			this.setChanged();
		}
		return true;
	}

	private void finishSawing(Level level, BlockPos pos) {
		BlockPos logPos = pos.below();
		BlockState logState = level.getBlockState(logPos);
		ItemStack planks = LogPlanks.planksForLog(logState, 4);

		level.removeBlock(pos, false);
		Block.popResource(level, pos, new ItemStack(ModTools.BONE_SAW));

		if (!planks.isEmpty()) {
			level.removeBlock(logPos, false);
			Block.popResource(level, logPos, planks);
			level.playSound(null, logPos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("Strokes", this.strokes);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.strokes = Math.clamp(input.getIntOr("Strokes", 0), 0, STROKE_COUNT - 1);
	}
}
