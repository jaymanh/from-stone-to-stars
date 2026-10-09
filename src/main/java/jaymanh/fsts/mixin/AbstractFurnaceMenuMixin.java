package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.block.entity.BasicSmelterEntity;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.item.CrucibleItem;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;

@Mixin(AbstractFurnaceMenu.class)
public abstract class AbstractFurnaceMenuMixin {
	@Shadow
	@Final
	private Container container;

	@Inject(method = "canSmelt", at = @At("HEAD"), cancellable = true)
	private void fsts$allowChargedCrucible(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (CrucibleItem.isCharged(stack) && fsts$firesAlloys(this.container)) {
			cir.setReturnValue(true);
		}
	}

	private static boolean fsts$firesAlloys(Container container) {
		return container instanceof BasicSmelterEntity
				|| container instanceof BlastFurnaceBlockEntity
				|| container instanceof ForgeHearthBlockEntity;
	}
}
