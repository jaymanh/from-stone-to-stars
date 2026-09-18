package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.block.entity.KindlingBlockEntity;
import jaymanh.fsts.compat.tooltip.ProcessTooltips;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;

public enum WthitKindlingProvider implements IBlockComponentProvider, IDataProvider<KindlingBlockEntity> {
	INSTANCE;

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(FstsWthitPlugin.KINDLING)) {
			return;
		}
		boolean lit = accessor.getBlockState().getValue(KindlingBlock.LIT);
		ProcessTooltips.kindlingLines(lit, accessor.getData().raw()).forEach(tooltip::addLine);
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<KindlingBlockEntity> accessor, IPluginConfig config) {
		KindlingBlockEntity kindling = accessor.getTarget();
		if (kindling.isBurning()) {
			ProcessTooltips.writeKindling(data.raw(), kindling.getProcessProgress(), kindling.getProcessDuration());
		}
	}
}
