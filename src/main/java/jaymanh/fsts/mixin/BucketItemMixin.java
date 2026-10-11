package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.item.BronzeBucketBehaviors;
import jaymanh.fsts.item.ModItems;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;

@Mixin(BucketItem.class)
public class BucketItemMixin {
	@Inject(method = "getEmptySuccessItem", at = @At("HEAD"), cancellable = true)
	private static void fsts$emptyToBronze(ItemStack stack, Player player, CallbackInfoReturnable<ItemStack> cir) {
		if (player.hasInfiniteMaterials()) {
			return;
		}
		if (BronzeBucketBehaviors.emptiesToBronze(stack.getItem())) {
			cir.setReturnValue(new ItemStack(ModItems.BRONZE_BUCKET));
		}
	}

	@Redirect(
			method = "use",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemUtils;createFilledResult(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
			)
	)
	private ItemStack fsts$remapFilledResult(ItemStack hand, Player player, ItemStack result) {
		return ItemUtils.createFilledResult(hand, player, BronzeBucketBehaviors.remapContents(hand, result));
	}
}
