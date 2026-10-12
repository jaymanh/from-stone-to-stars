package jaymanh.fsts.client.screen;

import java.util.List;

import com.mojang.blaze3d.platform.cursor.CursorTypes;

import jaymanh.fsts.inventory.ForgingStumpMenu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * Stonecutter-style recipe grid for the forging stump. Buttons the stack cannot afford yet are dimmed.
 */
public class ForgingStumpScreen extends AbstractContainerScreen<ForgingStumpMenu> {
	private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/scroller");
	private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/scroller_disabled");
	private static final Identifier RECIPE_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe_selected");
	private static final Identifier RECIPE_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe_highlighted");
	private static final Identifier RECIPE_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe");
	private static final Identifier BG_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/stonecutter.png");

	private static final int RECIPES_COLUMNS = 4;
	private static final int RECIPES_ROWS = 3;
	private static final int RECIPES_VISIBLE = RECIPES_COLUMNS * RECIPES_ROWS;
	private static final int RECIPE_WIDTH = 16;
	private static final int RECIPE_HEIGHT = 18;
	private static final int RECIPES_X = 52;
	private static final int RECIPES_Y = 14;

	private float scrollOffs;
	private boolean scrolling;
	private int startIndex;
	private boolean displayRecipes;

	public ForgingStumpScreen(ForgingStumpMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		menu.registerUpdateListener(this::containerChanged);
		this.titleLabelY--;
		this.containerChanged();
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		// Icons can arrive after the slot listener ran, so read the menu again before drawing.
		this.containerChanged();
		int left = this.leftPos;
		int top = this.topPos;
		graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, left, top, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
		int scroll = (int) (41.0F * this.scrollOffs);
		Identifier scroller = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, scroller, left + 119, top + 15 + scroll, 12, 15);

		int recipesLeft = left + RECIPES_X;
		int recipesTop = top + RECIPES_Y;
		int last = this.startIndex + RECIPES_VISIBLE;
		this.extractButtons(graphics, mouseX, mouseY, recipesLeft, recipesTop, last);
		this.extractRecipes(graphics, recipesLeft, recipesTop, last);
		this.extractProgress(graphics);
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		super.extractTooltip(graphics, mouseX, mouseY);
		if (!this.displayRecipes) {
			return;
		}
		int left = this.leftPos + RECIPES_X;
		int top = this.topPos + RECIPES_Y;
		int last = this.startIndex + RECIPES_VISIBLE;
		int recipeCount = this.menu.getNumberOfVisibleRecipes();
		for (int index = this.startIndex; index < last && index < recipeCount; index++) {
			int visible = index - this.startIndex;
			int buttonX = left + visible % RECIPES_COLUMNS * RECIPE_WIDTH;
			int buttonY = top + visible / RECIPES_COLUMNS * RECIPE_HEIGHT + 2;
			if (mouseX >= buttonX && mouseX < buttonX + RECIPE_WIDTH && mouseY >= buttonY && mouseY < buttonY + RECIPE_HEIGHT) {
				ItemStack result = this.menu.getRecipeIcon(index);
				graphics.setComponentTooltipForNextFrame(this.font, List.of(
						result.getHoverName(),
						Component.translatable("container.from-stone-to-stars.forging.cost", this.menu.getRecipeCost(index)).withStyle(ChatFormatting.GRAY),
						Component.translatable("container.from-stone-to-stars.forging.hits", this.menu.getRecipeHits(index)).withStyle(ChatFormatting.GRAY)
				), mouseX, mouseY);
			}
		}
	}

	private void extractButtons(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int x, int y, int last) {
		for (int index = this.startIndex; index < last && index < this.menu.getNumberOfVisibleRecipes(); index++) {
			int visible = index - this.startIndex;
			int buttonX = x + visible % RECIPES_COLUMNS * RECIPE_WIDTH;
			int buttonY = y + visible / RECIPES_COLUMNS * RECIPE_HEIGHT + 2;
			boolean affordable = this.menu.canCraft(index);
			boolean selected = index == this.menu.getSelectedRecipeIndex();
			Identifier sprite;
			if (selected) {
				sprite = RECIPE_SELECTED_SPRITE;
			} else if (affordable && mouseX >= buttonX && mouseY >= buttonY && mouseX < buttonX + RECIPE_WIDTH && mouseY < buttonY + RECIPE_HEIGHT) {
				sprite = RECIPE_HIGHLIGHTED_SPRITE;
			} else {
				sprite = RECIPE_SPRITE;
			}
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, buttonX, buttonY - 1, RECIPE_WIDTH, RECIPE_HEIGHT);
			if (affordable && mouseX >= buttonX && mouseY >= buttonY - 1 && mouseX < buttonX + RECIPE_WIDTH && mouseY < buttonY - 1 + RECIPE_HEIGHT) {
				graphics.requestCursor(CursorTypes.POINTING_HAND);
			}
		}
	}

	private void extractRecipes(GuiGraphicsExtractor graphics, int x, int y, int last) {
		int recipeCount = this.menu.getNumberOfVisibleRecipes();
		for (int index = this.startIndex; index < last && index < recipeCount; index++) {
			int visible = index - this.startIndex;
			int itemX = x + visible % RECIPES_COLUMNS * RECIPE_WIDTH;
			int itemY = y + visible / RECIPES_COLUMNS * RECIPE_HEIGHT + 2;
			ItemStack result = this.menu.getRecipeIcon(index);
			graphics.item(result, itemX, itemY);
			graphics.itemDecorations(this.font, result, itemX, itemY);
			if (!this.menu.canCraft(index)) {
				graphics.fill(itemX, itemY, itemX + RECIPE_WIDTH, itemY + RECIPE_WIDTH, 0x80000000);
			}
		}
	}

	private void extractProgress(GuiGraphicsExtractor graphics) {
		int selected = this.menu.getSelectedRecipeIndex();
		if (selected < 0 || selected >= this.menu.getNumberOfVisibleRecipes()) {
			return;
		}
		Component text = Component.translatable(
				"container.from-stone-to-stars.forging.progress",
				this.menu.getProgress(),
				this.menu.getRecipeHits(selected)
		);
		int x = this.leftPos + 151 - this.font.width(text) / 2;
		graphics.text(this.font, text, x, this.topPos + 54, -12566464, false);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
		this.scrolling = false;
		if (this.displayRecipes) {
			double mouseX = event.x();
			double mouseY = event.y();
			int left = this.leftPos + RECIPES_X;
			int top = this.topPos + RECIPES_Y;
			int last = this.startIndex + RECIPES_VISIBLE;
			for (int index = this.startIndex; index < last && index < this.menu.getNumberOfVisibleRecipes(); index++) {
				int visible = index - this.startIndex;
				double localX = mouseX - (left + visible % RECIPES_COLUMNS * RECIPE_WIDTH);
				double localY = mouseY - (top + visible / RECIPES_COLUMNS * RECIPE_HEIGHT);
				if (localX < 0.0 || localY < 0.0 || localX >= RECIPE_WIDTH || localY >= RECIPE_HEIGHT) {
					continue;
				}
				if (this.minecraft.player != null
						&& this.menu.canCraft(index)
						&& this.menu.clickMenuButton(this.minecraft.player, index)) {
					this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
					if (this.minecraft.gameMode != null) {
						this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
					}
				}
				return true;
			}

			int scrollerX = this.leftPos + 119;
			int scrollerY = this.topPos + 9;
			if (mouseX >= scrollerX && mouseX < scrollerX + 12 && mouseY >= scrollerY && mouseY < scrollerY + 54) {
				this.scrolling = true;
			}
		}
		return super.mouseClicked(event, doubled);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
		if (this.scrolling && this.isScrollBarActive()) {
			int top = this.topPos + RECIPES_Y;
			int bottom = top + 54;
			this.scrollOffs = ((float) event.y() - top - 7.5F) / (bottom - top - 15.0F);
			this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int) (this.scrollOffs * this.getOffscreenRows() + 0.5) * RECIPES_COLUMNS;
			return true;
		}
		return super.mouseDragged(event, dragX, dragY);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		this.scrolling = false;
		return super.mouseReleased(event);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
			return true;
		}
		if (this.isScrollBarActive()) {
			int rows = this.getOffscreenRows();
			this.scrollOffs = Mth.clamp(this.scrollOffs - (float) scrollY / rows, 0.0F, 1.0F);
			this.startIndex = (int) (this.scrollOffs * rows + 0.5) * RECIPES_COLUMNS;
		}
		return true;
	}

	private boolean isScrollBarActive() {
		return this.displayRecipes && this.menu.getNumberOfVisibleRecipes() > RECIPES_VISIBLE;
	}

	private int getOffscreenRows() {
		return Math.max(0, (this.menu.getNumberOfVisibleRecipes() + RECIPES_COLUMNS - 1) / RECIPES_COLUMNS - RECIPES_ROWS);
	}

	private void containerChanged() {
		this.displayRecipes = this.menu.hasInputItem();
		if (!this.displayRecipes) {
			this.scrollOffs = 0.0F;
			this.startIndex = 0;
			return;
		}
		int rows = this.getOffscreenRows();
		int maxStart = rows * RECIPES_COLUMNS;
		if (this.startIndex > maxStart) {
			this.startIndex = maxStart;
			this.scrollOffs = rows == 0 ? 0.0F : (float) (this.startIndex / RECIPES_COLUMNS) / rows;
		}
	}
}
