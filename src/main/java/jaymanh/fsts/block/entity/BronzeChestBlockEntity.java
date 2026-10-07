package jaymanh.fsts.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BronzeChestBlockEntity extends ChestBlockEntity {
	public BronzeChestBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.BRONZE_CHEST, pos, state);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.from-stone-to-stars.bronze_chest");
	}
}
