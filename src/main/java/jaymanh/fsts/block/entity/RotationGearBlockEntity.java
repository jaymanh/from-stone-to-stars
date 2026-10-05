package jaymanh.fsts.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RotationGearBlockEntity extends AbstractRotationBlockEntity {
	public RotationGearBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ROTATION_GEAR, pos, state);
	}
}
