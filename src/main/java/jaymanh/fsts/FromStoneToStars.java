package jaymanh.fsts;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.item.ModCreativeTabs;
import jaymanh.fsts.item.ModItems;
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
		ModBlocks.initialize();
		ModItems.initialize();
		ModCreativeTabs.initialize();
		ModWorldgen.initialize();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
