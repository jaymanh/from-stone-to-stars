package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import jaymanh.fsts.inventory.WorkStumpMenu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

/**
 * Wear the work stump when a craft result is taken from its menu (anvil-style).
 */
@Mixin(ResultSlot.class)
public class ResultSlotMixin {
	@Inject(method = "onTake", at = @At("TAIL"))
	private void fsts$damageWorkStump(Player player, ItemStack stack, CallbackInfo ci) {
		if (player.containerMenu instanceof WorkStumpMenu menu) {
			menu.onCraftTaken(player);
		}
	}
}
