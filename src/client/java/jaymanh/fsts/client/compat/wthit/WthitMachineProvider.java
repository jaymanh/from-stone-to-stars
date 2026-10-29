package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.compat.tooltip.MachineTooltips;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;

import net.minecraft.world.level.block.entity.BlockEntity;

/** Ironworks fittings, the ore crusher, and the sawmill. */
public enum WthitMachineProvider implements IBlockComponentProvider, IDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(FstsWthitPlugin.MACHINES)) {
			return;
		}
		MachineTooltips.machineLines(accessor.getData().raw(), accessor.getWorld().registryAccess()).forEach(tooltip::addLine);
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<BlockEntity> accessor, IPluginConfig config) {
		MachineTooltips.writeMachine(data.raw(), accessor.getTarget(), accessor.getLevel().registryAccess());
	}
}
