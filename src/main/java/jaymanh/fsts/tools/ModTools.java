package jaymanh.fsts.tools;

import jaymanh.fsts.item.ModItemIds;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;

import static jaymanh.fsts.item.ModItems.register;

public class ModTools {

    public static final Item BONE_SWORD = register(
            ModItemIds.BONE_SWORD,
            Item::new,
            new Item.Properties().sword(ToolMaterials.BONEMATERIAL.getMaterial(), 3.0F, -2.4F)
    );
    public static final AxeItem BONE_AXE = register(
            ModItemIds.BONE_AXE,
            settings -> new AxeItem(ToolMaterials.BONEMATERIAL.getMaterial(), 6.0F, -3.2F, settings),
            new Item.Properties()
    );
    public static final Item BONE_PICKAXE = register(
            ModItemIds.BONE_PICKAXE,
            Item::new,
            new Item.Properties().pickaxe(ToolMaterials.BONEMATERIAL.getMaterial(), 1.0F, -2.8F)
    );
    public static final ShovelItem BONE_SHOVEL = register(
            ModItemIds.BONE_SHOVEL,
            settings -> new ShovelItem(ToolMaterials.BONEMATERIAL.getMaterial(), 1.5F, -3.0F, settings),
            new Item.Properties()
    );
    public static final HoeItem BONE_HOE = register(
            ModItemIds.BONE_HOE,
            settings -> new HoeItem(ToolMaterials.BONEMATERIAL.getMaterial(), 0.0F, -3.0F, settings),
            new Item.Properties()
    );

    public static void initialize(){

    }
}
