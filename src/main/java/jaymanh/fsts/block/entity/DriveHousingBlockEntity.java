package jaymanh.fsts.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The bore rig's drive. Takes the wheel's shaft and its one unit of capacity; the drill head reads {@link #isDriving()}.
 */
public class DriveHousingBlockEntity extends AbstractRotationBlockEntity {
	public DriveHousingBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.DRIVE_HOUSING, pos, state);
	}

	public boolean isDriving() {
		return this.isSpinning() && this.isNetworkActive();
	}
}
