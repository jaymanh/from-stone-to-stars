package jaymanh.fsts.item;

import java.util.function.Consumer;

import org.jspecify.annotations.Nullable;

import jaymanh.fsts.component.CrucibleCharge;
import jaymanh.fsts.component.ModDataComponents;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class CrucibleItem extends Item {
	public static final int MAX_USES = 8;

	public CrucibleItem(Properties properties) {
		super(properties);
	}

	public static boolean isCrucible(ItemStack stack) {
		return stack.getItem() instanceof CrucibleItem;
	}

	public static boolean isEmpty(ItemStack stack) {
		return isCrucible(stack) && !stack.has(ModDataComponents.CRUCIBLE_CHARGE);
	}

	public static boolean isCharged(ItemStack stack) {
		return isCrucible(stack) && stack.has(ModDataComponents.CRUCIBLE_CHARGE);
	}

	public static @Nullable CrucibleCharge getCharge(ItemStack stack) {
		return stack.get(ModDataComponents.CRUCIBLE_CHARGE);
	}

	public static ItemStack createCharged(ItemStack emptyCrucible, CrucibleCharge charge) {
		ItemStack charged = emptyCrucible.copyWithCount(1);
		charged.set(ModDataComponents.CRUCIBLE_CHARGE, charge);
		return charged;
	}

	/**
	 * Clears the charge and applies one use of wear. Returns empty if the crucible breaks.
	 */
	public static ItemStack finishFiring(ItemStack charged) {
		ItemStack leftover = charged.copyWithCount(1);
		leftover.remove(ModDataComponents.CRUCIBLE_CHARGE);
		leftover.setDamageValue(leftover.getDamageValue() + 1);
		if (leftover.getDamageValue() >= leftover.getMaxDamage()) {
			return ItemStack.EMPTY;
		}
		return leftover;
	}

	@Override
	public void appendHoverText(
			ItemStack stack,
			TooltipContext context,
			TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder,
			TooltipFlag flag
	) {
		CrucibleCharge charge = getCharge(stack);
		if (charge != null) {
			ItemStack result = charge.createResult();
			tooltipAdder.accept(Component.translatable(
					"item.from-stone-to-stars.crucible.charged",
					result.getCount(),
					result.getHoverName()
			));
		} else {
			tooltipAdder.accept(Component.translatable("item.from-stone-to-stars.crucible.empty"));
		}
	}
}
