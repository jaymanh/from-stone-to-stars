package jaymanh.fsts.compat;

import net.fabricmc.loader.api.FabricLoader;

public final class CompatMods {
	public static final boolean MOD_MENU = isLoaded("modmenu");
	public static final boolean YACL = isLoaded("yet_another_config_lib_v3");
	public static final boolean EMI = isLoaded("emi");
	public static final boolean JEI = isLoaded("jei");
	public static final boolean JADE = isLoaded("jade");
	public static final boolean WTHIT = isLoaded("wthit");
	public static final boolean TEAM_REBORN_ENERGY = isLoaded("team_reborn_energy");
	public static final boolean AE2 = isLoaded("ae2");

	private CompatMods() {
	}

	private static boolean isLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}
}
