package jaymanh.fsts.item;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItemIds {
	public static final ResourceKey<Item> RAW_LEATHER = create("raw_leather");
	public static final ResourceKey<Item> LEATHER_STRIPS = create("leather_strips");

	public static final ResourceKey<Item> HAMMERSTONE = create("hammerstone");
	public static final ResourceKey<Item> FLINT_KNIFE = create("flint_knife");

	public static final ResourceKey<Item> BONE_SWORD = create("bone_sword");
	public static final ResourceKey<Item> BONE_AXE = create("bone_axe");
	public static final ResourceKey<Item> BONE_PICKAXE = create("bone_pickaxe");
	public static final ResourceKey<Item> BONE_SHOVEL = create("bone_shovel");
	public static final ResourceKey<Item> BONE_HOE = create("bone_hoe");

	private ModItemIds() {
	}

	public static ResourceKey<Item> create(String name) {
		return ResourceKey.create(Registries.ITEM, FromStoneToStars.id(name));
	}
}
