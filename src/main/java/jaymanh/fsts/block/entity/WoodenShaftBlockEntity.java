package jaymanh.fsts.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WoodenShaftBlockEntity extends AbstractRotationBlockEntity {
	public WoodenShaftBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.WOODEN_SHAFT, pos, state);
	}
}
