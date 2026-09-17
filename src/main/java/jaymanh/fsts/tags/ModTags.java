package jaymanh.fsts.tags;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static jaymanh.fsts.FromStoneToStars.MOD_ID;

public class ModTags {
    public static final TagKey<Block> FSTS_FLAMMABLE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "flammable"));

    public static final TagKey<Block> FSTS_NEEDS_BONE_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "needs_bone_tool"));
    public static final TagKey<Block> FSTS_INCORRECT_FOR_FLINT_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "incorrect_for_flint_tool"));


    public static void initialize(){
        FlammableBlockRegistry.getDefaultInstance().add(FSTS_FLAMMABLE, 25,10);
    }
}
