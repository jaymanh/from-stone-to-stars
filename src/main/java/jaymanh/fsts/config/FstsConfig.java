package jaymanh.fsts.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jaymanh.fsts.FromStoneToStars;

import net.fabricmc.loader.api.FabricLoader;

public final class FstsConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("from-stone-to-stars.json");

	public static FstsConfig INSTANCE = new FstsConfig();

	public boolean debugLogging = false;
	public boolean generatePebbles = true;

	private FstsConfig() {
	}

	public static void load() {
		if (!Files.exists(PATH)) {
			save();
			return;
		}

		try (Reader reader = Files.newBufferedReader(PATH)) {
			FstsConfig loaded = GSON.fromJson(reader, FstsConfig.class);
			if (loaded != null) {
				INSTANCE = loaded;
			}
		} catch (IOException exception) {
			FromStoneToStars.LOGGER.warn("Could not read config; using defaults", exception);
		}
	}

	public static void save() {
		try {
			Files.createDirectories(PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(PATH)) {
				GSON.toJson(INSTANCE, writer);
			}
		} catch (IOException exception) {
			FromStoneToStars.LOGGER.warn("Could not write config", exception);
		}
	}
}
