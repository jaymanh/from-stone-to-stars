package jaymanh.fsts.tools;

import jaymanh.fsts.tags.ModTags;
import net.minecraft.world.item.ToolMaterial;

import static jaymanh.fsts.tools.ToolKeys.ItemKey.BONE_REPAIR_ITEMS;
import static jaymanh.fsts.tools.ToolKeys.ItemKey.FLINT_REPAIR_ITEMS;
import static net.minecraft.tags.BlockTags.*;

public enum ToolMaterials {
    FLINTMATERIAL(
            new ToolMaterial(
                    ModTags.FSTS_INCORRECT_FOR_FLINT_TOOL, 80, 3f, 0f, 8,
                    FLINT_REPAIR_ITEMS
            )
    ),
    BONEMATERIAL(
            new ToolMaterial(
                INCORRECT_FOR_WOODEN_TOOL, 150, 4.5f, 1f, 10,
                    BONE_REPAIR_ITEMS
            )
    );

    private final ToolMaterial material;

    ToolMaterials(ToolMaterial material){
        this.material = material;
    }

    public  ToolMaterial getMaterial(){
        return this.material;
    }
}
