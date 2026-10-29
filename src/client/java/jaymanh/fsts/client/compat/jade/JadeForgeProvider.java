package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.compat.tooltip.MachineTooltips;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadeForgeProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(this)) {
			return;
		}
		MachineTooltips.forgeLines(accessor.getBlockState(), accessor.getServerData(), accessor.getLevel().registryAccess())
				.forEach(tooltip::add);
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("forge");
	}

	public enum Data implements IServerDataProvider<BlockAccessor> {
		INSTANCE;

		@Override
		public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
			if (accessor.getBlockEntity() instanceof ForgeHearthBlockEntity hearth) {
				MachineTooltips.writeForge(tag, hearth, accessor.getLevel().registryAccess());
			}
		}

		@Override
		public boolean shouldRequestData(BlockAccessor accessor) {
			return true;
		}

		@Override
		public Identifier getUid() {
			return JadeForgeProvider.INSTANCE.getUid();
		}
	}
}
