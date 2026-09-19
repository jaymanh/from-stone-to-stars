package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.compat.tooltip.ProcessTooltips;
import jaymanh.fsts.multiblock.ActiveMultiblocks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * Progress overlay for non-kindling multiblock members (logs, planks, clay, …).
 * Kindling uses {@link JadeKindlingProvider}.
 */
public enum JadeMultiblockProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(this) || accessor.getBlock() instanceof KindlingBlock) {
			return;
		}
		ProcessTooltips.progressLines(accessor.getServerData()).forEach(tooltip::add);
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("multiblock");
	}

	static boolean isLikelyMember(BlockState state) {
		return state.is(BlockTags.LOGS)
				|| state.is(BlockTags.PLANKS)
				|| state.is(ModBlocks.CLAY_BRICKS)
				|| state.is(ModBlocks.FIRED_CLAY_BRICKS)
				|| state.is(ModBlocks.CHARCOAL_BLOCK);
	}

	public enum Data implements IServerDataProvider<BlockAccessor> {
		INSTANCE;

		@Override
		public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
			if (accessor.getBlock() instanceof KindlingBlock) {
				return;
			}
			ActiveMultiblocks.get(accessor.getLevel(), accessor.getPosition()).ifPresent(formed ->
					ProcessTooltips.writeKindling(tag, formed.getProcessProgress(), formed.getProcessDuration())
			);
		}

		@Override
		public boolean shouldRequestData(BlockAccessor accessor) {
			return !(accessor.getBlock() instanceof KindlingBlock) && isLikelyMember(accessor.getBlockState());
		}

		@Override
		public Identifier getUid() {
			return JadeMultiblockProvider.INSTANCE.getUid();
		}
	}
}
