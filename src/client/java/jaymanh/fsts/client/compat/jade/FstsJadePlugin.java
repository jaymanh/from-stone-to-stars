package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.block.ModCustomBlockModels.ForgeHearthBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PoweredQuernBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TripHammerBlock;
import jaymanh.fsts.block.ModCustomBlockModels.RotationGearBlock;
import jaymanh.fsts.block.ModCustomBlockModels.WaterWheelBlock;
import jaymanh.fsts.block.ModCustomBlockModels.WoodenShaftBlock;
import jaymanh.fsts.block.entity.PoweredQuernBlockEntity;
import jaymanh.fsts.block.entity.TripHammerBlockEntity;
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
	}
}
