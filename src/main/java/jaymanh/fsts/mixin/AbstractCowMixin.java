package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.item.ModItems;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;

@Mixin(AbstractCow.class)
public class AbstractCowMixin {
	@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
	private void fsts$bronzeMilk(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		ItemStack stack = player.getItemInHand(hand);
		AbstractCow cow = (AbstractCow) (Object) this;
		if (!stack.is(ModItems.BRONZE_BUCKET) || cow.isBaby()) {
			return;
		}

		player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
		player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, ModItems.BRONZE_MILK_BUCKET.getDefaultInstance()));
		cir.setReturnValue(InteractionResult.SUCCESS);
	}
}
