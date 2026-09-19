package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.block.ModCustomBlockModels.KindlingBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PebbleBlock;
import jaymanh.fsts.block.ModCustomBlockModels.PlacedBoneSawBlock;
import jaymanh.fsts.block.ModCustomBlockModels.TanningRackBlock;
import jaymanh.fsts.block.entity.KindlingBlockEntity;
import jaymanh.fsts.block.entity.PlacedBoneSawBlockEntity;
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
		registration.registerBlockDataProvider(JadeBoneSawProvider.Data.INSTANCE, PlacedBoneSawBlockEntity.class);
		registration.registerBlockDataProvider(JadeMultiblockProvider.Data.INSTANCE, Block.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(JadePebbleProvider.INSTANCE, PebbleBlock.class);
		registration.registerBlockComponent(JadeTanningProvider.INSTANCE, TanningRackBlock.class);
		registration.registerBlockComponent(JadeKindlingProvider.INSTANCE, KindlingBlock.class);
		registration.registerBlockComponent(JadeBoneSawProvider.INSTANCE, PlacedBoneSawBlock.class);
		registration.registerBlockComponent(JadeMultiblockProvider.INSTANCE, Block.class);
	}
}
