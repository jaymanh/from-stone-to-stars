package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.entity.AbstractRotationBlockEntity;
import jaymanh.fsts.rotation.RotationStatus;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadeRotationProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(this)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		String status = data.getStringOr("FstsRotationStatus", RotationStatus.DISCONNECTED.name());
		tooltip.add(Component.translatable("tooltip.from-stone-to-stars.rotation." + status.toLowerCase()));
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("rotation");
	}

	public enum Data implements IServerDataProvider<BlockAccessor> {
		INSTANCE;

		@Override
		public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
			if (accessor.getBlockEntity() instanceof AbstractRotationBlockEntity rotation) {
				tag.putString("FstsRotationStatus", rotation.getRotationStatus().name());
			}
		}

		@Override
		public Identifier getUid() {
			return JadeRotationProvider.INSTANCE.getUid();
		}
	}
}
