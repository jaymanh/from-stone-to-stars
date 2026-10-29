package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.compat.tooltip.MachineTooltips;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;

public enum WthitForgeProvider implements IBlockComponentProvider, IDataProvider<ForgeHearthBlockEntity> {
	INSTANCE;

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(FstsWthitPlugin.FORGE)) {
			return;
		}
		MachineTooltips.forgeLines(accessor.getBlockState(), accessor.getData().raw(), accessor.getWorld().registryAccess())
				.forEach(tooltip::addLine);
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<ForgeHearthBlockEntity> accessor, IPluginConfig config) {
		MachineTooltips.writeForge(data.raw(), accessor.getTarget(), accessor.getLevel().registryAccess());
	}
}
