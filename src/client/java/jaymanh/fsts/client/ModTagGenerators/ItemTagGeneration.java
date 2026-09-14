package jaymanh.fsts.client.ModTagGenerators;

import jaymanh.fsts.item.ModItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGeneration extends FabricTagsProvider.ItemTagsProvider {
    public ItemTagGeneration(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(ItemTags.SWORDS)
                .add(ModItemIds.FLINT_KNIFE)
                .add(ModItemIds.BONE_SWORD);

        builder(ItemTags.AXES)
                .add(ModItemIds.BONE_AXE);

        builder(ItemTags.PICKAXES)
                .add(ModItemIds.BONE_PICKAXE);

        builder(ItemTags.SHOVELS)
                .add(ModItemIds.BONE_SHOVEL);

        builder(ItemTags.HOES)
                .add(ModItemIds.BONE_HOE);
    }
}
