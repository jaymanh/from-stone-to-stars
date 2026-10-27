package jaymanh.fsts.multiblock;

import java.util.List;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.multiblock.process.BrickDryingProcess;
import jaymanh.fsts.multiblock.process.CharcoalPileProcess;
import jaymanh.fsts.multiblock.process.ForgeProcess;
import jaymanh.fsts.multiblock.process.SawmillProcess;
import jaymanh.fsts.tags.ModTags;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.AABB;

public final class ModMultiblocks {
	public static final Identifier CHARCOAL_PILE_ID = FromStoneToStars.id("charcoal_pile");
	public static final Identifier BRICK_DRYING_PIT = FromStoneToStars.id("brick_drying_pit");
	public static final Identifier FORGE_ID = FromStoneToStars.id("forge");
	public static final Identifier SAWMILL_ID = FromStoneToStars.id("sawmill");

	private ModMultiblocks() {
	}

	public static void initialize() {
		ActiveMultiblocks.initialize();
		MultiblockRegistry.register(new MultiblockDefinition(
				CHARCOAL_PILE_ID,
				MultiblockPattern.builder()
						.where(1, 0, 0, BlockPredicates.is(BlockTags.LOGS), MultiblockRole.INPUT)
						.where(2, 0, 0, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(1, 0, 1, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(1, 0, -1, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.build(),
				BlockPredicates.is(ModBlocks.KINDLING),
				CharcoalPileProcess::new
		));

		MultiblockRegistry.register(new MultiblockDefinition(
				BRICK_DRYING_PIT,
				MultiblockPattern.builder()
						.where(1, 0, 0, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(-1, 0, 0, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(0, 0, 1, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(0, 0, -1, BlockPredicates.is(ModBlocks.KINDLING), MultiblockRole.ORIGIN)
						.where(1, 1, 0, BlockPredicates.is(ModTags.COAL_BLOCKS), MultiblockRole.CONSUMED)
						.where(-1, 1, 0, BlockPredicates.is(ModTags.COAL_BLOCKS), MultiblockRole.CONSUMED)
						.where(0, 1, 1, BlockPredicates.is(ModTags.COAL_BLOCKS), MultiblockRole.CONSUMED)
						.where(0, 1, -1, BlockPredicates.is(ModTags.COAL_BLOCKS), MultiblockRole.CONSUMED)
						.where(0, 1, 0, BlockPredicates.is(ModTags.UNFIRED_POTTERY), MultiblockRole.INPUT)
						.build(),
				BlockPredicates.is(ModBlocks.KINDLING),
				BrickDryingProcess::new
		));

		AssemblyRegistry.register(new AssemblyDefinition(
				FORGE_ID,
				BlockPredicates.is(ModBlocks.FORGE_HEARTH),
				new AABB(-4.0, -3.0, -4.0, 5.0, 8.0, 5.0),
				List.of(ForgeLayout.Rule.INSTANCE),
				ForgeProcess::new,
				Component.translatable("multiblock.from-stone-to-stars.forge")
		));

		AssemblyRegistry.register(new AssemblyDefinition(
				SAWMILL_ID,
				BlockPredicates.is(ModBlocks.SAW_ARBOR),
				new AABB(-21.0, -3.0, -21.0, 22.0, 4.0, 22.0),
				List.of(SawmillLayout.Rule.INSTANCE),
				SawmillProcess::new,
				Component.translatable("multiblock.from-stone-to-stars.sawmill")
		));
	}
}
