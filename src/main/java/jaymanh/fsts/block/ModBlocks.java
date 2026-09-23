package jaymanh.fsts.block;

import java.util.function.Function;

import jaymanh.fsts.block.ModCustomBlockModels.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class ModBlocks {
	public static final Block PEBBLE = register(
			ModBlockItemIds.PEBBLE,
			PebbleBlock::new,
			BlockBehaviour.Properties.of().noCollision().sound(SoundType.STONE).instabreak().explosionResistance(0).pushReaction(PushReaction.DESTROY).replaceable()
			);
	public static final Block STICKS = register(
			ModBlockItemIds.STICKS,
			SticksBlock::new,
			BlockBehaviour.Properties.of().noCollision().sound(SoundType.WOOD).instabreak().explosionResistance(0).pushReaction(PushReaction.DESTROY).replaceable().ignitedByLava()
			);
	public static final Block TANNING_RACK = register(
			ModBlockItemIds.TANNING_RACK,
			TanningRackBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(2.0F).pushReaction(PushReaction.DESTROY)
			);

	public static final Block KINDLING = register(
			ModBlockItemIds.KINDLING,
			KindlingBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(2.0F, 3.0F).ignitedByLava().lightLevel(state -> state.getValue(KindlingBlock.LIT) ? 10 : 0)
	);

	public static final Block CHARCOAL_BLOCK = register(
			ModBlockItemIds.CHARCOAL_BLOCK,
			Block::new,
			BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).sound(SoundType.STONE).strength(3.0F, 6.0F)
	);

	public static final Block PLACED_BONE_SAW = register(
			ModBlockIds.PLACED_BONE_SAW,
			PlacedBoneSawBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(0.5F).noOcclusion().pushReaction(PushReaction.DESTROY)
	);

	public static final Block CLAY_BRICKS = register(
			ModBlockItemIds.CLAY_BRICKS,
			ClayBricksBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).strength(0.5F).noOcclusion().pushReaction(PushReaction.DESTROY)
	);
	public static final Block FIRED_CLAY_BRICKS = register(
			ModBlockItemIds.FIRED_CLAY_BRICKS,
			ClayBricksBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(0.5F).noOcclusion().pushReaction(PushReaction.DESTROY)
	);
	public static final Block ROPE = register(
			ModBlockItemIds.ROPE,
			RopeBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.LADDER).strength(0.5f, 1f).noOcclusion().pushReaction(PushReaction.DESTROY).noCollision()
	);
	public static final Block BASIC_SMELTER = register(
			ModBlockItemIds.BASIC_SMELTER,
			BasicSmelter::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.TERRACOTTA_ORANGE)
					.sound(SoundType.STONE)
					.strength(3.5F)
					.requiresCorrectToolForDrops()
					.lightLevel(state -> state.getValue(BasicSmelter.LIT) ? 13 : 0)
	);
	public static final Block MORTAR_AND_PESTLE = register(
			ModBlockItemIds.MORTAR_AND_PESTLE,
			MortarAndPestle::new,
			BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(3.5f).noOcclusion()
	);
	public static final Block SNARE = register(
			ModBlockItemIds.SNARE,
			SnareBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(0.5F).noOcclusion().pushReaction(PushReaction.DESTROY).ignitedByLava()
	);
	public static final Block FISH_TRAP = register(
			ModBlockItemIds.FISH_TRAP,
			FishTrapBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(0.5f).noOcclusion().pushReaction(PushReaction.DESTROY)
	);
	public static final Block TIN_ORE = register(
			ModBlockItemIds.TIN_ORE,
			Block::new,
			BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()
	);
	public static final Block DEEPSLATE_TIN_ORE = register(
			ModBlockItemIds.DEEPSLATE_TIN_ORE,
			Block::new,
			BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(4.5F, 3.0F).requiresCorrectToolForDrops()
	);
	public static final Block HIDE_BED = registerBed(
			ModBlockItemIds.HIDE_BED,
			HideBedBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(state -> state.getValue(BedBlock.PART) == BedPart.FOOT ? MapColor.COLOR_BROWN : MapColor.WOOL)
					.sound(SoundType.WOOD)
					.strength(0.2F)
					.bounceRestitution(0.75F)
					.noOcclusion()
					.ignitedByLava()
					.pushReaction(PushReaction.DESTROY)
	);
	public static final Block THATCH = register(
			ModBlockItemIds.THATCH,
			Block::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.COLOR_YELLOW)
					.sound(SoundType.GRASS)
					.strength(0.5F)
					.ignitedByLava()
	);
	public static final Block WATTLE_AND_DAUB = register(
			ModBlockItemIds.WATTLE_AND_DAUB,
			Block::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
					.sound(SoundType.GRAVEL)
					.strength(1.0F)
	);

	private ModBlocks() {
	}

	public static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
		Block block = blockFactory.apply(properties.setId(id));
		return Registry.register(BuiltInRegistries.BLOCK, id, block);
	}

	public static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
		Block block = register(id.block(), blockFactory, properties);
		BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
		Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);
		return block;
	}

	public static Block registerBed(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
		Block block = register(id.block(), blockFactory, properties);
		BedItem bedItem = new BedItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
		Registry.register(BuiltInRegistries.ITEM, id.item(), bedItem);
		return block;
	}

	public static void initialize() {
	}
}
