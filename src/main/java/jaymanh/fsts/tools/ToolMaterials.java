package jaymanh.fsts.tools;

import net.minecraft.world.item.ToolMaterial;

import static jaymanh.fsts.tools.ToolKeys.ItemKey.BONE_REPAIR_ITEMS;
import static net.minecraft.tags.BlockTags.*;

public enum ToolMaterials {
    BONEMATERIAL(
            new ToolMaterial(
                INCORRECT_FOR_STONE_TOOL, 150, 4.5f, 1f, 10,
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
