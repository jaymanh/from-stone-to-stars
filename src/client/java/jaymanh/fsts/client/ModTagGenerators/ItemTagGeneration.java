package jaymanh.fsts.client.ModTagGenerators;

import jaymanh.fsts.item.ModItemIds;
import jaymanh.fsts.tags.ModTags;
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

	public ItemTagGeneration(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
		super(output, registryLookupFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		builder(ItemTags.SWORDS)
				.add(ModItemIds.FLINT_KNIFE)
				.add(ModItemIds.BONE_SWORD);

		builder(ItemTags.AXES)
				.add(ModItemIds.BONE_AXE)
				.add(ModItemIds.FLINT_AXE);

		builder(ItemTags.PICKAXES)
				.add(ModItemIds.BONE_PICKAXE);

		builder(ItemTags.SHOVELS)
				.add(ModItemIds.BONE_SHOVEL);

		builder(ItemTags.HOES)
				.add(ModItemIds.BONE_HOE);

		builder(C_LEATHERS)
				.add(ModItemIds.RAW_LEATHER);

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
	}

	private static ResourceKey<Item> minecraftItem(String path) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", path));
	}
}
