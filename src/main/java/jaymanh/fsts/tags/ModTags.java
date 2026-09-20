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
    public static final TagKey<Block> FSTS_INCORRECT_FOR_FLINT_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "incorrect_for_flint_tool"));
    public static final TagKey<Block> FSTS_INCORRECT_FOR_BONE_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "incorrect_for_bone_tool"));

    public static final TagKey<EntityType<?>> DROPS_BONES = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "drops_bones"));
    public static final ResourceKey<LootTable> BONES_INJECT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(MOD_ID, "inject/bones"));

    public static final TagKey<Item> SNARE_MEAT_BAIT = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "snare_meat_bait"));
    public static final TagKey<Item> SNARE_PLANT_BAIT = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "snare_plant_bait"));
    public static final ResourceKey<LootTable> SNARE_MEAT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(MOD_ID, "gameplay/snare_meat"));
    public static final ResourceKey<LootTable> SNARE_PLANT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(MOD_ID, "gameplay/snare_plant"));

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
