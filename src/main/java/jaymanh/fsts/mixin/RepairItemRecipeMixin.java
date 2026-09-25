package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.item.CrucibleItem;
import jaymanh.fsts.tools.HammerstoneItem;
import jaymanh.fsts.tools.LeatherSkinItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RepairItemRecipe;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {
	@Inject(method = "canCombine", at = @At("HEAD"), cancellable = true)
	private static void fsts$blockSelfRepair(ItemStack first, ItemStack second, CallbackInfoReturnable<Boolean> cir) {
		Item item = first.getItem();
		if (item instanceof LeatherSkinItem || item instanceof HammerstoneItem || item instanceof CrucibleItem) {
			cir.setReturnValue(false);
		}
	}
}
