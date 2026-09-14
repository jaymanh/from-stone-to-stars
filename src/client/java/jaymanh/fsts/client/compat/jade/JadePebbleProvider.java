package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.compat.tooltip.PebbleTooltip;

import net.minecraft.resources.Identifier;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadePebbleProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		PebbleTooltip.waterloggedLine(accessor.getBlockState()).ifPresent(tooltip::add);
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("pebble");
	}
}
