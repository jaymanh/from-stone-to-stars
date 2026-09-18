package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.entity.PlacedBoneSawBlockEntity;
import jaymanh.fsts.compat.tooltip.ProcessTooltips;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadeBoneSawProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(this)) {
			return;
		}
		ProcessTooltips.boneSawLines(accessor.getServerData()).forEach(tooltip::add);
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("bone_saw");
	}

	public enum Data implements IServerDataProvider<BlockAccessor> {
		INSTANCE;

		@Override
		public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
			if (accessor.getBlockEntity() instanceof PlacedBoneSawBlockEntity saw) {
				ProcessTooltips.writeBoneSaw(tag, saw);
			}
		}

		@Override
		public Identifier getUid() {
			return JadeBoneSawProvider.INSTANCE.getUid();
		}
	}
}
