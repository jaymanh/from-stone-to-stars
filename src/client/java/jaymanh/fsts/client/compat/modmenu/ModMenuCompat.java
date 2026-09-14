package jaymanh.fsts.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import jaymanh.fsts.client.config.FstsYaclScreen;
import jaymanh.fsts.compat.CompatMods;

public final class ModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (!CompatMods.YACL) {
			return parent -> null;
		}

		return FstsYaclScreen::create;
	}
}
