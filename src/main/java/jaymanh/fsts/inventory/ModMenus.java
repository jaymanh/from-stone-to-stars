package jaymanh.fsts.inventory;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public final class ModMenus {
	public static final MenuType<ForgingStumpMenu> FORGING_STUMP = Registry.register(
			BuiltInRegistries.MENU,
			FromStoneToStars.id("forging_stump"),
			new MenuType<>(ForgingStumpMenu::new, FeatureFlags.VANILLA_SET)
	);
	public static final MenuType<TappingSpoutMenu> TAPPING_SPOUT = Registry.register(
			BuiltInRegistries.MENU,
			FromStoneToStars.id("tapping_spout"),
			new MenuType<>(TappingSpoutMenu::new, FeatureFlags.VANILLA_SET)
	);

	private ModMenus() {
	}

	public static void initialize() {
	}
}
