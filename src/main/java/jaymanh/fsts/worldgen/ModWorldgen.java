package jaymanh.fsts.worldgen;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.config.FstsConfig;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class ModWorldgen {
	public static final ResourceKey<PlacedFeature> PEBBLE_GEN_NON_WATERLOGGED = ResourceKey.create(
			Registries.PLACED_FEATURE,
			FromStoneToStars.id("pebble_gen_non_waterlogged")
	);
	public static final ResourceKey<PlacedFeature> PEBBLE_GEN_WATERLOGGED = ResourceKey.create(
			Registries.PLACED_FEATURE,
			FromStoneToStars.id("pebble_gen_waterlogged")
	);
	public static final ResourceKey<PlacedFeature> STICKS_GEN = ResourceKey.create(
			Registries.PLACED_FEATURE,
			FromStoneToStars.id("sticks_gen")
	);
	public static final ResourceKey<PlacedFeature> ZINC_GEN = ResourceKey.create(
			Registries.PLACED_FEATURE,
			FromStoneToStars.id("zinc_gen")
	);
	/** Underwater dirt disks, same placement as vanilla clay disks. */
	public static final ResourceKey<PlacedFeature> RUSTY_MUD_DISK = ResourceKey.create(
			Registries.PLACED_FEATURE,
			FromStoneToStars.id("rusty_mud_disk")
	);
	/** Lush-cave stone veins, same placement as vanilla clay ore. */
	public static final ResourceKey<PlacedFeature> RUSTY_MUD_ORE = ResourceKey.create(
			Registries.PLACED_FEATURE,
			FromStoneToStars.id("rusty_mud_ore")
	);

	private ModWorldgen() {
	}

	public static void initialize() {
		MinYPlacement.initialize();
		if (FstsConfig.INSTANCE.generatePebbles) {
			BiomeModifications.addFeature(
					BiomeSelectors.foundInOverworld(),
					GenerationStep.Decoration.VEGETAL_DECORATION,
					PEBBLE_GEN_NON_WATERLOGGED
			);
			BiomeModifications.addFeature(
					BiomeSelectors.foundInOverworld(),
					GenerationStep.Decoration.VEGETAL_DECORATION,
					PEBBLE_GEN_WATERLOGGED
			);
		}

		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.VEGETAL_DECORATION,
				STICKS_GEN
		);

		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.UNDERGROUND_ORES,
				ZINC_GEN
		);

		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.UNDERGROUND_ORES,
				RUSTY_MUD_DISK
		);
		BiomeModifications.addFeature(
				BiomeSelectors.includeByKey(Biomes.LUSH_CAVES),
				GenerationStep.Decoration.UNDERGROUND_ORES,
				RUSTY_MUD_ORE
		);
	}
}
