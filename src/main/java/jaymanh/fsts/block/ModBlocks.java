package jaymanh.fsts.block;

import java.util.function.Function;

import jaymanh.fsts.block.ModCustomBlockModels.PebbleBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PlacedBoneSawBlock;
import jaymanh.fsts.block.ModCustomBlockModels.SticksBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
			Block::new,
			BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(2.0F, 3.0F).ignitedByLava()
	);

	public static final Block PLACED_BONE_SAW = register(
			ModBlockIds.PLACED_BONE_SAW,
			PlacedBoneSawBlock::new,
			BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(0.5F).noOcclusion().pushReaction(PushReaction.DESTROY)
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

	public static void initialize() {
	}
}
