package jaymanh.fsts.mixin;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class CampfireUnlitMixin {
    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)

    private void makeUnlitOnPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir){
        BlockState originalState = cir.getReturnValue();

        if(originalState!=null && originalState.hasProperty(CampfireBlock.LIT)){
            cir.setReturnValue(originalState.setValue(CampfireBlock.LIT,false));
        }
    }
}
