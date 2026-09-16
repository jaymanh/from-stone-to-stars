package jaymanh.fsts.tags;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static jaymanh.fsts.FromStoneToStars.MOD_ID;

public class ModTags {
    public static final TagKey<Block> FSTS_FLAMMABLE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "flammable"));

    public static void initialize(){
        FlammableBlockRegistry.getDefaultInstance().add(FSTS_FLAMMABLE, 25,10);
    }
}
