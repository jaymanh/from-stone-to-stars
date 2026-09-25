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
				output.accept(ModItems.BARK);
				output.accept(ModItems.SHARPENED_BONE);
				output.accept(ModItems.FLOUR);
				output.accept(ModItems.DOUGH);

				output.accept(ModBlocks.PEBBLE);
				output.accept(ModBlocks.STICKS);
				output.accept(ModBlocks.KINDLING);
				output.accept(ModBlocks.CHARCOAL_BLOCK);
				output.accept(ModBlocks.CLAY_BRICKS);
				output.accept(ModBlocks.FIRED_CLAY_BRICKS);
				output.accept(ModBlocks.ROPE);
				output.accept(ModBlocks.THATCH);
				output.accept(ModBlocks.WATTLE_AND_DAUB);

				output.accept(ModBlocks.TIN_ORE);
				output.accept(ModBlocks.DEEPSLATE_TIN_ORE);
				output.accept(ModItems.RAW_TIN);
				output.accept(ModItems.TIN_INGOT);
				output.accept(ModItems.BRASS_INGOT);

				output.accept(ModBlocks.TANNING_RACK);
				output.accept(ModBlocks.UNFINISHED_WORK_STUMP);
				output.accept(ModBlocks.WORK_STUMP);
				output.accept(ModBlocks.BASIC_SMELTER);
				output.accept(ModTools.HAMMERSTONE);
				output.accept(ModBlocks.MORTAR_AND_PESTLE);
				output.accept(ModBlocks.SNARE);
				output.accept(ModBlocks.FISH_TRAP);
				output.accept(ModBlocks.HIDE_BED);

				output.accept(ModTools.FLINT_KNIFE);
				output.accept(ModTools.FLINT_AXE);
				output.accept(ModTools.BOW_AND_DRILL);

				output.accept(ModTools.BONE_SWORD);
				output.accept(ModTools.BONE_AXE);
				output.accept(ModTools.BONE_PICKAXE);
				output.accept(ModTools.BONE_SHOVEL);
				output.accept(ModTools.BONE_HOE);
				output.accept(ModTools.BONE_SAW);

				output.accept(ModTools.BRASS_SWORD);
				output.accept(ModTools.BRASS_AXE);
				output.accept(ModTools.BRASS_PICKAXE);
				output.accept(ModTools.BRASS_HOE);
				output.accept(ModTools.BRASS_SHOVEL);
				output.accept(ModTools.BRASS_SPEAR);
			})
			.build();

	private ModCreativeTabs() {
	}

	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_KEY, MAIN);
	}
}
