package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.tools.LeatherSkinItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeMixin {
	@Inject(
			method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
			at = @At("RETURN"),
			cancellable = true
	)
	private void fsts$rejectEmptyWaterSkin(CraftingInput input, Level level, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ()) {
			return;
		}

		for (int slot = 0; slot < input.size(); slot++) {
			ItemStack stack = input.getItem(slot);
			if (stack.getItem() instanceof LeatherSkinItem && !LeatherSkinItem.hasWater(stack)) {
				cir.setReturnValue(false);
				return;
			}
		}
	}
}
