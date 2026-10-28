package jaymanh.fsts.block.entity;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModBlocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {
	public static final BlockEntityType<TanningRackBlockEntity> TANNING_RACK = register(
			"tanning_rack",
			TanningRackBlockEntity::new,
			ModBlocks.TANNING_RACK
	);

	public static final BlockEntityType<PlacedBoneSawBlockEntity> PLACED_BONE_SAW = register(
			"placed_bone_saw",
			PlacedBoneSawBlockEntity::new,
			ModBlocks.PLACED_BONE_SAW
	);

	public static final BlockEntityType<KindlingBlockEntity> KINDLING = register(
			"kindling",
			KindlingBlockEntity::new,
			ModBlocks.KINDLING
	);

	public static final BlockEntityType<BasicSmelterEntity> BASIC_SMELTER = register(
			"basic_smelter",
			BasicSmelterEntity::new,
			ModBlocks.BASIC_SMELTER
	);

	public static final BlockEntityType<MortarAndPestleEntity> MORTAR_AND_PESTLE = register(
			"mortar_and_pestle",
			MortarAndPestleEntity::new,
			ModBlocks.MORTAR_AND_PESTLE
	);

	public static final BlockEntityType<SnareBlockEntity> SNARE = register(
			"snare",
			SnareBlockEntity::new,
			ModBlocks.SNARE
	);

	public static final BlockEntityType<FishTrapEntity> FISH_TRAP = register(
			"fish_trap",
			FishTrapEntity::new,
			ModBlocks.FISH_TRAP
	);

	public static final BlockEntityType<StoragePotBlockEntity> STORAGE_POT = register(
			"storage_pot",
			StoragePotBlockEntity::new,
			ModBlocks.STORAGE_POT
	);

	public static final BlockEntityType<BronzeChestBlockEntity> BRONZE_CHEST = register(
			"bronze_chest",
			BronzeChestBlockEntity::new,
			ModBlocks.BRONZE_CHEST
	);

	public static final BlockEntityType<ForgeHearthBlockEntity> FORGE_HEARTH = register(
			"forge_hearth",
			ForgeHearthBlockEntity::new,
			ModBlocks.FORGE_HEARTH
	);

	public static final BlockEntityType<ForgingStumpEntity> FORGING_STUMP = register(
			"forging_stump",
			ForgingStumpEntity::new,
			ModBlocks.FORGING_STUMP
	);

	public static final BlockEntityType<WaterWheelBlockEntity> WATER_WHEEL = register(
			"water_wheel",
			WaterWheelBlockEntity::new,
			ModBlocks.WATER_WHEEL
	);

	public static final BlockEntityType<WoodenShaftBlockEntity> WOODEN_SHAFT = register(
			"wooden_shaft",
			WoodenShaftBlockEntity::new,
			ModBlocks.WOODEN_SHAFT
	);

	public static final BlockEntityType<RotationGearBlockEntity> ROTATION_GEAR = register(
			"rotation_gear",
			RotationGearBlockEntity::new,
			ModBlocks.ROTATION_GEAR
	);

	public static final BlockEntityType<PoweredQuernBlockEntity> POWERED_QUERN = register(
			"powered_quern",
			PoweredQuernBlockEntity::new,
			ModBlocks.POWERED_QUERN
	);

	public static final BlockEntityType<TripHammerBlockEntity> TRIP_HAMMER = register(
			"trip_hammer",
			TripHammerBlockEntity::new,
			ModBlocks.TRIP_HAMMER
	);

	public static final BlockEntityType<BronzeHopperBlockEntity> BRONZE_HOPPER = register(
			"bronze_hopper",
			BronzeHopperBlockEntity::new,
			ModBlocks.BRONZE_HOPPER
	);

	public static final BlockEntityType<PoweredBellowsBlockEntity> POWERED_BELLOWS = register(
			"powered_bellows",
			PoweredBellowsBlockEntity::new,
			ModBlocks.POWERED_BELLOWS
	);

	public static final BlockEntityType<ForgeInputBlockEntity> FORGE_INPUT = register(
			"forge_input",
			ForgeInputBlockEntity::new,
			ModBlocks.FORGE_INPUT
	);

	public static final BlockEntityType<TappingSpoutBlockEntity> TAPPING_SPOUT = register(
			"tapping_spout",
			TappingSpoutBlockEntity::new,
			ModBlocks.TAPPING_SPOUT
	);

	public static final BlockEntityType<OreCrusherBlockEntity> ORE_CRUSHER = register(
			"ore_crusher",
			OreCrusherBlockEntity::new,
			ModBlocks.ORE_CRUSHER
	);

	public static final BlockEntityType<SawArborBlockEntity> SAW_ARBOR = register(
			"saw_arbor",
			SawArborBlockEntity::new,
			ModBlocks.SAW_ARBOR
	);

	public static final BlockEntityType<SawmillPortBlockEntity> SAWMILL_PORT = Registry.register(
			BuiltInRegistries.BLOCK_ENTITY_TYPE,
			FromStoneToStars.id("sawmill_port"),
			FabricBlockEntityTypeBuilder.<SawmillPortBlockEntity>create(
					SawmillPortBlockEntity::new,
					ModBlocks.LOG_INTAKE,
					ModBlocks.PLANK_OUTFEED
			).build()
	);

	private ModBlockEntities() {
	}

	private static <T extends BlockEntity> BlockEntityType<T> register(
			String name,
			FabricBlockEntityTypeBuilder.Factory<? extends T> factory,
			net.minecraft.world.level.block.Block block
	) {
		return Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				FromStoneToStars.id(name),
				FabricBlockEntityTypeBuilder.<T>create(factory, block).build()
		);
	}

	public static void initialize() {
	}
}
