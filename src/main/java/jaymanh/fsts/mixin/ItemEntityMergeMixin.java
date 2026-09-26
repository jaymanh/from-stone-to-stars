package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.item.HeatedMetalItem;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMergeMixin {
	@Inject(method = "merge(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"))
	private static void fsts$keepEarlierHeatOnMerge(
			ItemStack destination,
			ItemStack source,
			int maxCount,
			CallbackInfoReturnable<ItemStack> cir
	) {
		ItemStack result = cir.getReturnValue();
		if (result != null && HeatedMetalItem.isHeated(result)) {
			HeatedMetalItem.keepEarlierHeat(result, destination);
			HeatedMetalItem.keepEarlierHeat(result, source);
		}
	}
}
