package jaymanh.fsts.client.screen;

import jaymanh.fsts.inventory.TappingSpoutMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Drawn like a one-row chest.
 */
public class TappingSpoutScreen extends AbstractContainerScreen<TappingSpoutMenu> {
	private static final Identifier BG_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
	private static final int TOP_HEIGHT = TappingSpoutMenu.SLOT_Y + 18 - 1;
	private static final int BOTTOM_V = 126;
	private static final int BOTTOM_HEIGHT = 96;

	public TappingSpoutScreen(TappingSpoutMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, 176, TOP_HEIGHT + BOTTOM_HEIGHT);
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		int left = this.leftPos;
		int top = this.topPos;
		graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, left, top, 0.0F, 0.0F, this.imageWidth, TOP_HEIGHT, 256, 256);
		graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, left, top + TOP_HEIGHT, 0.0F, BOTTOM_V, this.imageWidth, BOTTOM_HEIGHT, 256, 256);
	}
}
