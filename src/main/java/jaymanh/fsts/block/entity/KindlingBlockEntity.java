package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.multiblock.ActiveMultiblocks;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.MultiblockRole;
import jaymanh.fsts.multiblock.StructureMatch;

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
	/** False until the first post-load tick so we never touch other BEs during chunk deserialize. */
	private boolean participantsLinked;

	public KindlingBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.KINDLING, pos, state);
	}

	public boolean isBurning() {
		return this.formed != null && this.formed.isActive();
	}

	public int getProcessProgress() {
		return this.isBurning() ? this.formed.getProcessProgress() : 0;
	}

	public int getProcessDuration() {
		return this.formed != null ? this.formed.getProcessDuration() : 0;
	}

	public boolean start(StructureMatch match) {
		if (!(this.level instanceof ServerLevel serverLevel) || this.isBurning()) {
			return false;
		}
		FormedMultiblock formed = new FormedMultiblock(match, match.processFactory().create(match));
		linkParticipants(serverLevel, formed);
		ActiveMultiblocks.register(serverLevel, formed);
		this.participantsLinked = true;
		formed.onFormed(serverLevel);
		return true;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, KindlingBlockEntity kindling) {
		if (!(level instanceof ServerLevel serverLevel) || kindling.formed == null) {
			return;
		}

		FormedMultiblock formed = kindling.formed;
		// Only the structure origin owns ticking so satellite origins do not double-run the process.
		if (!pos.equals(formed.match().origin())) {
			return;
		}

		if (!kindling.participantsLinked) {
			linkParticipants(serverLevel, formed);
			ActiveMultiblocks.register(serverLevel, formed);
			kindling.participantsLinked = true;
		}

		formed.tick(serverLevel);

		if (!formed.isActive()) {
			unlinkParticipants(serverLevel, formed);
			kindling.participantsLinked = false;
			if (!kindling.isRemoved()
					&& level.getBlockState(pos).is(state.getBlock())
					&& state.getValue(KindlingBlock.LIT)) {
				level.setBlock(pos, state.setValue(KindlingBlock.LIT, false), Block.UPDATE_ALL);
			}
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		// Only the structure origin persists the process; satellites are re-linked on first tick.
		if (this.formed != null && this.formed.isActive() && this.worldPosition.equals(this.formed.match().origin())) {
			ValueOutput formedOut = output.child("Formed");
			this.formed.saveAdditional(formedOut);
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		// Do not clear an existing link when this BE has no Formed tag (satellites).
		input.child("Formed").flatMap(FormedMultiblock::load).ifPresent(loaded -> {
			this.formed = loaded;
			this.participantsLinked = false;
		});
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level instanceof ServerLevel serverLevel && this.formed != null) {
			FormedMultiblock formed = this.formed;
			if (formed.isActive()) {
				formed.cancel(serverLevel);
			}
			unlinkParticipants(serverLevel, formed);
			this.participantsLinked = false;
		}
		super.preRemoveSideEffects(pos, state);
	}

	private static void linkParticipants(ServerLevel level, FormedMultiblock formed) {
		for (BlockPos pos : formed.match().positionsWithRole(MultiblockRole.ORIGIN)) {
			if (level.getBlockEntity(pos) instanceof KindlingBlockEntity kindling) {
				kindling.formed = formed;
				kindling.participantsLinked = true;
			}
		}
	}

	private static void unlinkParticipants(ServerLevel level, FormedMultiblock formed) {
		ActiveMultiblocks.unregister(level, formed);
		for (BlockPos pos : formed.match().positionsWithRole(MultiblockRole.ORIGIN)) {
			if (level.getBlockEntity(pos) instanceof KindlingBlockEntity kindling && kindling.formed == formed) {
				kindling.formed = null;
				kindling.participantsLinked = false;
			}
		}
	}
}
