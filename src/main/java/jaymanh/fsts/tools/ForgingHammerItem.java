package jaymanh.fsts.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class ForgingHammerItem extends Item {
	public ForgingHammerItem(Properties properties) {
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

	@Override
	public @Nullable ItemStackTemplate getCraftingRemainder(ItemStack stack) {
		if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
			return null;
		}

		ItemStack leftover = stack.copyWithCount(1);
		leftover.setDamageValue(leftover.getDamageValue() + 1);
		return ItemStackTemplate.fromNonEmptyStack(leftover);
	}
}
