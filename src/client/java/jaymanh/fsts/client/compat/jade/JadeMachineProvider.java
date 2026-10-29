package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.compat.tooltip.MachineTooltips;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/** Ironworks fittings, the ore crusher, and the sawmill. */
public enum JadeMachineProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(this)) {
			return;
		}
		MachineTooltips.machineLines(accessor.getServerData(), accessor.getLevel().registryAccess()).forEach(tooltip::add);
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("machines");
	}

	public enum Data implements IServerDataProvider<BlockAccessor> {
		INSTANCE;

		@Override
		public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
			if (accessor.getBlockEntity() != null) {
				MachineTooltips.writeMachine(tag, accessor.getBlockEntity(), accessor.getLevel().registryAccess());
			}
		}

		@Override
		public Identifier getUid() {
			return JadeMachineProvider.INSTANCE.getUid();
		}
	}
}
