package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.block.entity.KindlingBlockEntity;
import jaymanh.fsts.compat.tooltip.ProcessTooltips;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadeKindlingProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(this)) {
			return;
		}
		boolean lit = accessor.getBlockState().getValue(KindlingBlock.LIT);
		ProcessTooltips.kindlingLines(lit, accessor.getServerData()).forEach(tooltip::add);
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("kindling");
	}

	public enum Data implements IServerDataProvider<BlockAccessor> {
		INSTANCE;

		@Override
		public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
			if (accessor.getBlockEntity() instanceof KindlingBlockEntity kindling && kindling.isBurning()) {
				ProcessTooltips.writeKindling(tag, kindling.getProcessProgress(), kindling.getProcessDuration());
			}
		}

		@Override
		public boolean shouldRequestData(BlockAccessor accessor) {
			return accessor.getBlockState().getValue(KindlingBlock.LIT);
		}

		@Override
		public Identifier getUid() {
			return JadeKindlingProvider.INSTANCE.getUid();
		}
	}
}
