package jaymanh.fsts.tags;

import jaymanh.fsts.block.ModBlocks;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import static jaymanh.fsts.FromStoneToStars.MOD_ID;

public class ModTags {
    public static final TagKey<Block> FSTS_WOOD_FLAMMABLE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "fsts_wood_flammable"));
    public static final TagKey<Block> FSTS_CHARCOAL_FLAMMABLE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "fsts_charcoal_flammable"));

    public static final TagKey<Block> FSTS_NEEDS_BONE_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "needs_bone_tool"));
    public static final TagKey<Block> FSTS_NEEDS_FLINT_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "needs_flint_tool"));
    public static final TagKey<Block> FSTS_NEEDS_BRONZE_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "needs_bronze_tool"));
    public static final TagKey<Block> FSTS_INCORRECT_FOR_FLINT_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "incorrect_for_flint_tool"));
    public static final TagKey<Block> FSTS_INCORRECT_FOR_BONE_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "incorrect_for_bone_tool"));
    public static final TagKey<Block> FSTS_INCORRECT_FOR_BRONZE_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "incorrect_for_bronze_tool"));

    public static final TagKey<EntityType<?>> DROPS_BONES = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "drops_bones"));
    public static final ResourceKey<LootTable> BONES_INJECT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(MOD_ID, "inject/bones"));

    public static final TagKey<Block> UNFIRED_POTTERY = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "unfired_pottery"));
    public static final TagKey<Block> COAL_BLOCKS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "coal_blocks"));
    public static final TagKey<Item> COAL_BLOCK_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "coal_blocks"));
	/** Bricks / cobble / etc. used as forge shell and chimney. */
	public static final TagKey<Block> FORGE_FRAME = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "forge_frame"));
	/** Side columns and top beam around the sawmill blade wall. */
	public static final TagKey<Block> SAWMILL_FRAME = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "sawmill_frame"));
	/** Ores the bore rig mines when a vein touches its hole. No diamond, emerald, or rusty mud. */
	public static final TagKey<Block> DRILL_ORES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "drill_ores"));

    public static final TagKey<Item> WATER_VESSELS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "water_vessels"));

	/** Copper or bronze nails — interchangeable in recipes. */
	public static final TagKey<Item> NAILS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "nails"));
	/** Hammers that strike a forging stump. */
	public static final TagKey<Item> FORGING_HAMMERS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "forging_hammers"));
	/** Knives that return damaged from a craft, such as cutting leather strips. */
	public static final TagKey<Item> KNIVES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "knives"));
	/** Copper or bronze shafts — interchangeable in recipes. */
	public static final TagKey<Item> SHAFTS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "shafts"));

	/**
	 * Items the forging stump slot accepts on the client. The client does not receive forging recipes,
	 * so this tag mirrors the recipe ingredients. The server still checks the recipes themselves.
	 */
	public static final TagKey<Item> FORGING_INGREDIENTS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "forging_ingredients"));
	/** Results that mark a forging recipe as a crush recipe, which the ore crusher also runs. */
	public static final TagKey<Item> CRUSHED_ORES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "crushed_ores"));

    public static final TagKey<Item> SNARE_MEAT_BAIT = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "snare_meat_bait"));
    public static final TagKey<Item> SNARE_PLANT_BAIT = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "snare_plant_bait"));
    public static final ResourceKey<LootTable> SNARE_MEAT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(MOD_ID, "gameplay/snare_meat"));
    public static final ResourceKey<LootTable> SNARE_PLANT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(MOD_ID, "gameplay/snare_plant"));

    public static final TagKey<Item> FISHING_BAIT = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "fishing_bait"));
    public static final ResourceKey<LootTable> FISH_TRAP_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(MOD_ID, "gameplay/fish_trap"));


    public static void initialize() {
        FlammableBlockRegistry.getDefaultInstance().add(FSTS_WOOD_FLAMMABLE, 25, 10);
        FlammableBlockRegistry.getDefaultInstance().add(FSTS_CHARCOAL_FLAMMABLE, 5, 5);

        FuelValueEvents.BUILD.register((builder, context) ->
                builder.add(ModBlocks.CHARCOAL_BLOCK, context.baseSmeltTime() * 8 * 5));

        LootTableEvents.MODIFY_DROPS.register((holder, context, drops) -> {
            if (holder.unwrapKey().filter(BONES_INJECT::equals).isPresent()) {
                return;
            }

            Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
            if (entity == null || !entity.getType().builtInRegistryHolder().is(DROPS_BONES)) {
                return;
            }

            context.getResolver().get(BONES_INJECT).ifPresent(inject ->
                    inject.value().getRandomItems(context, drops::add));
        });
    }
}
