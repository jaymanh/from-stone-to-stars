package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.block.entity.AbstractRotationBlockEntity;
import jaymanh.fsts.rotation.RotationStatus;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum WthitRotationProvider implements IBlockComponentProvider, IDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(FstsWthitPlugin.ROTATION)) {
			return;
		}
		String status = accessor.getData().raw().getStringOr("FstsRotationStatus", RotationStatus.DISCONNECTED.name());
		tooltip.addLine(Component.translatable("tooltip.from-stone-to-stars.rotation." + status.toLowerCase()));
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<BlockEntity> accessor, IPluginConfig config) {
		if (accessor.getTarget() instanceof AbstractRotationBlockEntity rotation) {
			data.raw().putString("FstsRotationStatus", rotation.getRotationStatus().name());
		}
	}
}
