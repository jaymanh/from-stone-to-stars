package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.progression.VanillaProgression;

import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.LootContext;

@Mixin(VillagerTrade.class)
public class VillagerTradeMixin {
	@Inject(method = "getOffer", at = @At("RETURN"), cancellable = true)
	private void fsts$skipIronSales(LootContext lootContext, CallbackInfoReturnable<MerchantOffer> cir) {
		MerchantOffer offer = cir.getReturnValue();
		if (offer != null && VanillaProgression.isFreeIron(offer.getResult())) {
			cir.setReturnValue(null);
		}
	}
}
