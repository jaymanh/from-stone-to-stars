package jaymanh.fsts.tools;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static jaymanh.fsts.FromStoneToStars.MOD_ID;

public class ToolKeys{
    public static class ItemKey{
        public static final TagKey<Item> FLINT_REPAIR_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "flint_repair_item"));
        public static final TagKey<Item> BONE_REPAIR_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID,"bone_repair_item"));
        public static final TagKey<Item> BRONZE_REPAIR_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID,"bronze_repair_item"));
    }
}
