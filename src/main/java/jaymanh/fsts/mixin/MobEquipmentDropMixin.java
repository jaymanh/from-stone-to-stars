package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import jaymanh.fsts.progression.VanillaProgression;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

/**
 * Spawned iron gear (zombie swords, vindicator axes, chain and iron armor) is not a mined drop.
 * Gear a mob picked up is preserved and still drops.
 */
@Mixin(Mob.class)
public abstract class MobEquipmentDropMixin {
	@Inject(method = "dropCustomDeathLoot", at = @At("HEAD"))
	private void fsts$voidSpawnedIron(ServerLevel level, DamageSource source, boolean killedByPlayer, CallbackInfo ci) {
		Mob self = (Mob) (Object) this;
		for (EquipmentSlot slot : EquipmentSlot.VALUES) {
			if (self.getDropChances().isPreserved(slot)) {
				continue;
			}
			ItemStack stack = self.getItemBySlot(slot);
			if (VanillaProgression.isFreeIron(stack)) {
				self.setItemSlot(slot, ItemStack.EMPTY);
			}
		}
	}
}
