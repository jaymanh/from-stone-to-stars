package jaymanh.fsts.client.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;

public final class WorldInteractionCategory extends AbstractRecipeCategory<WorldInteractionRecipe> {
	public WorldInteractionCategory(IGuiHelper guiHelper, IRecipeType<WorldInteractionRecipe> recipeType, Component title, ItemLike icon) {
		super(recipeType, title, guiHelper.createDrawableItemLike(icon), 96, 26);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, WorldInteractionRecipe recipe, IFocusGroup focuses) {
		builder.addInputSlot(1, 5).add(recipe.tool());
		builder.addInputSlot(21, 5).add(recipe.target());
		builder.addOutputSlot(75, 5).add(recipe.result());
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, WorldInteractionRecipe recipe, IFocusGroup focuses) {
		builder.addRecipePlusSignWidget().setPosition(16, 8);
		builder.addRecipeArrowWidget().setPosition(44, 5);
	}

	@Override
	public Identifier getIdentifier(WorldInteractionRecipe recipe) {
		return recipe.id();
	}
}
