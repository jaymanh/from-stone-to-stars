package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PebbleBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PlacedBoneSawBlock;
import jaymanh.fsts.block.ModCustomBlockModels.FishTrapBlock;
import jaymanh.fsts.block.ModCustomBlockModels.SnareBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PoweredQuernBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TripHammerBlock;
import jaymanh.fsts.block.ModCustomBlockModels.RotationGearBlock;
import jaymanh.fsts.block.ModCustomBlockModels.WaterWheelBlock;
import jaymanh.fsts.block.ModCustomBlockModels.WoodenShaftBlock;
import jaymanh.fsts.block.entity.KindlingBlockEntity;
import jaymanh.fsts.block.entity.PoweredQuernBlockEntity;
import jaymanh.fsts.block.entity.TripHammerBlockEntity;
import jaymanh.fsts.block.entity.RotationGearBlockEntity;
import jaymanh.fsts.block.entity.WaterWheelBlockEntity;
import jaymanh.fsts.block.entity.WoodenShaftBlockEntity;
import jaymanh.fsts.block.entity.PlacedBoneSawBlockEntity;
import jaymanh.fsts.block.entity.SnareBlockEntity;
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
	public static final Identifier SNARE = FromStoneToStars.id("snare");
	public static final Identifier ROTATION = FromStoneToStars.id("rotation");

	@Override
	public void register(IRegistrar registrar) {
		registrar.addConfig(PEBBLE, true);
		registrar.addConfig(TANNING, true);
		registrar.addConfig(KINDLING, true);
		registrar.addConfig(BONE_SAW, true);
		registrar.addConfig(SNARE, true);
		registrar.addConfig(ROTATION, true);

		registrar.addComponent(WthitPebbleProvider.INSTANCE, TooltipPosition.BODY, PebbleBlock.class);

		registrar.addComponent(WthitTanningProvider.INSTANCE, TooltipPosition.BODY, TanningRackBlock.class);
		registrar.addBlockData(WthitTanningProvider.INSTANCE, TanningRackBlockEntity.class);

		registrar.addComponent(WthitKindlingProvider.INSTANCE, TooltipPosition.BODY, KindlingBlock.class);
		registrar.addBlockData(WthitKindlingProvider.INSTANCE, KindlingBlockEntity.class);

		registrar.addComponent(WthitBoneSawProvider.INSTANCE, TooltipPosition.BODY, PlacedBoneSawBlock.class);
		registrar.addBlockData(WthitBoneSawProvider.INSTANCE, PlacedBoneSawBlockEntity.class);

		registrar.addComponent(WthitSnareProvider.INSTANCE, TooltipPosition.BODY, SnareBlock.class);
		registrar.addComponent(WthitSnareProvider.INSTANCE, TooltipPosition.BODY, FishTrapBlock.class);
		registrar.addBlockData(WthitSnareProvider.INSTANCE, SnareBlockEntity.class);

		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, WaterWheelBlock.class);
		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, WoodenShaftBlock.class);
		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, RotationGearBlock.class);
		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, PoweredQuernBlock.class);
		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, TripHammerBlock.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, WaterWheelBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, WoodenShaftBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, RotationGearBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, PoweredQuernBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, TripHammerBlockEntity.class);
	}
}
