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
