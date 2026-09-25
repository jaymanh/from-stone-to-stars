package jaymanh.fsts.tools;

import jaymanh.fsts.item.ModItemIds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;

import static jaymanh.fsts.item.ModItems.register;

public class ModTools {

    public static final Item LEATHER_SKIN = register(
            ModItemIds.LEATHER_SKIN,
            LeatherSkinItem::new,
            // maxDamage = CAPACITY + 1 so damage == CAPACITY (empty) is not considered broken
            new Item.Properties()
                    .durability(LeatherSkinItem.CAPACITY + 1)
                    .component(DataComponents.DAMAGE, LeatherSkinItem.CAPACITY)
    );

    public static final Item BOW_AND_DRILL = register(
            ModItemIds.BOW_AND_DRILL,
            FlintAndSteelItem::new,
            new Item.Properties().durability(4)
    );

    public static final Item HAMMERSTONE = register(
            ModItemIds.HAMMERSTONE,
            HammerstoneItem::new,
            new Item.Properties().durability(2)
    );

    public static final Item FLINT_KNIFE = register(
            ModItemIds.FLINT_KNIFE,
            FlintKnifeItem::new,
            new Item.Properties().sword(ToolMaterials.FLINTMATERIAL.getMaterial(), 3F, -2.4F)
    );

    public static final Item FLINT_AXE = register(
            ModItemIds.FLINT_AXE,
            settings -> new AxeItem(ToolMaterials.FLINTMATERIAL.getMaterial(), 1.0F, -2.8F, settings),
            new Item.Properties()
    );

    public static final Item FLINT_SPEAR = register(
            ModItemIds.FLINT_SPEAR,
            Item::new,
            new Item.Properties().spear(ToolMaterials.FLINTMATERIAL.getMaterial(), 0.75F, 0.82F, 0.7F, 4.5F, 13.0F, 9.0F, 5.1F, 13.75F, 4.6F)
    );

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
    public static final SawToolItem BONE_SAW = register(
            ModItemIds.BONE_SAW,
            SawToolItem::new,
            new Item.Properties()
    );

    public static final Item BRONZE_SWORD = register(
            ModItemIds.BRONZE_SWORD,
            Item::new,
            new Item.Properties().sword(ToolMaterials.BRONZEMATERIAL.getMaterial(), 3.0F, -2.4F)
    );
    public static final Item BRONZE_PICKAXE = register(
            ModItemIds.BRONZE_PICKAXE,
            Item::new,
            new Item.Properties().pickaxe(ToolMaterials.BRONZEMATERIAL.getMaterial(), 1.0F, -2.8F)
    );
    public static final Item BRONZE_AXE = register(
            ModItemIds.BRONZE_AXE,
            settings -> new AxeItem(ToolMaterials.BRONZEMATERIAL.getMaterial(),6.0F, -3.2F, settings),
            new Item.Properties()
    );
    public static final Item BRONZE_SHOVEL = register(
            ModItemIds.BRONZE_SHOVEL,
            settings -> new ShovelItem(ToolMaterials.BRONZEMATERIAL.getMaterial(),1.5F, -3.0F, settings),
            new Item.Properties()
    );
    public static final Item BRONZE_HOE = register(
            ModItemIds.BRONZE_HOE,
            settings -> new HoeItem(ToolMaterials.BRONZEMATERIAL.getMaterial(),0.0F, -3.0F, settings),
            new Item.Properties()
    );
    public static final Item BRONZE_SPEAR = register(
            ModItemIds.BRONZE_SPEAR,
            Item::new,
            new Item.Properties().spear(ToolMaterials.BRONZEMATERIAL.getMaterial(), 0.95F, 0.95F, 0.6F, 2.5F, 11.0F, 6.75F, 5.1F, 11.25F, 4.6F)
    );



    public static void initialize(){

    }
}
