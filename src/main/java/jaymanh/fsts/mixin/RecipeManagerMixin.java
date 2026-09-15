package jaymanh.fsts.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import jaymanh.fsts.progression.VanillaProgression;

import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
	@ModifyVariable(
			method = "apply(Lnet/minecraft/world/item/crafting/RecipeMap;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
			at = @At("HEAD"),
			argsOnly = true
	)
	private RecipeMap fsts$removeGatedRecipes(RecipeMap recipes) {
		List<RecipeHolder<?>> kept = new ArrayList<>();
		for (RecipeHolder<?> holder : recipes.values()) {
			if (!VanillaProgression.isRemovedRecipe(holder.id().identifier())) {
				kept.add(holder);
			}
		}
		return RecipeMap.create(kept);
	}
}
