package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import jaymanh.fsts.item.ModItems;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;

@Mixin(Axolotl.class)
public class AxolotlMixin {
	@Inject(method = "usePlayerItem", at = @At("HEAD"), cancellable = true)
	private void fsts$bronzeTropicalFish(Player player, InteractionHand hand, ItemStack stack, CallbackInfo ci) {
		if (!stack.is(ModItems.BRONZE_TROPICAL_FISH_BUCKET)) {
			return;
		}

		player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.BRONZE_WATER_BUCKET)));
		ci.cancel();
	}
}
