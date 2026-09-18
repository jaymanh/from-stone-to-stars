package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.compat.tooltip.PebbleTooltip;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;

public enum WthitPebbleProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(FstsWthitPlugin.PEBBLE)) {
			return;
		}
		PebbleTooltip.waterloggedLine(accessor.getBlockState()).ifPresent(tooltip::addLine);
	}
}
