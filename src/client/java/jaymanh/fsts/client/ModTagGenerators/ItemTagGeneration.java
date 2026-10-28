package jaymanh.fsts.client.ModTagGenerators;

import jaymanh.fsts.block.ModBlockItemIds;
import jaymanh.fsts.item.ModItemIds;
import jaymanh.fsts.tags.ModTags;
import jaymanh.fsts.tools.ToolKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ItemTagGeneration extends FabricTagsProvider.ItemTagsProvider {
	private static final TagKey<Item> C_LEATHERS = TagKey.create(
			Registries.ITEM,
			Identifier.fromNamespaceAndPath("c", "leathers")
	);
	private static final TagKey<Item> C_WOODEN_CHESTS = TagKey.create(
			Registries.ITEM,
			Identifier.fromNamespaceAndPath("c", "chests/wooden")
	);

	public ItemTagGeneration(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
		super(output, registryLookupFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		builder(ItemTags.SWORDS)
				.add(ModItemIds.FLINT_KNIFE)
				.add(ModItemIds.IRON_KNIFE)
				.add(ModItemIds.BONE_SWORD)
				.add(ModItemIds.BRONZE_SWORD);

		builder(ModTags.KNIVES)
				.add(ModItemIds.FLINT_KNIFE)
				.add(ModItemIds.IRON_KNIFE);

		builder(ItemTags.AXES)
				.add(ModItemIds.BONE_AXE)
				.add(ModItemIds.FLINT_AXE)
				.add(ModItemIds.BRONZE_AXE);

		builder(ItemTags.PICKAXES)
				.add(ModItemIds.BONE_PICKAXE)
				.add(ModItemIds.BRONZE_PICKAXE);

		builder(ItemTags.SHOVELS)
				.add(ModItemIds.BONE_SHOVEL)
				.add(ModItemIds.BRONZE_SHOVEL);

		builder(ItemTags.HOES)
				.add(ModItemIds.BONE_HOE)
				.add(ModItemIds.BRONZE_HOE);

		builder(ItemTags.SPEARS)
				.add(ModItemIds.FLINT_SPEAR)
				.add(ModItemIds.BRONZE_SPEAR);

		builder(ItemTags.HEAD_ARMOR)
				.add(ModItemIds.BRONZE_HELMET);
		builder(ItemTags.CHEST_ARMOR)
				.add(ModItemIds.BRONZE_CHESTPLATE);
		builder(ItemTags.LEG_ARMOR)
				.add(ModItemIds.BRONZE_LEGGINGS);
		builder(ItemTags.FOOT_ARMOR)
				.add(ModItemIds.BRONZE_BOOTS);

		builder(ItemTags.BEDS)
				.add(ModBlockItemIds.HIDE_BED.item());

		builder(ToolKeys.ItemKey.FLINT_REPAIR_ITEMS)
				.add(minecraftItem("flint"));

		builder(ToolKeys.ItemKey.BRONZE_REPAIR_ITEMS)
				.add(ModItemIds.BRONZE_INGOT);

		builder(ToolKeys.ItemKey.BRONZE_ARMOR_REPAIR)
				.add(ModItemIds.BRONZE_PLATE)
				.add(ModItemIds.LEATHER_STRIPS);

		builder(ToolKeys.ItemKey.BONE_REPAIR_ITEMS)
				.add(ModItemIds.SHARPENED_BONE);

		builder(C_LEATHERS)
				.add(ModItemIds.RAW_LEATHER);

		builder(ModTags.CRUSHED_ORES)
				.add(ModItemIds.CRUSHED_IRON)
				.add(ModItemIds.CRUSHED_COPPER)
				.add(ModItemIds.CRUSHED_GOLD)
				.add(ModItemIds.CRUSHED_ZINC);

		builder(C_WOODEN_CHESTS)
				.add(ModBlockItemIds.BRONZE_CHEST.item());

		builder(ModTags.SNARE_MEAT_BAIT)
				.addOptionalTag(ItemTags.MEAT);

		builder(ModTags.SNARE_PLANT_BAIT)
				.add(minecraftItem("wheat"))
				.add(minecraftItem("wheat_seeds"))
				.add(minecraftItem("pumpkin_seeds"))
				.add(minecraftItem("melon_seeds"))
				.add(minecraftItem("beetroot_seeds"))
				.add(minecraftItem("carrot"))
				.add(minecraftItem("potato"))
				.add(minecraftItem("beetroot"))
				.add(minecraftItem("apple"))
				.add(minecraftItem("sweet_berries"))
				.add(minecraftItem("glow_berries"))
				.add(minecraftItem("bread"))
				.add(ModItemIds.PLANT_FIBER);

		builder(ModTags.FISHING_BAIT)
				.add(minecraftItem("bread"))
				.add(minecraftItem("wheat"))
				.add(minecraftItem("wheat_seeds"))
				.add(minecraftItem("beetroot"))
				.add(minecraftItem("beetroot_seeds"))
				.add(ModItemIds.PLANT_FIBER);

		builder(ModTags.WATER_VESSELS)
				.add(ModItemIds.LEATHER_SKIN)
				.add(ModItemIds.CLAY_JUG);

		builder(ModTags.NAILS)
				.add(ModItemIds.BRONZE_NAIL)
				.add(ModItemIds.COPPER_NAIL);

		builder(ModTags.SHAFTS)
				.add(ModItemIds.BRONZE_SHAFT)
				.add(ModItemIds.COPPER_SHAFT);

		builder(ModTags.COAL_BLOCK_ITEMS)
				.add(ModBlockItemIds.CHARCOAL_BLOCK.item())
				.add(minecraftItem("coal_block"));
	}

	private static ResourceKey<Item> minecraftItem(String path) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", path));
	}
}
