package jaymanh.fsts.item;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public final class ModItems {
	public static final Item PLANT_FIBER = register(ModItemIds.PLANT_FIBER, Item::new, new Item.Properties());
	public static final Item RAW_LEATHER = register(ModItemIds.RAW_LEATHER, Item::new, new Item.Properties());
	public static final Item LEATHER_STRIPS = register(ModItemIds.LEATHER_STRIPS, Item::new, new Item.Properties());
	public static final Item BARK = register(ModItemIds.BARK, Item::new, new Item.Properties());
	public static final Item SHARPENED_BONE = register(ModItemIds.SHARPENED_BONE, Item::new, new Item.Properties());
	public static final Item FLOUR = register(ModItemIds.FLOUR, Item::new, new Item.Properties());
	public static final Item DOUGH = register(ModItemIds.DOUGH, Item::new, new Item.Properties());
	public static final Item RAW_ZINC = register(ModItemIds.RAW_ZINC, Item::new, new Item.Properties());
	public static final Item CRUSHED_IRON = register(ModItemIds.CRUSHED_IRON, Item::new, new Item.Properties());
	public static final Item CRUSHED_COPPER = register(ModItemIds.CRUSHED_COPPER, Item::new, new Item.Properties());
	public static final Item CRUSHED_GOLD = register(ModItemIds.CRUSHED_GOLD, Item::new, new Item.Properties());
	public static final Item CRUSHED_ZINC = register(ModItemIds.CRUSHED_ZINC, Item::new, new Item.Properties());
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
	public static final Item HEATED_IRON_INGOT = register(
			ModItemIds.HEATED_IRON_INGOT,
			properties -> new HeatedMetalItem(properties, () -> Items.IRON_INGOT),
			new Item.Properties()
	);
	public static final Item BRONZE_NAIL = register(ModItemIds.BRONZE_NAIL, Item::new, new Item.Properties());
	public static final Item COPPER_NAIL = register(ModItemIds.COPPER_NAIL, Item::new, new Item.Properties());
	public static final Item BRONZE_SHAFT = register(ModItemIds.BRONZE_SHAFT, Item::new, new Item.Properties());
	public static final Item COPPER_SHAFT = register(ModItemIds.COPPER_SHAFT, Item::new, new Item.Properties());
	public static final Item BRONZE_TOOL_BLADE = register(ModItemIds.BRONZE_TOOL_BLADE, Item::new, new Item.Properties());
	public static final Item BRONZE_PLATE = register(ModItemIds.BRONZE_PLATE, Item::new, new Item.Properties());
	public static final Item BRONZE_BUCKET = register(
			ModItemIds.BRONZE_BUCKET,
			properties -> new BronzeBucketItem(Fluids.EMPTY, properties),
			new Item.Properties().stacksTo(16)
	);
	public static final Item BRONZE_WATER_BUCKET = register(
			ModItemIds.BRONZE_WATER_BUCKET,
			properties -> new BronzeBucketItem(Fluids.WATER, properties),
			new Item.Properties().craftRemainder(BRONZE_BUCKET).stacksTo(1)
	);
	public static final Item BRONZE_LAVA_BUCKET = register(
			ModItemIds.BRONZE_LAVA_BUCKET,
			properties -> new BronzeBucketItem(Fluids.LAVA, properties),
			new Item.Properties().craftRemainder(BRONZE_BUCKET).stacksTo(1)
	);
	public static final Item BRONZE_MILK_BUCKET = register(
			ModItemIds.BRONZE_MILK_BUCKET,
			Item::new,
			new Item.Properties()
					.craftRemainder(BRONZE_BUCKET)
					.component(DataComponents.CONSUMABLE, Consumables.MILK_BUCKET)
					.usingConvertsTo(BRONZE_BUCKET)
					.stacksTo(1)
	);
	public static final Item BRONZE_POWDER_SNOW_BUCKET = register(
			ModItemIds.BRONZE_POWDER_SNOW_BUCKET,
			properties -> new SolidBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, properties),
			new Item.Properties().stacksTo(1).useItemDescriptionPrefix()
	);
	public static final Item BRONZE_COD_BUCKET = fishBucket(ModItemIds.BRONZE_COD_BUCKET, EntityTypes.COD, Foods.COD);
	public static final Item BRONZE_SALMON_BUCKET = fishBucket(ModItemIds.BRONZE_SALMON_BUCKET, EntityTypes.SALMON, Foods.SALMON);
	public static final Item BRONZE_TROPICAL_FISH_BUCKET = fishBucket(
			ModItemIds.BRONZE_TROPICAL_FISH_BUCKET, EntityTypes.TROPICAL_FISH, Foods.TROPICAL_FISH
	);
	public static final Item BRONZE_PUFFERFISH_BUCKET = fishBucket(
			ModItemIds.BRONZE_PUFFERFISH_BUCKET, EntityTypes.PUFFERFISH, Foods.PUFFERFISH
	);
	public static final Item BRONZE_TADPOLE_BUCKET = creatureBucket(
			ModItemIds.BRONZE_TADPOLE_BUCKET, EntityTypes.TADPOLE, Fluids.WATER, SoundEvents.BUCKET_EMPTY_TADPOLE
	);
	public static final Item BRONZE_AXOLOTL_BUCKET = creatureBucket(
			ModItemIds.BRONZE_AXOLOTL_BUCKET, EntityTypes.AXOLOTL, Fluids.WATER, SoundEvents.BUCKET_EMPTY_AXOLOTL
	);
	public static final Item BRONZE_SULFUR_CUBE_BUCKET = creatureBucket(
			ModItemIds.BRONZE_SULFUR_CUBE_BUCKET, EntityTypes.SULFUR_CUBE, Fluids.EMPTY, SoundEvents.BUCKET_EMPTY_SULFUR_CUBE
	);
	public static final Item IRON_TOOL_HEAD = register(ModItemIds.IRON_TOOL_HEAD, Item::new, new Item.Properties());
	public static final Item IRON_PLATE = register(ModItemIds.IRON_PLATE, Item::new, new Item.Properties());
	public static final Item CRUCIBLE = register(
			ModItemIds.CRUCIBLE,
			CrucibleItem::new,
			new Item.Properties().durability(CrucibleItem.MAX_USES)
	);
	public static final Item BRONZE_REINFORCED_CRUCIBLE = register(
			ModItemIds.BRONZE_REINFORCED_CRUCIBLE,
			CrucibleItem::new,
			new Item.Properties().durability((CrucibleItem.MAX_USES * 4))
	);
	public static final Item CLAY_JUG = register(
			ModItemIds.CLAY_JUG,
			ClayJugItem::new,
			new Item.Properties()
					.durability(ClayJugItem.CAPACITY + 1)
					.component(net.minecraft.core.component.DataComponents.DAMAGE, ClayJugItem.CAPACITY)
	);
	public static final Item IRON_SPECKS = register(ModItemIds.IRON_SPECKS, Item::new, new Item.Properties());
	public static final Item STEEL_INGOT = register(ModItemIds.STEEL_INGOT, Item::new, new Item.Properties());

	private ModItems() {
	}

	public static <T extends Item> T register(ResourceKey<Item> itemKey, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
		T item = itemFactory.apply(settings.setId(itemKey));
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);
		return item;
	}

	public static void initialize() {
		BronzeBucketBehaviors.registerDispensers();
	}

	private static Item fishBucket(ResourceKey<Item> key, EntityType<? extends Mob> type, FoodProperties food) {
		return register(
				key,
				properties -> new MobBucketItem(type, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties),
				new Item.Properties()
						.stacksTo(1)
						.component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
						.component(DataComponents.FOOD, food)
		);
	}

	private static Item creatureBucket(ResourceKey<Item> key, EntityType<? extends Mob> type, Fluid fluid, SoundEvent sound) {
		return register(
				key,
				properties -> new MobBucketItem(type, fluid, sound, properties),
				new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
		);
	}
}
