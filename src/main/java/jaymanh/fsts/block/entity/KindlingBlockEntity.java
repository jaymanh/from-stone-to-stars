package jaymanh.fsts.block.entity;

import java.util.Optional;

import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.MultiblockMatch;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class KindlingBlockEntity extends BlockEntity {
	private FormedMultiblock formed;

	public KindlingBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.KINDLING, pos, state);
	}

	public boolean isBurning() {
		return this.formed != null && this.formed.isActive();
	}

	public boolean start(MultiblockMatch match) {
		if (!(this.level instanceof ServerLevel serverLevel) || this.isBurning()) {
			return false;
		}
		this.formed = new FormedMultiblock(match, match.definition().processFactory().create(match));
		this.formed.onFormed(serverLevel);
		this.setChanged();
		return true;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, KindlingBlockEntity kindling) {
		if (!(level instanceof ServerLevel serverLevel) || kindling.formed == null) {
			return;
		}

		// Completion may remove this block and clear kindling.formed via preRemoveSideEffects.
		FormedMultiblock formed = kindling.formed;
		formed.tick(serverLevel);

		if (!formed.isActive()) {
			kindling.formed = null;
			if (!kindling.isRemoved()
					&& level.getBlockState(pos).is(state.getBlock())
					&& state.getValue(KindlingBlock.LIT)) {
				level.setBlock(pos, state.setValue(KindlingBlock.LIT, false), Block.UPDATE_ALL);
			}
			if (!kindling.isRemoved()) {
				kindling.setChanged();
			}
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (this.formed != null && this.formed.isActive()) {
			ValueOutput formedOut = output.child("Formed");
			this.formed.saveAdditional(formedOut);
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		Optional<ValueInput> formedIn = input.child("Formed");
		this.formed = formedIn.flatMap(FormedMultiblock::load).orElse(null);
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level instanceof ServerLevel serverLevel && this.formed != null) {
			this.formed.cancel(serverLevel);
			this.formed = null;
		}
		super.preRemoveSideEffects(pos, state);
	}
}
