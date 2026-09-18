package jaymanh.fsts.client.compat.jei;

import jaymanh.fsts.compat.tooltip.ProcessTooltips;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.ItemLike;

public final class ProcessRecipeCategory extends AbstractRecipeCategory<SimpleProcessRecipe> {
	private static final int SLOT = 18;
	private static final int ARROW_WIDTH = 20;
	private static final int PADDING = 4;

	public ProcessRecipeCategory(IGuiHelper guiHelper, IRecipeType<SimpleProcessRecipe> recipeType, Component title, ItemLike icon) {
		super(recipeType, title, guiHelper.createDrawableItemLike(icon), 120, 44);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, SimpleProcessRecipe recipe, IFocusGroup focuses) {
		int x = 1;
		for (SlotDisplay input : recipe.inputs()) {
			builder.addInputSlot(x, 1).add(input);
			x += SLOT;
		}
		for (SlotDisplay catalyst : recipe.catalysts()) {
			builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, x, 1).add(catalyst);
			x += SLOT;
		}
		int outputX = Math.max(x + ARROW_WIDTH + PADDING, 91);
		builder.addOutputSlot(outputX, 1).add(recipe.output());
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, SimpleProcessRecipe recipe, IFocusGroup focuses) {
		int inputSlots = recipe.inputs().size() + recipe.catalysts().size();
		int arrowX = 1 + inputSlots * SLOT + 2;
		if (recipe.durationTicks() > 0) {
			builder.addAnimatedRecipeArrowWidget(recipe.durationTicks()).setPosition(arrowX, 1);
			builder.addText(
							Component.translatable("jei.from-stone-to-stars.duration", ProcessTooltips.duration(recipe.durationTicks())),
							getWidth(),
							12
					)
					.setPosition(0, 22)
					.setColor(0xFF404040);
		} else {
			builder.addRecipeArrowWidget().setPosition(arrowX, 1);
		}
	}

	@Override
	public Identifier getIdentifier(SimpleProcessRecipe recipe) {
		return recipe.id();
	}
}
