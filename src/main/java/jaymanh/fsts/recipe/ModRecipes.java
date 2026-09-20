package jaymanh.fsts.recipe;

import java.util.Optional;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public final class ModRecipes {
	public static final RecipeType<GrindingRecipe> GRINDING = Registry.register(
			BuiltInRegistries.RECIPE_TYPE,
			FromStoneToStars.id("grinding"),
			new RecipeType<GrindingRecipe>() {
				@Override
				public String toString() {
					return FromStoneToStars.MOD_ID + ":grinding";
				}
			}
	);

	public static final RecipeSerializer<GrindingRecipe> GRINDING_SERIALIZER = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			FromStoneToStars.id("grinding"),
			new RecipeSerializer<>(
					SingleItemRecipe.simpleMapCodec(GrindingRecipe::new),
					SingleItemRecipe.simpleStreamCodec(GrindingRecipe::new)
			)
	);

	public static final RecipeBookCategory GRINDING_CATEGORY = Registry.register(
			BuiltInRegistries.RECIPE_BOOK_CATEGORY,
			FromStoneToStars.id("grinding"),
			new RecipeBookCategory()
	);

	private ModRecipes() {
	}

	public static void initialize() {
	}

	public static Optional<RecipeHolder<GrindingRecipe>> findGrinding(Level level, ItemStack stack) {
		if (level == null || stack.isEmpty()) {
			return Optional.empty();
		}
		if (!(level.recipeAccess() instanceof RecipeManager recipes)) {
			return Optional.empty();
		}
		return recipes.getRecipeFor(GRINDING, new SingleRecipeInput(stack), level);
	}
}
