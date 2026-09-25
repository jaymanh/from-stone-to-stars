package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.tags.ModTags;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
	@Shadow
	public abstract Block getBlock();

	@Inject(method = "requiresCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
	private void fsts$needsModTool(CallbackInfoReturnable<Boolean> cir) {
		Holder.Reference<Block> holder = this.getBlock().builtInRegistryHolder();
		if (holder.is(ModTags.FSTS_NEEDS_BONE_TOOL) || holder.is(ModTags.FSTS_NEEDS_FLINT_TOOL) || holder.is(ModTags.FSTS_NEEDS_BRASS_TOOL)) {
			cir.setReturnValue(true);
		}
	}
}
