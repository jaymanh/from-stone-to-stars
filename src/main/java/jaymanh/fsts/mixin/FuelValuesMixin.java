package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.item.ModItems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FuelValues;

@Mixin(FuelValues.class)
public class FuelValuesMixin {
	/** Same burn time as a lava bucket: 100 times the 200-tick fuel unit. */
	private static final int BRONZE_LAVA_BURN = 20000;

	@Inject(method = "isFuel", at = @At("HEAD"), cancellable = true)
	private void fsts$bronzeLava(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(ModItems.BRONZE_LAVA_BUCKET)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "burnDuration", at = @At("HEAD"), cancellable = true)
	private void fsts$bronzeLavaDuration(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (stack.is(ModItems.BRONZE_LAVA_BUCKET)) {
			cir.setReturnValue(BRONZE_LAVA_BURN);
		}
	}
}
