package jaymanh.fsts.client.ModTagGenerators;

import jaymanh.fsts.block.ModBlockItemIds;
import jaymanh.fsts.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {
    public BlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(ModTags.FSTS_FLAMMABLE)
                .add(ModBlockItemIds.STICKS)
                .add(ModBlockItemIds.TANNING_RACK);
    }
}
