package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.block.entity.PlacedBoneSawBlockEntity;
import jaymanh.fsts.compat.tooltip.ProcessTooltips;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;

public enum WthitBoneSawProvider implements IBlockComponentProvider, IDataProvider<PlacedBoneSawBlockEntity> {
	INSTANCE;

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(FstsWthitPlugin.BONE_SAW)) {
			return;
		}
		ProcessTooltips.boneSawLines(accessor.getData().raw()).forEach(tooltip::addLine);
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<PlacedBoneSawBlockEntity> accessor, IPluginConfig config) {
		ProcessTooltips.writeBoneSaw(data.raw(), accessor.getTarget());
	}
}
