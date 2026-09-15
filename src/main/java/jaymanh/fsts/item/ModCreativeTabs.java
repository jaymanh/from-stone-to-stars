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

				output.accept(ModBlocks.PEBBLE);
				output.accept(ModBlocks.STICKS);
				output.accept(ModTools.HAMMERSTONE);

				output.accept(ModTools.FLINT_KNIFE);

				output.accept(ModTools.BONE_SWORD);
				output.accept(ModTools.BONE_AXE);
				output.accept(ModTools.BONE_PICKAXE);
				output.accept(ModTools.BONE_SHOVEL);
				output.accept(ModTools.BONE_HOE);
			})
			.build();

	private ModCreativeTabs() {
	}

	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_KEY, MAIN);
	}
}
