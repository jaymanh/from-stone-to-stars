package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.block.entity.TanningRackBlockEntity;
import jaymanh.fsts.compat.tooltip.ProcessTooltips;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;

public enum WthitTanningProvider implements IBlockComponentProvider, IDataProvider<TanningRackBlockEntity> {
	INSTANCE;

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(FstsWthitPlugin.TANNING)) {
			return;
		}
		ProcessTooltips.tanningLines(accessor.getBlockState(), accessor.getData().raw()).forEach(tooltip::addLine);
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<TanningRackBlockEntity> accessor, IPluginConfig config) {
		ProcessTooltips.writeTanning(data.raw(), accessor.getTarget());
	}
}
