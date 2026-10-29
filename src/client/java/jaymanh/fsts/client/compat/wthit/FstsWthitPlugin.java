package jaymanh.fsts.client.compat.wthit;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModCustomBlockModels.DriveHousingBlock;
import jaymanh.fsts.block.ModCustomBlockModels.DrillHeadBlock;
import jaymanh.fsts.block.ModCustomBlockModels.OreBoxBlock;
import jaymanh.fsts.block.ModCustomBlockModels.RodRackBlock;
import jaymanh.fsts.block.entity.DriveHousingBlockEntity;
import jaymanh.fsts.block.entity.DrillHeadBlockEntity;
import jaymanh.fsts.block.entity.OreBoxBlockEntity;
import jaymanh.fsts.block.entity.RodRackBlockEntity;
import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PebbleBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PlacedBoneSawBlock;
import jaymanh.fsts.block.ModCustomBlockModels.FishTrapBlock;
import jaymanh.fsts.block.ModCustomBlockModels.SnareBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PoweredQuernBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TripHammerBlock;
import jaymanh.fsts.block.ModCustomBlockModels.BronzeHopperBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PoweredBellowsBlock;
import jaymanh.fsts.block.ModCustomBlockModels.OreCrusherBlock;
import jaymanh.fsts.block.entity.OreCrusherBlockEntity;
import jaymanh.fsts.block.ModCustomBlockModels.ForgeHearthBlock;
import jaymanh.fsts.block.ModCustomBlockModels.ForgeInputBlock;
import jaymanh.fsts.block.ModCustomBlockModels.SawArborBlock;
import jaymanh.fsts.block.ModCustomBlockModels.SawmillPortBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TappingSpoutBlock;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.block.entity.ForgeInputBlockEntity;
import jaymanh.fsts.block.entity.SawArborBlockEntity;
import jaymanh.fsts.block.entity.SawmillPortBlockEntity;
import jaymanh.fsts.block.entity.TappingSpoutBlockEntity;
import jaymanh.fsts.block.ModCustomBlockModels.RotationGearBlock;
import jaymanh.fsts.block.ModCustomBlockModels.WaterWheelBlock;
import jaymanh.fsts.block.ModCustomBlockModels.WoodenShaftBlock;
import jaymanh.fsts.block.entity.KindlingBlockEntity;
import jaymanh.fsts.block.entity.PoweredQuernBlockEntity;
import jaymanh.fsts.block.entity.TripHammerBlockEntity;
import jaymanh.fsts.block.entity.BronzeHopperBlockEntity;
import jaymanh.fsts.block.entity.PoweredBellowsBlockEntity;
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
	public static final Identifier FORGE = FromStoneToStars.id("forge");
	public static final Identifier MACHINES = FromStoneToStars.id("machines");

	@Override
	public void register(IRegistrar registrar) {
		registrar.addConfig(PEBBLE, true);
		registrar.addConfig(TANNING, true);
		registrar.addConfig(KINDLING, true);
		registrar.addConfig(BONE_SAW, true);
		registrar.addConfig(SNARE, true);
		registrar.addConfig(ROTATION, true);
		registrar.addConfig(FORGE, true);
		registrar.addConfig(MACHINES, true);

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
		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, BronzeHopperBlock.class);
		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, PoweredBellowsBlock.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, WaterWheelBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, WoodenShaftBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, RotationGearBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, PoweredQuernBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, TripHammerBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, BronzeHopperBlockEntity.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, PoweredBellowsBlockEntity.class);
		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, OreCrusherBlock.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, OreCrusherBlockEntity.class);
		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, SawArborBlock.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, SawArborBlockEntity.class);

		registrar.addComponent(WthitForgeProvider.INSTANCE, TooltipPosition.BODY, ForgeHearthBlock.class);
		registrar.addBlockData(WthitForgeProvider.INSTANCE, ForgeHearthBlockEntity.class);

		registrar.addComponent(WthitMachineProvider.INSTANCE, TooltipPosition.BODY, ForgeInputBlock.class);
		registrar.addComponent(WthitMachineProvider.INSTANCE, TooltipPosition.BODY, TappingSpoutBlock.class);
		registrar.addComponent(WthitMachineProvider.INSTANCE, TooltipPosition.BODY, PoweredBellowsBlock.class);
		registrar.addComponent(WthitMachineProvider.INSTANCE, TooltipPosition.BODY, OreCrusherBlock.class);
		registrar.addComponent(WthitMachineProvider.INSTANCE, TooltipPosition.BODY, SawArborBlock.class);
		registrar.addComponent(WthitMachineProvider.INSTANCE, TooltipPosition.BODY, SawmillPortBlock.class);
		registrar.addBlockData(WthitMachineProvider.INSTANCE, ForgeInputBlockEntity.class);
		registrar.addBlockData(WthitMachineProvider.INSTANCE, TappingSpoutBlockEntity.class);
		registrar.addBlockData(WthitMachineProvider.INSTANCE, PoweredBellowsBlockEntity.class);
		registrar.addBlockData(WthitMachineProvider.INSTANCE, OreCrusherBlockEntity.class);
		registrar.addBlockData(WthitMachineProvider.INSTANCE, SawArborBlockEntity.class);
		registrar.addBlockData(WthitMachineProvider.INSTANCE, SawmillPortBlockEntity.class);

		registrar.addComponent(WthitRotationProvider.INSTANCE, TooltipPosition.BODY, DriveHousingBlock.class);
		registrar.addBlockData(WthitRotationProvider.INSTANCE, DriveHousingBlockEntity.class);
		registrar.addComponent(WthitMachineProvider.INSTANCE, TooltipPosition.BODY, DrillHeadBlock.class);
		registrar.addComponent(WthitMachineProvider.INSTANCE, TooltipPosition.BODY, RodRackBlock.class);
		registrar.addComponent(WthitMachineProvider.INSTANCE, TooltipPosition.BODY, OreBoxBlock.class);
		registrar.addBlockData(WthitMachineProvider.INSTANCE, DrillHeadBlockEntity.class);
		registrar.addBlockData(WthitMachineProvider.INSTANCE, RodRackBlockEntity.class);
		registrar.addBlockData(WthitMachineProvider.INSTANCE, OreBoxBlockEntity.class);
	}
}
