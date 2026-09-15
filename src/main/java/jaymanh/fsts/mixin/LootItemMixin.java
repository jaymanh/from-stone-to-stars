package jaymanh.fsts.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import jaymanh.fsts.item.ModItems;
import jaymanh.fsts.progression.VanillaProgression;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;

@Mixin(LootItem.class)
public class LootItemMixin {
	@Shadow
	@Final
	private Holder<Item> item;

	@Inject(method = "createItemStack", at = @At("HEAD"), cancellable = true)
	private void fsts$replaceGatedDrops(Consumer<ItemStack> output, LootContext context, CallbackInfo ci) {
		Item dropped = this.item.value();
		if (dropped == Items.LEATHER) {
			output.accept(new ItemStack(ModItems.RAW_LEATHER));
			ci.cancel();
			return;
		}
		if (VanillaProgression.isRemovedToolDrop(dropped)) {
			ci.cancel();
		}
	}
}
