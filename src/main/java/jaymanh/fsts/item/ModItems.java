package jaymanh.fsts.item;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItems {
	public static final Item PLANT_FIBER = register(ModItemIds.PLANT_FIBER, Item::new, new Item.Properties());
	public static final Item RAW_LEATHER = register(ModItemIds.RAW_LEATHER, Item::new, new Item.Properties());
	public static final Item LEATHER_STRIPS = register(ModItemIds.LEATHER_STRIPS, Item::new, new Item.Properties());

	private ModItems() {
	}

	public static <T extends Item> T register(ResourceKey<Item> itemKey, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
		T item = itemFactory.apply(settings.setId(itemKey));
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);
		return item;
	}

	public static void initialize() {
	}
}
