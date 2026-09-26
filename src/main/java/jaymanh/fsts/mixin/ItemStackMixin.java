package jaymanh.fsts.mixin;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.component.ModDataComponents;
import jaymanh.fsts.item.HeatedMetalItem;

import net.minecraft.world.item.ItemStack;

/**
 * Heated metal stacks carry different {@code heat_until} values; treat them as stackable anyway.
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Inject(method = "isSameItemSameComponents", at = @At("HEAD"), cancellable = true)
	private static void fsts$ignoreHeatUntilForHeatedMetal(
			ItemStack left,
			ItemStack right,
			CallbackInfoReturnable<Boolean> cir
	) {
		if (!HeatedMetalItem.isSameHeatedMetal(left, right)) {
			return;
		}
		if (left.isEmpty() && right.isEmpty()) {
			cir.setReturnValue(true);
			return;
		}

		ItemStack leftCopy = left.copy();
		ItemStack rightCopy = right.copy();
		leftCopy.remove(ModDataComponents.HEAT_UNTIL);
		rightCopy.remove(ModDataComponents.HEAT_UNTIL);
		cir.setReturnValue(Objects.equals(leftCopy.getComponents(), rightCopy.getComponents()));
	}
}
