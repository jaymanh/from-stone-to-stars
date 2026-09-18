package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PebbleBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PlacedBoneSawBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock;
import jaymanh.fsts.block.entity.KindlingBlockEntity;
import jaymanh.fsts.block.entity.PlacedBoneSawBlockEntity;
import jaymanh.fsts.block.entity.TanningRackBlockEntity;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;

import net.minecraft.resources.Identifier;

public final class FstsWthitPlugin implements IWailaPlugin {
	public static final Identifier PEBBLE = FromStoneToStars.id("pebble");
	public static final Identifier TANNING = FromStoneToStars.id("tanning");
	public static final Identifier KINDLING = FromStoneToStars.id("kindling");
	public static final Identifier BONE_SAW = FromStoneToStars.id("bone_saw");

	@Override
	public void register(IRegistrar registrar) {
		registrar.addConfig(PEBBLE, true);
		registrar.addConfig(TANNING, true);
		registrar.addConfig(KINDLING, true);
		registrar.addConfig(BONE_SAW, true);

		registrar.addComponent(WthitPebbleProvider.INSTANCE, TooltipPosition.BODY, PebbleBlock.class);

		registrar.addComponent(WthitTanningProvider.INSTANCE, TooltipPosition.BODY, TanningRackBlock.class);
		registrar.addBlockData(WthitTanningProvider.INSTANCE, TanningRackBlockEntity.class);

		registrar.addComponent(WthitKindlingProvider.INSTANCE, TooltipPosition.BODY, KindlingBlock.class);
		registrar.addBlockData(WthitKindlingProvider.INSTANCE, KindlingBlockEntity.class);

		registrar.addComponent(WthitBoneSawProvider.INSTANCE, TooltipPosition.BODY, PlacedBoneSawBlock.class);
		registrar.addBlockData(WthitBoneSawProvider.INSTANCE, PlacedBoneSawBlockEntity.class);
	}
}
