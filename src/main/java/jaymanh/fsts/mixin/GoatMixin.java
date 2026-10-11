package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.item.ModItems;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;

@Mixin(Goat.class)
public abstract class GoatMixin {
	@Shadow
	protected abstract SoundEvent getMilkingSound();

	@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
	private void fsts$bronzeMilk(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		ItemStack stack = player.getItemInHand(hand);
		Goat goat = (Goat) (Object) this;
		if (!stack.is(ModItems.BRONZE_BUCKET) || goat.isBaby()) {
			return;
		}

		player.playSound(this.getMilkingSound(), 1.0F, 1.0F);
		player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, ModItems.BRONZE_MILK_BUCKET.getDefaultInstance()));
		cir.setReturnValue(InteractionResult.SUCCESS);
	}
}
