package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.progression.VanillaProgression;

import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffers;

/**
 * Villagers saved before this change still have iron sales stored on them.
 */
@Mixin(AbstractVillager.class)
public class AbstractVillagerMixin {
	@Inject(method = "getOffers", at = @At("RETURN"))
	private void fsts$dropSavedIronSales(CallbackInfoReturnable<MerchantOffers> cir) {
		MerchantOffers offers = cir.getReturnValue();
		if (offers != null) {
			offers.removeIf(offer -> VanillaProgression.isFreeIron(offer.getResult()));
		}
	}
}
