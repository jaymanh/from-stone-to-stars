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
	/** Placed by {@link net.minecraft.world.item.Items#BRICK}; no separate mod item. */
	public static final Block FIRED_CLAY_BRICKS = register(
			ModBlockIds.FIRED_CLAY_BRICKS,
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
	public static final Block ZINC_ORE = register(
			ModBlockItemIds.ZINC_ORE,
			Block::new,
			BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()
	);
	public static final Block DEEPSLATE_ZINC_ORE = register(
			ModBlockItemIds.DEEPSLATE_ZINC_ORE,
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
	public static final Block WORK_STUMP = register(
			ModBlockItemIds.WORK_STUMP,
			WorkStumpBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(2.0F, 3.0F)
					.noOcclusion()
					.ignitedByLava()
	);
	public static final Block UNFINISHED_WORK_STUMP = register(
			ModBlockItemIds.UNFINISHED_WORK_STUMP,
			UnfinishedWorkStumpBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(2.0F, 3.0F)
					.noOcclusion()
					.ignitedByLava()
	);
	public static final Block UNFIRED_CLAY_JUG = register(
			ModBlockItemIds.UNFIRED_CLAY_JUG,
			UnfiredPotteryBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.CLAY)
					.sound(SoundType.GRAVEL)
					.strength(0.5F)
					.noOcclusion()
					.pushReaction(PushReaction.DESTROY)
	);
	public static final Block UNFIRED_STORAGE_POT = register(
			ModBlockItemIds.UNFIRED_STORAGE_POT,
			UnfiredPotteryBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.CLAY)
					.sound(SoundType.GRAVEL)
					.strength(0.5F)
					.noOcclusion()
					.pushReaction(PushReaction.DESTROY)
	);
	public static final Block STORAGE_POT = register(
			ModBlockItemIds.STORAGE_POT,
			StoragePotBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.TERRACOTTA_ORANGE)
					.sound(SoundType.DECORATED_POT)
					.strength(0.5F)
					.noOcclusion()
					.pushReaction(PushReaction.DESTROY)
	);
	public static final Block BRONZE_CHEST = register(
			ModBlockItemIds.BRONZE_CHEST,
			BronzeChestBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(2.5F)
					.noOcclusion()
					.ignitedByLava()
	);
	public static final Block FORGE_HEARTH = register(
			ModBlockItemIds.FORGE_HEARTH,
			ForgeHearthBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.STONE)
					.sound(SoundType.STONE)
					.strength(2.0F, 6.0F)
					.lightLevel(state -> state.getValue(ForgeHearthBlock.LIT) ? 14 : 0)
					.noOcclusion()
	);
	public static final Block BELLOWS = register(
			ModBlockItemIds.BELLOWS,
			BellowsBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(1.5F, 3.0F)
					.noOcclusion()
					.ignitedByLava()
	);
	public static final Block POWERED_BELLOWS = register(
			ModBlockItemIds.POWERED_BELLOWS,
			PoweredBellowsBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.sound(SoundType.METAL)
					.strength(3.0F, 4.8F)
					.requiresCorrectToolForDrops()
					.noOcclusion()
	);
	public static final Block FORGE_INPUT = register(
			ModBlockItemIds.FORGE_INPUT,
			ForgeInputBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.COLOR_RED)
					.sound(SoundType.METAL)
					.strength(2.5F, 6.0F)
					.requiresCorrectToolForDrops()
	);
	public static final Block TAPPING_SPOUT = register(
			ModBlockItemIds.TAPPING_SPOUT,
			TappingSpoutBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.COLOR_RED)
					.sound(SoundType.METAL)
					.strength(2.5F, 6.0F)
					.requiresCorrectToolForDrops()
					.noOcclusion()
	);
	public static final Block ORE_CRUSHER = register(
			ModBlockItemIds.ORE_CRUSHER,
			OreCrusherBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.sound(SoundType.METAL)
					.strength(3.0F, 4.8F)
					.requiresCorrectToolForDrops()
					.noOcclusion()
	);
	public static final Block FORGING_STUMP = register(
			ModBlockItemIds.FORGING_STUMP,
			ForgingStumpBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(2.5F, 3.0F)
					.noOcclusion()
					.ignitedByLava()
	);
	public static final Block WATER_WHEEL = register(
			ModBlockItemIds.WATER_WHEEL,
			WaterWheelBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(2.0F, 3.0F)
					.noOcclusion()
					.ignitedByLava()
	);
	public static final Block WOODEN_SHAFT = register(
			ModBlockItemIds.WOODEN_SHAFT,
			WoodenShaftBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(1.0F, 2.0F)
					.noOcclusion()
					.ignitedByLava()
	);
	public static final Block ROTATION_GEAR = register(
			ModBlockItemIds.ROTATION_GEAR,
			RotationGearBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(1.5F, 2.5F)
					.noOcclusion()
	);
	public static final Block POWERED_QUERN = register(
			ModBlockItemIds.POWERED_QUERN,
			PoweredQuernBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.STONE)
					.sound(SoundType.STONE)
					.strength(3.5F)
					.noOcclusion()
	);
	public static final Block TRIP_HAMMER = register(
			ModBlockItemIds.TRIP_HAMMER,
			TripHammerBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(2.0F, 3.0F)
					.noOcclusion()
					.ignitedByLava()
	);
	public static final Block BRONZE_HOPPER = register(
			ModBlockItemIds.BRONZE_HOPPER,
			BronzeHopperBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.TERRACOTTA_ORANGE)
					.sound(SoundType.METAL)
					.strength(3.0F, 4.8F)
					.requiresCorrectToolForDrops()
					.noOcclusion()
	);
	public static final Block RUSTY_MUD = register(
			ModBlockItemIds.RUSTY_MUD,
			Block::new,
			BlockBehaviour.Properties.of().strength(0.6f).sound(SoundType.GRAVEL)
			);
	public static final Block DRIED_RUSTY_MUD = register(
			ModBlockItemIds.DRIED_RUSTY_MUD,
			Block::new,
			BlockBehaviour.Properties.of().strength(0.6f).sound(SoundType.STONE)
	);
	public static final Block STEEL_SAW_BLADES = register(
			ModBlockItemIds.STEEL_SAW_BLADES,
			SteelSawBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.IRON)
					.strength(2f)
					.requiresCorrectToolForDrops()
					.noOcclusion()
	);
	public static final Block WOODEN_ROLLERS = register(
			ModBlockItemIds.WOODEN_ROLLERS,
			WoodenRollersBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(1.0F, 2.0F)
					.noOcclusion()
					.ignitedByLava()
	);
	public static final Block SAW_ARBOR = register(
			ModBlockItemIds.SAW_ARBOR,
			SawArborBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(2.0F, 3.0F)
	);
	public static final Block LOG_INTAKE = register(
			ModBlockItemIds.LOG_INTAKE,
			SawmillPortBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(1.5F, 2.0F)
					.ignitedByLava()
	);
	public static final Block PLANK_OUTFEED = register(
			ModBlockItemIds.PLANK_OUTFEED,
			SawmillPortBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.sound(SoundType.WOOD)
					.strength(1.5F, 2.0F)
					.ignitedByLava()
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
