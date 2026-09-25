package jaymanh.fsts.tools;

import jaymanh.fsts.tags.ModTags;
import net.minecraft.world.item.ToolMaterial;

import static jaymanh.fsts.tools.ToolKeys.ItemKey.*;

public enum ToolMaterials {
    FLINTMATERIAL(
            new ToolMaterial(
                    ModTags.FSTS_INCORRECT_FOR_FLINT_TOOL, 80, 3f, 0f, 8,
                    FLINT_REPAIR_ITEMS
            )
    ),
    BONEMATERIAL(
            new ToolMaterial(
                    ModTags.FSTS_INCORRECT_FOR_BONE_TOOL, 150, 4.5f, 1f, 10,
                    BONE_REPAIR_ITEMS
            )
    ),
    BRASSMATERIAL(
            new ToolMaterial(
                    ModTags.FSTS_INCORRECT_FOR_BRASS_TOOL, 280, 5.5f, 2f, 15,
                    BRASS_REPAIR_ITEMS
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
