package jaymanh.fsts.client;

import jaymanh.fsts.client.screen.ForgingStumpScreen;
import jaymanh.fsts.inventory.ModMenus;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class FromStoneToStarsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenus.FORGING_STUMP, ForgingStumpScreen::new);
	}
}