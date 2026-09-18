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
	public static final int COOLDOWN_TICKS = 10; // 0.5 seconds

	private boolean sawing;
	private int strokes;
	private int cooldown;

	public PlacedBoneSawBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.PLACED_BONE_SAW, pos, state);
	}

	public boolean isSawing() {
		return this.sawing;
	}

	public int getStrokes() {
		return this.strokes;
	}

	public boolean tryStartSawing() {
		if (this.sawing || this.level == null || this.level.isClientSide()) {
			return false;
		}
		this.sawing = true;
		this.strokes = 0;
		this.cooldown = 0;
		this.setChanged();
		return true;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, PlacedBoneSawBlockEntity saw) {
		if (!saw.sawing) {
			return;
		}

		if (saw.cooldown > 0) {
			saw.cooldown--;
			return;
		}

		saw.strokes++;
		boolean nextOffset = !state.getValue(PlacedBoneSawBlock.OFFSET);
		level.setBlock(pos, state.setValue(PlacedBoneSawBlock.OFFSET, nextOffset), Block.UPDATE_ALL);
		level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 0.8F, 1.0F);

		if (saw.strokes >= STROKE_COUNT) {
			saw.finishSawing(level, pos);
			return;
		}

		saw.cooldown = COOLDOWN_TICKS;
		saw.setChanged();
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
		output.putBoolean("Sawing", this.sawing);
		output.putInt("Strokes", this.strokes);
		output.putInt("Cooldown", this.cooldown);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.sawing = input.getBooleanOr("Sawing", false);
		this.strokes = input.getIntOr("Strokes", 0);
		this.cooldown = input.getIntOr("Cooldown", 0);
	}
}
