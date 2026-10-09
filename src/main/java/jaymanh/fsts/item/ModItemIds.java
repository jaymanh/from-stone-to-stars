package jaymanh.fsts.item;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItemIds {
	public static final ResourceKey<Item> PLANT_FIBER = create("plant_fiber");
	public static final ResourceKey<Item> RAW_LEATHER = create("raw_leather");
	public static final ResourceKey<Item> LEATHER_STRIPS = create("leather_strips");
	public static final ResourceKey<Item> BARK = create("bark");
	public static final ResourceKey<Item> SHARPENED_BONE = create("sharpened_bone");
	public static final ResourceKey<Item> LEATHER_SKIN = create("leather_skin");
	public static final ResourceKey<Item> FLOUR = create("flour");
	public static final ResourceKey<Item> DOUGH = create("dough");
	public static final ResourceKey<Item> RAW_ZINC = create("raw_zinc");
	public static final ResourceKey<Item> ZINC_INGOT = create("zinc_ingot");
	public static final ResourceKey<Item> BRONZE_INGOT = create("bronze_ingot");
	public static final ResourceKey<Item> HEATED_BRONZE_INGOT = create("heated_bronze_ingot");
	public static final ResourceKey<Item> HEATED_COPPER_INGOT = create("heated_copper_ingot");
	public static final ResourceKey<Item> HEATED_IRON_INGOT = create("heated_iron_ingot");
	public static final ResourceKey<Item> BRONZE_NAIL = create("bronze_nail");
	public static final ResourceKey<Item> COPPER_NAIL = create("copper_nail");
	public static final ResourceKey<Item> BRONZE_SHAFT = create("bronze_shaft");
	public static final ResourceKey<Item> COPPER_SHAFT = create("copper_shaft");
	public static final ResourceKey<Item> BRONZE_TOOL_BLADE = create("bronze_tool_blade");
	public static final ResourceKey<Item> BRONZE_PLATE = create("bronze_plate");
	public static final ResourceKey<Item> IRON_TOOL_HEAD = create("iron_tool_head");
	public static final ResourceKey<Item> IRON_PLATE = create("iron_plate");
	public static final ResourceKey<Item> CRUCIBLE = create("crucible");
	public static final ResourceKey<Item> CLAY_JUG = create("clay_jug");

	public static final ResourceKey<Item> HAMMERSTONE = create("hammerstone");
	public static final ResourceKey<Item> FLINT_KNIFE = create("flint_knife");
	public static final ResourceKey<Item> FLINT_AXE =  create("flint_axe");
	public static final ResourceKey<Item> FLINT_SPEAR = create("flint_spear");
	public static final ResourceKey<Item> BOW_AND_DRILL =  create("bow_and_drill");

	public static final ResourceKey<Item> BONE_SWORD = create("bone_sword");
	public static final ResourceKey<Item> BONE_AXE = create("bone_axe");
	public static final ResourceKey<Item> BONE_PICKAXE = create("bone_pickaxe");
	public static final ResourceKey<Item> BONE_SHOVEL = create("bone_shovel");
	public static final ResourceKey<Item> BONE_HOE = create("bone_hoe");
	public static final ResourceKey<Item> BONE_SAW = create("bone_saw");
	public static final ResourceKey<Item> BRONZE_SWORD = create("bronze_sword");
	public static final ResourceKey<Item> BRONZE_PICKAXE = create("bronze_pickaxe");
	public static final ResourceKey<Item> BRONZE_AXE = create("bronze_axe");
	public static final ResourceKey<Item> BRONZE_HOE = create("bronze_hoe");
	public static final ResourceKey<Item> BRONZE_SHOVEL = create("bronze_shovel");
	public static final ResourceKey<Item> BRONZE_SPEAR = create("bronze_spear");
	public static final ResourceKey<Item> BRONZE_HAMMER = create("bronze_hammer");
	public static final ResourceKey<Item> IRON_HAMMER = create("iron_hammer");
	public static final ResourceKey<Item> IRON_KNIFE = create("iron_knife");
	public static final ResourceKey<Item> BRONZE_HELMET = create("bronze_helmet");
	public static final ResourceKey<Item> BRONZE_CHESTPLATE = create("bronze_chestplate");
	public static final ResourceKey<Item> BRONZE_LEGGINGS = create("bronze_leggings");
	public static final ResourceKey<Item> BRONZE_BOOTS = create("bronze_boots");
	public static final ResourceKey<Item> IRON_SPECKS = create("iron_specks");


	private ModItemIds() {
	}

	public static ResourceKey<Item> create(String name) {
		return ResourceKey.create(Registries.ITEM, FromStoneToStars.id(name));
	}
}
