package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock.HideStage;
import jaymanh.fsts.block.entity.TanningRackBlockEntity;
import jaymanh.fsts.compat.tooltip.ProcessTooltips;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadeTanningProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(this)) {
			return;
		}
		ProcessTooltips.tanningLines(accessor.getBlockState(), accessor.getServerData()).forEach(tooltip::add);
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("tanning");
	}

	public enum Data implements IServerDataProvider<BlockAccessor> {
		INSTANCE;

		@Override
		public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
			if (accessor.getBlockEntity() instanceof TanningRackBlockEntity rack) {
				ProcessTooltips.writeTanning(tag, rack);
			}
		}

		@Override
		public boolean shouldRequestData(BlockAccessor accessor) {
			BlockState state = accessor.getBlockState();
			for (var slot : TanningRackBlock.SLOTS) {
				if (state.getValue(slot) != HideStage.EMPTY) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Identifier getUid() {
			return JadeTanningProvider.INSTANCE.getUid();
		}
	}
}
