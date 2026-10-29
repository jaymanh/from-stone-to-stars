package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.block.ModCustomBlockModels.DriveHousingBlock;
import jaymanh.fsts.block.ModCustomBlockModels.DrillHeadBlock;
import jaymanh.fsts.block.ModCustomBlockModels.OreBoxBlock;
import jaymanh.fsts.block.ModCustomBlockModels.RodRackBlock;
import jaymanh.fsts.block.entity.DriveHousingBlockEntity;
import jaymanh.fsts.block.entity.DrillHeadBlockEntity;
import jaymanh.fsts.block.entity.OreBoxBlockEntity;
import jaymanh.fsts.block.entity.RodRackBlockEntity;
import jaymanh.fsts.block.ModCustomBlockModels.ForgeHearthBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PoweredQuernBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TripHammerBlock;
import jaymanh.fsts.block.ModCustomBlockModels.BronzeHopperBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PoweredBellowsBlock;
import jaymanh.fsts.block.ModCustomBlockModels.OreCrusherBlock;
import jaymanh.fsts.block.entity.OreCrusherBlockEntity;
import jaymanh.fsts.block.ModCustomBlockModels.ForgeInputBlock;
import jaymanh.fsts.block.ModCustomBlockModels.SawArborBlock;
import jaymanh.fsts.block.ModCustomBlockModels.SawmillPortBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TappingSpoutBlock;
import jaymanh.fsts.block.entity.ForgeInputBlockEntity;
import jaymanh.fsts.block.entity.SawArborBlockEntity;
import jaymanh.fsts.block.entity.SawmillPortBlockEntity;
import jaymanh.fsts.block.entity.TappingSpoutBlockEntity;
import jaymanh.fsts.block.ModCustomBlockModels.RotationGearBlock;
import jaymanh.fsts.block.ModCustomBlockModels.WaterWheelBlock;
import jaymanh.fsts.block.ModCustomBlockModels.WoodenShaftBlock;
import jaymanh.fsts.block.entity.PoweredQuernBlockEntity;
import jaymanh.fsts.block.entity.TripHammerBlockEntity;
import jaymanh.fsts.block.entity.BronzeHopperBlockEntity;
import jaymanh.fsts.block.entity.PoweredBellowsBlockEntity;
import jaymanh.fsts.block.entity.RotationGearBlockEntity;
import jaymanh.fsts.block.entity.WaterWheelBlockEntity;
import jaymanh.fsts.block.entity.WoodenShaftBlockEntity;
import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PebbleBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PlacedBoneSawBlock;
import jaymanh.fsts.block.ModCustomBlockModels.FishTrapBlock;
import jaymanh.fsts.block.ModCustomBlockModels.SnareBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.block.entity.KindlingBlockEntity;
import jaymanh.fsts.block.entity.PlacedBoneSawBlockEntity;
import jaymanh.fsts.block.entity.SnareBlockEntity;
import jaymanh.fsts.block.entity.TanningRackBlockEntity;

import net.minecraft.world.level.block.Block;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public final class FstsJadePlugin implements IWailaPlugin {
	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(JadeTanningProvider.Data.INSTANCE, TanningRackBlockEntity.class);
		registration.registerBlockDataProvider(JadeKindlingProvider.Data.INSTANCE, KindlingBlockEntity.class);
		registration.registerBlockDataProvider(JadeForgeProvider.Data.INSTANCE, ForgeHearthBlockEntity.class);
		registration.registerBlockDataProvider(JadeBoneSawProvider.Data.INSTANCE, PlacedBoneSawBlockEntity.class);
		registration.registerBlockDataProvider(JadeMultiblockProvider.Data.INSTANCE, Block.class);
		registration.registerBlockDataProvider(JadeSnareProvider.Data.INSTANCE, SnareBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, WaterWheelBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, WoodenShaftBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, RotationGearBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, PoweredQuernBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, TripHammerBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, BronzeHopperBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, PoweredBellowsBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, OreCrusherBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, SawArborBlockEntity.class);
		registration.registerBlockDataProvider(JadeMachineProvider.Data.INSTANCE, ForgeInputBlockEntity.class);
		registration.registerBlockDataProvider(JadeMachineProvider.Data.INSTANCE, TappingSpoutBlockEntity.class);
		registration.registerBlockDataProvider(JadeMachineProvider.Data.INSTANCE, PoweredBellowsBlockEntity.class);
		registration.registerBlockDataProvider(JadeMachineProvider.Data.INSTANCE, OreCrusherBlockEntity.class);
		registration.registerBlockDataProvider(JadeMachineProvider.Data.INSTANCE, SawArborBlockEntity.class);
		registration.registerBlockDataProvider(JadeMachineProvider.Data.INSTANCE, SawmillPortBlockEntity.class);
		registration.registerBlockDataProvider(JadeRotationProvider.Data.INSTANCE, DriveHousingBlockEntity.class);
		registration.registerBlockDataProvider(JadeMachineProvider.Data.INSTANCE, DrillHeadBlockEntity.class);
		registration.registerBlockDataProvider(JadeMachineProvider.Data.INSTANCE, RodRackBlockEntity.class);
		registration.registerBlockDataProvider(JadeMachineProvider.Data.INSTANCE, OreBoxBlockEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(JadePebbleProvider.INSTANCE, PebbleBlock.class);
		registration.registerBlockComponent(JadeTanningProvider.INSTANCE, TanningRackBlock.class);
		registration.registerBlockComponent(JadeKindlingProvider.INSTANCE, KindlingBlock.class);
		registration.registerBlockComponent(JadeForgeProvider.INSTANCE, ForgeHearthBlock.class);
		registration.registerBlockComponent(JadeBoneSawProvider.INSTANCE, PlacedBoneSawBlock.class);
		registration.registerBlockComponent(JadeMultiblockProvider.INSTANCE, Block.class);
		registration.registerBlockComponent(JadeSnareProvider.INSTANCE, SnareBlock.class);
		registration.registerBlockComponent(JadeSnareProvider.INSTANCE, FishTrapBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, WaterWheelBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, WoodenShaftBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, RotationGearBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, PoweredQuernBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, TripHammerBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, BronzeHopperBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, PoweredBellowsBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, OreCrusherBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, SawArborBlock.class);
		registration.registerBlockComponent(JadeMachineProvider.INSTANCE, ForgeInputBlock.class);
		registration.registerBlockComponent(JadeMachineProvider.INSTANCE, TappingSpoutBlock.class);
		registration.registerBlockComponent(JadeMachineProvider.INSTANCE, PoweredBellowsBlock.class);
		registration.registerBlockComponent(JadeMachineProvider.INSTANCE, OreCrusherBlock.class);
		registration.registerBlockComponent(JadeMachineProvider.INSTANCE, SawArborBlock.class);
		registration.registerBlockComponent(JadeMachineProvider.INSTANCE, SawmillPortBlock.class);
		registration.registerBlockComponent(JadeRotationProvider.INSTANCE, DriveHousingBlock.class);
		registration.registerBlockComponent(JadeMachineProvider.INSTANCE, DrillHeadBlock.class);
		registration.registerBlockComponent(JadeMachineProvider.INSTANCE, RodRackBlock.class);
		registration.registerBlockComponent(JadeMachineProvider.INSTANCE, OreBoxBlock.class);
	}
}
