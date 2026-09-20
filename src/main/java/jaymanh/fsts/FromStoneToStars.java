package jaymanh.fsts;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.entity.ModBlockEntities;
import jaymanh.fsts.compat.energy.TeamRebornEnergyCompat;
import jaymanh.fsts.config.FstsConfig;
import jaymanh.fsts.item.ModCreativeTabs;
import jaymanh.fsts.item.ModItems;
import jaymanh.fsts.progression.VanillaProgression;
import jaymanh.fsts.tags.ModTags;
import jaymanh.fsts.item.UseInteractions;
import jaymanh.fsts.multiblock.ModMultiblocks;
import jaymanh.fsts.recipe.ModRecipes;
import jaymanh.fsts.tools.ModTools;
import jaymanh.fsts.worldgen.ModWorldgen;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FromStoneToStars implements ModInitializer {
	public static final String MOD_ID = "from-stone-to-stars";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		FstsConfig.load();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModItems.initialize();
		ModTools.initialize();
		ModCreativeTabs.initialize();
		ModWorldgen.initialize();
		VanillaProgression.initialize();
		TeamRebornEnergyCompat.initialize();
		ModTags.initialize();
		ModRecipes.initialize();
		ModMultiblocks.initialize();
		UseInteractions.initialise();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
