package jaymanh.fsts.recipe;

import java.util.List;

import jaymanh.fsts.block.ModBlocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;

/**
 * One input item to one output stack. Used by the mortar and any later grinders.
 */
public class GrindingRecipe extends SingleItemRecipe {
	public GrindingRecipe(Recipe.CommonInfo info, Ingredient ingredient, ItemStackTemplate result) {
		super(info, ingredient, result);
	}

	@Override
	public RecipeType<GrindingRecipe> getType() {
		return ModRecipes.GRINDING;
	}

	@Override
	public RecipeSerializer<GrindingRecipe> getSerializer() {
		return ModRecipes.GRINDING_SERIALIZER;
	}

	@Override
	public String group() {
		return "";
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return ModRecipes.GRINDING_CATEGORY;
	}

	public ItemStack resultStack() {
		return this.result().create();
	}

	@Override
	public List<RecipeDisplay> display() {
		return List.of(new StonecutterRecipeDisplay(
				this.input().display(),
				new SlotDisplay.ItemStackSlotDisplay(this.result()),
				new SlotDisplay.ItemSlotDisplay(ModBlocks.MORTAR_AND_PESTLE.asItem())
		));
	}
}
