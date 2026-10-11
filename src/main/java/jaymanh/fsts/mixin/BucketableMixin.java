package jaymanh.fsts.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.item.BronzeBucketBehaviors;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(Bucketable.class)
public interface BucketableMixin {
	@Inject(method = "bucketMobPickup", at = @At("HEAD"), cancellable = true)
	private static void fsts$bronzeMobBucket(
			Player player,
			InteractionHand hand,
			LivingEntity entity,
			CallbackInfoReturnable<Optional<InteractionResult>> cir
	) {
		Optional<InteractionResult> result = BronzeBucketBehaviors.tryBucketMob(player, hand, entity);
		if (result != null) {
			cir.setReturnValue(result);
		}
	}
}
