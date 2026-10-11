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

public final class ForgingRecipeCategory extends AbstractRecipeCategory<ForgingJeiRecipe> {
	public ForgingRecipeCategory(IGuiHelper guiHelper, IRecipeType<ForgingJeiRecipe> recipeType, Component title, ItemLike icon) {
		super(recipeType, title, guiHelper.createDrawableItemLike(icon), 120, 44);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ForgingJeiRecipe recipe, IFocusGroup focuses) {
		builder.addInputSlot(1, 1).add(recipe.input());
		builder.addOutputSlot(91, 1).add(recipe.output());
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, ForgingJeiRecipe recipe, IFocusGroup focuses) {
		builder.addRecipeArrowWidget().setPosition(21, 1);
		builder.addText(
						Component.translatable("container.from-stone-to-stars.forging.hits", recipe.hits()),
						getWidth(),
						12
				)
				.setPosition(0, 22)
				.setColor(0xFF404040);
	}

	@Override
	public Identifier getIdentifier(ForgingJeiRecipe recipe) {
		return recipe.id();
	}
}
