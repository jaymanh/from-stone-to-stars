package jaymanh.fsts.client.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;

import jaymanh.fsts.config.FstsConfig;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class FstsYaclScreen {
	private FstsYaclScreen() {
	}

	public static Screen create(Screen parent) {
		FstsConfig config = FstsConfig.INSTANCE;

		return YetAnotherConfigLib.createBuilder()
				.title(Component.translatable("config.from-stone-to-stars.title"))
				.category(ConfigCategory.createBuilder()
						.name(Component.translatable("config.from-stone-to-stars.category.general"))
						.option(Option.<Boolean>createBuilder()
								.name(Component.translatable("config.from-stone-to-stars.generatePebbles"))
								.description(OptionDescription.of(Component.translatable("config.from-stone-to-stars.generatePebbles.desc")))
								.binding(true, () -> config.generatePebbles, value -> config.generatePebbles = value)
								.controller(TickBoxControllerBuilder::create)
								.build())
						.option(Option.<Boolean>createBuilder()
								.name(Component.translatable("config.from-stone-to-stars.debugLogging"))
								.description(OptionDescription.of(Component.translatable("config.from-stone-to-stars.debugLogging.desc")))
								.binding(false, () -> config.debugLogging, value -> config.debugLogging = value)
								.controller(TickBoxControllerBuilder::create)
								.build())
						.build())
				.save(FstsConfig::save)
				.build()
				.generateScreen(parent);
	}
}
