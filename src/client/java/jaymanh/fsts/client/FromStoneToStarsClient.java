package jaymanh.fsts.client;

import jaymanh.fsts.block.entity.ModBlockEntities;
import jaymanh.fsts.client.renderer.BronzeChestModel;
import jaymanh.fsts.client.renderer.BronzeChestRenderer;
import jaymanh.fsts.client.renderer.SawmillPortRenderer;
import jaymanh.fsts.client.screen.ForgingStumpScreen;
import jaymanh.fsts.client.screen.TappingSpoutScreen;
import jaymanh.fsts.inventory.ModMenus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class FromStoneToStarsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenus.FORGING_STUMP, ForgingStumpScreen::new);
		MenuScreens.register(ModMenus.TAPPING_SPOUT, TappingSpoutScreen::new);
		ModelLayerRegistry.registerModelLayer(BronzeChestModel.SINGLE, BronzeChestModel::createSingleLayer);
		ModelLayerRegistry.registerModelLayer(BronzeChestModel.LEFT, BronzeChestModel::createLeftLayer);
		ModelLayerRegistry.registerModelLayer(BronzeChestModel.RIGHT, BronzeChestModel::createRightLayer);
		BlockEntityRenderers.register(ModBlockEntities.BRONZE_CHEST, BronzeChestRenderer::new);
		BlockEntityRenderers.register(ModBlockEntities.SAWMILL_PORT, SawmillPortRenderer::new);
	}
}