package jaymanh.fsts.ModTagGenerators;

import jaymanh.fsts.tools.ModTools;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static jaymanh.fsts.FromStoneToStars.MOD_ID;

public class ItemTagGeneration extends FabricTagsProvider.ItemTagsProvider {
    public ItemTagGeneration(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture, @Nullable BlockTagsProvider blockTagsProvider) {
        super(output, registryLookupFuture, blockTagsProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(ItemTags.SWORDS).addOptionalTag(Identifier.fromNamespaceAndPath(MOD_ID, ModTools.FLINT_KNIFE.toString()))
                .addOptionalTag(Identifier.fromNamespaceAndPath(MOD_ID, ModTools.BONE_SWORD.toString()));

        getOrCreateRawBuilder(ItemTags.AXES).addOptionalTag(Identifier.fromNamespaceAndPath(MOD_ID, ModTools.BONE_AXE.toString()));

        getOrCreateRawBuilder(ItemTags.PICKAXES).addOptionalTag(Identifier.fromNamespaceAndPath(MOD_ID, ModTools.BONE_PICKAXE.toString()));

        getOrCreateRawBuilder(ItemTags.SHOVELS).addOptionalTag(Identifier.fromNamespaceAndPath(MOD_ID, ModTools.BONE_SHOVEL.toString()));

        getOrCreateRawBuilder(ItemTags.HOES).addOptionalTag(Identifier.fromNamespaceAndPath(MOD_ID, ModTools.BONE_HOE.toString()));
    }
}
