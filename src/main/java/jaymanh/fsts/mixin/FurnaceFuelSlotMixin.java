package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.item.ModItems;

import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.ItemStack;

@Mixin(FurnaceFuelSlot.class)
public class FurnaceFuelSlotMixin {
	@Inject(method = "isBucket", at = @At("HEAD"), cancellable = true)
	private static void fsts$bronzeBucket(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(ModItems.BRONZE_BUCKET)) {
			cir.setReturnValue(true);
		}
	}
}
