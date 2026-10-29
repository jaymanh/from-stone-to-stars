package jaymanh.fsts.item;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModBlocks;

import jaymanh.fsts.tools.ModTools;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ModCreativeTabs {
	public static final ResourceKey<CreativeModeTab> MAIN_KEY = ResourceKey.create(
			BuiltInRegistries.CREATIVE_MODE_TAB.key(),
			FromStoneToStars.id("main")
	);

	public static final CreativeModeTab MAIN = FabricCreativeModeTab.builder()
			.icon(() -> new ItemStack(ModItems.RAW_LEATHER))
			.title(Component.translatable("itemGroup.from-stone-to-stars.main"))
			.displayItems((params, output) -> {
				output.accept(ModItems.PLANT_FIBER);
				output.accept(ModItems.RAW_LEATHER);
				output.accept(ModItems.LEATHER_STRIPS);
				output.accept(ModTools.LEATHER_SKIN);
				output.accept(ClayJugItem.createEmpty());
				output.accept(ModItems.BARK);
				output.accept(ModItems.SHARPENED_BONE);
				output.accept(ModItems.FLOUR);
				output.accept(ModItems.DOUGH);

				output.accept(ModBlocks.PEBBLE);
				output.accept(ModBlocks.STICKS);
				output.accept(ModBlocks.KINDLING);
				output.accept(ModBlocks.CHARCOAL_BLOCK);
				output.accept(ModBlocks.CLAY_BRICKS);
				output.accept(Items.BRICK);
				output.accept(ModBlocks.UNFIRED_CLAY_JUG);
				output.accept(ModBlocks.UNFIRED_STORAGE_POT);
				output.accept(ModBlocks.STORAGE_POT);
				output.accept(ModBlocks.BRONZE_CHEST);
				output.accept(ModBlocks.ROPE);
				output.accept(ModBlocks.THATCH);
				output.accept(ModBlocks.WATTLE_AND_DAUB);
				output.accept(ModBlocks.RUSTY_MUD);
				output.accept(ModBlocks.DRIED_RUSTY_MUD);
				output.accept((ModItems.IRON_SPECKS));
				output.accept(ModItems.STEEL_INGOT);
				output.accept(ModItems.BRONZE_REINFORCED_CRUCIBLE);

				output.accept(ModBlocks.ZINC_ORE);
				output.accept(ModBlocks.DEEPSLATE_ZINC_ORE);
				output.accept(ModItems.RAW_ZINC);
				output.accept(ModItems.CRUSHED_IRON);
				output.accept(ModItems.CRUSHED_COPPER);
				output.accept(ModItems.CRUSHED_GOLD);
				output.accept(ModItems.CRUSHED_ZINC);
				output.accept(ModItems.ZINC_INGOT);
				output.accept(ModItems.BRONZE_INGOT);
				output.accept(ModItems.HEATED_BRONZE_INGOT);
				output.accept(ModItems.HEATED_COPPER_INGOT);
				output.accept(ModItems.HEATED_IRON_INGOT);
				output.accept(ModItems.HEATED_STEEL_INGOT);
				output.accept(ModItems.BRONZE_NAIL);
				output.accept(ModItems.COPPER_NAIL);
				output.accept(ModItems.BRONZE_SHAFT);
				output.accept(ModItems.COPPER_SHAFT);
				output.accept(ModItems.BRONZE_TOOL_BLADE);
				output.accept(ModItems.BRONZE_PLATE);
				output.accept(ModItems.BRONZE_BUCKET);
				output.accept(ModItems.BRONZE_WATER_BUCKET);
				output.accept(ModItems.BRONZE_LAVA_BUCKET);
				output.accept(ModItems.BRONZE_MILK_BUCKET);
				output.accept(ModItems.BRONZE_POWDER_SNOW_BUCKET);
				output.accept(ModItems.BRONZE_COD_BUCKET);
				output.accept(ModItems.BRONZE_SALMON_BUCKET);
				output.accept(ModItems.BRONZE_TROPICAL_FISH_BUCKET);
				output.accept(ModItems.BRONZE_PUFFERFISH_BUCKET);
				output.accept(ModItems.BRONZE_TADPOLE_BUCKET);
				output.accept(ModItems.BRONZE_AXOLOTL_BUCKET);
				output.accept(ModItems.BRONZE_SULFUR_CUBE_BUCKET);
				output.accept(ModItems.IRON_TOOL_HEAD);
				output.accept(ModItems.IRON_PLATE);
				output.accept(ModItems.CRUCIBLE);
				output.accept(ModBlocks.STEEL_SAW_BLADES);

				output.accept(ModBlocks.TANNING_RACK);
				output.accept(ModBlocks.UNFINISHED_WORK_STUMP);
				output.accept(ModBlocks.WORK_STUMP);
				output.accept(ModBlocks.FORGING_STUMP);
				output.accept(ModBlocks.BASIC_SMELTER);
				output.accept(ModBlocks.FORGE_HEARTH);
				output.accept(ModBlocks.BELLOWS);
				output.accept(ModBlocks.POWERED_BELLOWS);
				output.accept(ModBlocks.FORGE_INPUT);
				output.accept(ModBlocks.TAPPING_SPOUT);
				output.accept(ModBlocks.ORE_CRUSHER);
				output.accept(ModBlocks.DRILL_HEAD);
				output.accept(ModBlocks.ROD_RACK);
				output.accept(ModBlocks.ORE_BOX);
				output.accept(ModBlocks.DRIVE_HOUSING);
				output.accept(ModTools.HAMMERSTONE);
				output.accept(ModBlocks.MORTAR_AND_PESTLE);
				output.accept(ModBlocks.WATER_WHEEL);
				output.accept(ModBlocks.WOODEN_SHAFT);
				output.accept(ModBlocks.WOODEN_ROLLERS);
				output.accept(ModBlocks.LOG_INTAKE);
				output.accept(ModBlocks.PLANK_OUTFEED);
				output.accept(ModBlocks.SAW_ARBOR);
				output.accept(ModBlocks.ROTATION_GEAR);
				output.accept(ModBlocks.POWERED_QUERN);
				output.accept(ModBlocks.TRIP_HAMMER);
				output.accept(ModBlocks.BRONZE_HOPPER);
				output.accept(ModBlocks.SNARE);
				output.accept(ModBlocks.FISH_TRAP);
				output.accept(ModBlocks.HIDE_BED);

				output.accept(ModTools.FLINT_KNIFE);
				output.accept(ModTools.IRON_KNIFE);
				output.accept(ModTools.FLINT_AXE);
				output.accept(ModTools.FLINT_SPEAR);
				output.accept(ModTools.BOW_AND_DRILL);

				output.accept(ModTools.BONE_SWORD);
				output.accept(ModTools.BONE_AXE);
				output.accept(ModTools.BONE_PICKAXE);
				output.accept(ModTools.BONE_SHOVEL);
				output.accept(ModTools.BONE_HOE);
				output.accept(ModTools.BONE_SAW);

				output.accept(ModTools.BRONZE_SWORD);
				output.accept(ModTools.BRONZE_AXE);
				output.accept(ModTools.BRONZE_PICKAXE);
				output.accept(ModTools.BRONZE_HOE);
				output.accept(ModTools.BRONZE_SHOVEL);
				output.accept(ModTools.BRONZE_SPEAR);
				output.accept(ModTools.BRONZE_HAMMER);
				output.accept(ModTools.IRON_HAMMER);
				output.accept(ModTools.BRONZE_HELMET);
				output.accept(ModTools.BRONZE_CHESTPLATE);
				output.accept(ModTools.BRONZE_LEGGINGS);
				output.accept(ModTools.BRONZE_BOOTS);

				output.accept(ModItems.STEEL_SHEET);
				output.accept(ModItems.STEEL_BLADE);
				output.accept(ModItems.STEEL_DRILL_ROD);
			})
			.build();

	private ModCreativeTabs() {
	}

	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_KEY, MAIN);
	}
}
