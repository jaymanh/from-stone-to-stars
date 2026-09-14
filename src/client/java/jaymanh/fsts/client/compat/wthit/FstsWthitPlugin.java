package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.block.ModCustomBlockModels.PebbleBlock;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;

public final class FstsWthitPlugin implements IWailaPlugin {
	@Override
	public void register(IRegistrar registrar) {
		registrar.addComponent(WthitPebbleProvider.INSTANCE, TooltipPosition.BODY, PebbleBlock.class);
	}
}
