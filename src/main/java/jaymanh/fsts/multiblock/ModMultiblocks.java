package jaymanh.fsts.multiblock;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.multiblock.process.CharcoalPileProcess;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;

public final class ModMultiblocks {
	public static final Identifier CHARCOAL_PILE_ID = FromStoneToStars.id("charcoal_pile");

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
	}
}
