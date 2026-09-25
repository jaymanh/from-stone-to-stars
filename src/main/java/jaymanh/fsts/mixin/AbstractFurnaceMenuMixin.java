package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.block.entity.BasicSmelterEntity;
import jaymanh.fsts.item.CrucibleItem;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.ItemStack;

@Mixin(AbstractFurnaceMenu.class)
public abstract class AbstractFurnaceMenuMixin {
	@Shadow
	@Final
	private Container container;

	@Inject(method = "canSmelt", at = @At("HEAD"), cancellable = true)
	private void fsts$allowChargedCrucible(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (this.container instanceof BasicSmelterEntity && CrucibleItem.isCharged(stack)) {
			cir.setReturnValue(true);
		}
	}
}
