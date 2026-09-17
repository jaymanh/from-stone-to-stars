package jaymanh.fsts.mixin;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerLevel.class)
public class CreeperNoDamageMixin {
    @ModifyVariable(method = "explode", at = @At("HEAD"), argsOnly = true)
    private Level.ExplosionInteraction disableMobExplosionGriefing(Level.ExplosionInteraction interaction) {
        return interaction == Level.ExplosionInteraction.MOB
                ? Level.ExplosionInteraction.NONE
                : interaction;
    }
}
