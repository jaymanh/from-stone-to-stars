package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.block.entity.SnareBlockEntity;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.data.ItemData;

import net.minecraft.network.chat.Component;

public enum WthitSnareProvider implements IBlockComponentProvider, IDataProvider<SnareBlockEntity> {
	INSTANCE;

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(FstsWthitPlugin.SNARE)) {
			return;
		}
		int bait = accessor.getData().raw().getIntOr("FstsBait", 0);
		tooltip.addLine(Component.translatable(
				"tooltip.from-stone-to-stars.snare.bait",
				bait,
				SnareBlockEntity.BAIT_CAPACITY
		));
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<SnareBlockEntity> accessor, IPluginConfig config) {
		SnareBlockEntity snare = accessor.getTarget();
		data.raw().putInt("FstsBait", snare.getItem(SnareBlockEntity.BAIT_SLOT).getCount());
		data.add(ItemData.TYPE, result -> result.add(ItemData.of(config).vanilla(snare)));
	}
}
