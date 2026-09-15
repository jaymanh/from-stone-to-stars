package jaymanh.fsts.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HammerstoneItem extends Item {
	public HammerstoneItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
		if (!level.isClientSide() && state.is(Blocks.GRAVEL)) {
			stack.hurtAndBreak(1, miner, EquipmentSlot.MAINHAND);
			return true;
		}

		return false;
	}
}
