package jaymanh.fsts.tools;

import java.util.Map;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * Leather-backed bronze plate armor: a bit tougher than leather, short of full copper/iron plate.
 */
public final class ModArmorMaterials {
	public static final ResourceKey<EquipmentAsset> BRONZE_ASSET = ResourceKey.create(
			EquipmentAssets.ROOT_ID,
			FromStoneToStars.id("bronze")
	);

	/** Durability between leather (5) and copper (11). Defense roughly copper-tier. */
	public static final ArmorMaterial BRONZE = new ArmorMaterial(
			10,
			ArmorMaterials.makeDefense(2, 3, 4, 2, 4),
			12,
			SoundEvents.ARMOR_EQUIP_LEATHER,
			0.0F,
			0.0F,
			ToolKeys.ItemKey.BRONZE_ARMOR_REPAIR,
			BRONZE_ASSET
	);

	private ModArmorMaterials() {
	}
}
