package jaymanh.fsts.client.ModTagGenerators;

import jaymanh.fsts.block.ModBlockIds;
import jaymanh.fsts.block.ModBlockItemIds;
import jaymanh.fsts.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {
    public BlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
		builder(ModTags.FSTS_WOOD_FLAMMABLE)
                .add(ModBlockItemIds.STICKS)
                .add(ModBlockItemIds.TANNING_RACK)
                .add(ModBlockItemIds.KINDLING)
				.add(ModBlockItemIds.SNARE)
				.add(ModBlockItemIds.FISH_TRAP)
				.add(ModBlockItemIds.HIDE_BED)
				.add(ModBlockItemIds.THATCH)
				.add(ModBlockItemIds.WORK_STUMP)
				.add(ModBlockItemIds.UNFINISHED_WORK_STUMP);

		builder(ModTags.FSTS_CHARCOAL_FLAMMABLE)
                .add(ModBlockItemIds.CHARCOAL_BLOCK);

        builder(ModTags.FSTS_NEEDS_BONE_TOOL)
                .addOptionalTag(BlockTags.COPPER_ORES)
                .add(ModBlockItemIds.ZINC_ORE)
                .add(ModBlockItemIds.DEEPSLATE_ZINC_ORE);

		builder(ModTags.FSTS_NEEDS_FLINT_TOOL)
				.addOptionalTag(BlockTags.LOGS);

        builder(BlockTags.NEEDS_IRON_TOOL)
                .addOptionalTag(BlockTags.IRON_ORES)
				.addOptional(minecraftBlock("raw_iron_block"))
				.addOptional(minecraftBlock("iron_block"))

				.remove(minecraftBlock("gold_ore"))
				.remove(minecraftBlock("deepslate_gold_ore"))
				.remove(minecraftBlock("gold_block"))
				.remove(minecraftBlock("raw_gold_block"));

		builder(ModTags.FSTS_NEEDS_BRONZE_TOOL)
				.addOptionalTag(BlockTags.GOLD_ORES)
				.add(minecraftBlock("gold_block"))
				.add(minecraftBlock("raw_gold_block"));

        builder(ModTags.FSTS_INCORRECT_FOR_BONE_TOOL)
                .addTag(ModTags.FSTS_NEEDS_BRONZE_TOOL)
                .addOptionalTag(BlockTags.NEEDS_IRON_TOOL)
                .addOptionalTag(BlockTags.NEEDS_DIAMOND_TOOL);

        builder(ModTags.FSTS_INCORRECT_FOR_FLINT_TOOL)
                .addTag(ModTags.FSTS_NEEDS_BONE_TOOL)
                .addOptionalTag(BlockTags.NEEDS_STONE_TOOL)
                .addTag(ModTags.FSTS_INCORRECT_FOR_BONE_TOOL);

		// Bronze sits above bone: can mine gold (needs_bronze) but not iron/diamond-tier blocks.
		builder(ModTags.FSTS_INCORRECT_FOR_BRONZE_TOOL)
				.addOptionalTag(BlockTags.NEEDS_IRON_TOOL)
				.addOptionalTag(BlockTags.NEEDS_DIAMOND_TOOL);

        builder(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlockItemIds.KINDLING)
                .add(ModBlockItemIds.TANNING_RACK)
                .add(ModBlockIds.PLACED_BONE_SAW)
				.add(ModBlockItemIds.SNARE)
				.add(ModBlockItemIds.FISH_TRAP)
				.add(ModBlockItemIds.HIDE_BED)
				.add(ModBlockItemIds.THATCH)
				.add(ModBlockItemIds.WATTLE_AND_DAUB)
				.add(ModBlockItemIds.WORK_STUMP)
				.add(ModBlockItemIds.UNFINISHED_WORK_STUMP);

		builder(BlockTags.BEDS)
				.add(ModBlockItemIds.HIDE_BED);

		builder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlockItemIds.CHARCOAL_BLOCK)
                .add(ModBlockItemIds.BASIC_SMELTER)
                .add(ModBlockItemIds.ZINC_ORE)
                .add(ModBlockItemIds.DEEPSLATE_ZINC_ORE)
				.add(ModBlockItemIds.MORTAR_AND_PESTLE)
				.add(ModBlockItemIds.CLAY_BRICKS)
				.add(ModBlockIds.FIRED_CLAY_BRICKS);

        builder(BlockTags.CLIMBABLE)
                .add(ModBlockItemIds.ROPE);

    }

	private static ResourceKey<Block> minecraftBlock(String path) {
		return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("minecraft", path));
	}
}
