package jaymanh.fsts.item;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class ModItems {
	public static final Item PLANT_FIBER = register(ModItemIds.PLANT_FIBER, Item::new, new Item.Properties());
	public static final Item RAW_LEATHER = register(ModItemIds.RAW_LEATHER, Item::new, new Item.Properties());
	public static final Item LEATHER_STRIPS = register(ModItemIds.LEATHER_STRIPS, Item::new, new Item.Properties());
	public static final Item BARK = register(ModItemIds.BARK, Item::new, new Item.Properties());
	public static final Item SHARPENED_BONE = register(ModItemIds.SHARPENED_BONE, Item::new, new Item.Properties());
	public static final Item FLOUR = register(ModItemIds.FLOUR, Item::new, new Item.Properties());
	public static final Item DOUGH = register(ModItemIds.DOUGH, Item::new, new Item.Properties());
	public static final Item RAW_ZINC = register(ModItemIds.RAW_ZINC, Item::new, new Item.Properties());
	public static final Item ZINC_INGOT = register(ModItemIds.ZINC_INGOT, Item::new, new Item.Properties());
	public static final Item BRONZE_INGOT = register(ModItemIds.BRONZE_INGOT, Item::new, new Item.Properties());
	public static final Item HEATED_BRONZE_INGOT = register(
			ModItemIds.HEATED_BRONZE_INGOT,
			properties -> new HeatedMetalItem(properties, () -> BRONZE_INGOT),
			new Item.Properties()
	);
	public static final Item HEATED_COPPER_INGOT = register(
			ModItemIds.HEATED_COPPER_INGOT,
			properties -> new HeatedMetalItem(properties, () -> Items.COPPER_INGOT),
			new Item.Properties()
	);
	public static final Item BRONZE_NAIL = register(ModItemIds.BRONZE_NAIL, Item::new, new Item.Properties());
	public static final Item COPPER_NAIL = register(ModItemIds.COPPER_NAIL, Item::new, new Item.Properties());
	public static final Item BRONZE_SHAFT = register(ModItemIds.BRONZE_SHAFT, Item::new, new Item.Properties());
	public static final Item COPPER_SHAFT = register(ModItemIds.COPPER_SHAFT, Item::new, new Item.Properties());
	public static final Item BRONZE_TOOL_BLADE = register(ModItemIds.BRONZE_TOOL_BLADE, Item::new, new Item.Properties());
	public static final Item BRONZE_PLATE = register(ModItemIds.BRONZE_PLATE, Item::new, new Item.Properties());
	public static final Item CRUCIBLE = register(
			ModItemIds.CRUCIBLE,
			CrucibleItem::new,
			new Item.Properties().durability(CrucibleItem.MAX_USES)
	);
	public static final Item CLAY_JUG = register(
			ModItemIds.CLAY_JUG,
			ClayJugItem::new,
			new Item.Properties()
					.durability(ClayJugItem.CAPACITY + 1)
					.component(net.minecraft.core.component.DataComponents.DAMAGE, ClayJugItem.CAPACITY)
	);

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
