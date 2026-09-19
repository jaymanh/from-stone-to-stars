package jaymanh.fsts.multiblock;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.multiblock.process.BrickDryingProcess;
import jaymanh.fsts.multiblock.process.CharcoalPileProcess;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;

public final class ModMultiblocks {
	public static final Identifier CHARCOAL_PILE_ID = FromStoneToStars.id("charcoal_pile");
	public static final Identifier BRICK_DRYING_PIT = FromStoneToStars.id("brick_drying_pit");

	private ModMultiblocks() {
	}

	public static void initialize() {
		MultiblockRegistry.register(new MultiblockDefinition(
				CHARCOAL_PILE_ID,
				MultiblockPattern.builder()
						.where(1, 0, 0, BlockPredicates.is(BlockTags.LOGS), MultiblockRole.INPUT)
						.where(2,0,0, BlockPredicates.is(ModBlocks.KINDLING),  MultiblockRole.ORIGIN)
						.where(1,0,1, BlockPredicates.is(ModBlocks.KINDLING),  MultiblockRole.ORIGIN)
						.where(1,0,-1, BlockPredicates.is(ModBlocks.KINDLING),  MultiblockRole.ORIGIN)
						.build(),
				BlockPredicates.is(ModBlocks.KINDLING),
				CharcoalPileProcess::new
		));

		MultiblockRegistry.register(new MultiblockDefinition(
				BRICK_DRYING_PIT,
				MultiblockPattern.builder()
						.where(1,0,0, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(-1,0,0, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(0,0,1, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(0,0,-1, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(1,1,0,BlockPredicates.is(ModBlocks.CHARCOAL_BLOCK),  MultiblockRole.CONSUMED)
						.where(-1,1,0,BlockPredicates.is(ModBlocks.CHARCOAL_BLOCK),  MultiblockRole.CONSUMED)
						.where(0,1,1,BlockPredicates.is(ModBlocks.CHARCOAL_BLOCK),  MultiblockRole.CONSUMED)
						.where(0,1,-1,BlockPredicates.is(ModBlocks.CHARCOAL_BLOCK),  MultiblockRole.CONSUMED)
						.where(0,1,0,BlockPredicates.is(ModBlocks.CLAY_BRICKS),  MultiblockRole.INPUT)
						.build(),
				BlockPredicates.is(ModBlocks.KINDLING),
				BrickDryingProcess::new
		));
	}
}
