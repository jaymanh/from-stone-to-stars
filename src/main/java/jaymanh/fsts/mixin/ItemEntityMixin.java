package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import jaymanh.fsts.item.HeatedMetalItem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	@Shadow
	public abstract ItemStack getItem();

	@Inject(method = "tick", at = @At("TAIL"))
	private void fsts$tickHeatedMetal(CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (!(self.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		ItemStack stack = this.getItem();
		if (HeatedMetalItem.isHeated(stack)) {
			HeatedMetalItem.tickCooling(stack, serverLevel, self);
		}
	}
}
