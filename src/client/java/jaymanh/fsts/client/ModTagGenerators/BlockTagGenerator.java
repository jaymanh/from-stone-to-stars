package jaymanh.fsts.client.ModTagGenerators;

import jaymanh.fsts.block.ModBlockIds;
import jaymanh.fsts.block.ModBlockItemIds;
import jaymanh.fsts.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

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
				.add(ModBlockItemIds.THATCH);

		builder(ModTags.FSTS_CHARCOAL_FLAMMABLE)
                .add(ModBlockItemIds.CHARCOAL_BLOCK);

        builder(ModTags.FSTS_NEEDS_BONE_TOOL)
                .addOptionalTag(BlockTags.LOGS)
                .addOptionalTag(BlockTags.COPPER_ORES)
                .add(ModBlockItemIds.TIN_ORE)
                .add(ModBlockItemIds.DEEPSLATE_TIN_ORE);

        builder(BlockTags.NEEDS_IRON_TOOL)
                .addOptionalTag(BlockTags.IRON_ORES);

        builder(ModTags.FSTS_INCORRECT_FOR_BONE_TOOL)
                .addOptionalTag(BlockTags.NEEDS_IRON_TOOL)
                .addOptionalTag(BlockTags.NEEDS_DIAMOND_TOOL);

        builder(ModTags.FSTS_INCORRECT_FOR_FLINT_TOOL)
                .addTag(ModTags.FSTS_NEEDS_BONE_TOOL)
                .addOptionalTag(BlockTags.NEEDS_STONE_TOOL)
                .addTag(ModTags.FSTS_INCORRECT_FOR_BONE_TOOL);

        builder(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlockItemIds.KINDLING)
                .add(ModBlockItemIds.TANNING_RACK)
                .add(ModBlockIds.PLACED_BONE_SAW)
				.add(ModBlockItemIds.SNARE)
				.add(ModBlockItemIds.FISH_TRAP)
				.add(ModBlockItemIds.HIDE_BED)
				.add(ModBlockItemIds.THATCH)
				.add(ModBlockItemIds.WATTLE_AND_DAUB);

		builder(BlockTags.BEDS)
				.add(ModBlockItemIds.HIDE_BED);

		builder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlockItemIds.CHARCOAL_BLOCK)
                .add(ModBlockItemIds.BASIC_SMELTER)
                .add(ModBlockItemIds.TIN_ORE)
                .add(ModBlockItemIds.DEEPSLATE_TIN_ORE);

        builder(BlockTags.CLIMBABLE)
                .add(ModBlockItemIds.ROPE);

    }
}
