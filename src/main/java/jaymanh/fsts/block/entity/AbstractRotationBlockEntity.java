package jaymanh.fsts.block.entity;

import jaymanh.fsts.rotation.RotationNetwork;
import jaymanh.fsts.rotation.RotationStatus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractRotationBlockEntity extends BlockEntity {
	private RotationStatus status = RotationStatus.DISCONNECTED;
	private boolean spinning;
	private boolean networkActive;
	private long lastNetworkUpdate = Long.MIN_VALUE;

	protected AbstractRotationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	/** True when a network update already reached this block during the given tick. */
	public boolean wasUpdatedAt(long gameTime) {
		return this.lastNetworkUpdate == gameTime;
	}

	public RotationStatus getRotationStatus() {
		return this.status;
	}

	public boolean isSpinning() {
		return this.spinning;
	}

	public boolean isNetworkActive() {
		return this.networkActive;
	}

	public void applyNetworkState(boolean spinning, RotationStatus status, boolean networkActive, long gameTime) {
		this.spinning = spinning;
		this.status = status;
		this.networkActive = networkActive;
		this.lastNetworkUpdate = gameTime;
		this.setChanged();
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		super.preRemoveSideEffects(pos, state);
		if (this.level != null && !this.level.isClientSide()) {
			for (Direction direction : Direction.values()) {
				RotationNetwork.update(this.level, pos.relative(direction));
			}
		}
	}
}
