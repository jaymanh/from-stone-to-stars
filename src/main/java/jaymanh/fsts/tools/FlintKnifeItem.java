package jaymanh.fsts.tools;

import org.jspecify.annotations.Nullable;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class FlintKnifeItem extends Item {
	public FlintKnifeItem(Properties properties) {
		super(properties);
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
