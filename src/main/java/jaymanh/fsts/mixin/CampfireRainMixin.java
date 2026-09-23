package jaymanh.fsts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Lit campfires receive random ticks. If rain reaches them, they go out —
 * so a roofed camp keeps its fire.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public class CampfireRainMixin {
	@Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
	private void fsts$litCampfiresRandomTick(CallbackInfoReturnable<Boolean> cir) {
		BlockState state = (BlockState) (Object) this;
		if (state.getBlock() instanceof CampfireBlock && state.getValue(CampfireBlock.LIT)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "randomTick", at = @At("HEAD"))
	private void fsts$extinguishCampfireInRain(ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		BlockState state = (BlockState) (Object) this;
		if (!(state.getBlock() instanceof CampfireBlock) || !state.getValue(CampfireBlock.LIT)) {
			return;
		}
		if (!level.isRainingAt(pos)) {
			return;
		}

		level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
		CampfireBlock.dowse(null, level, pos, state);
		level.setBlock(pos, state.setValue(CampfireBlock.LIT, false), 3);
	}
}
