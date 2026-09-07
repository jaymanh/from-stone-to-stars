package jaymanh.fsts.item;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItemIds {
	public static final ResourceKey<Item> RAW_LEATHER = create("raw_leather");

	private ModItemIds() {
	}

	public static ResourceKey<Item> create(String name) {
		return ResourceKey.create(Registries.ITEM, FromStoneToStars.id(name));
	}
}
