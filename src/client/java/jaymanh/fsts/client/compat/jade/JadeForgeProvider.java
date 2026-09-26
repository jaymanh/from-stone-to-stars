package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModCustomBlockModels.ForgeHearthBlock;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.compat.tooltip.ProcessTooltips;
import jaymanh.fsts.multiblock.AssemblyMatch;
import jaymanh.fsts.multiblock.AssemblyMatcher;
import jaymanh.fsts.multiblock.AssemblyRegistry;
import jaymanh.fsts.multiblock.ModMultiblocks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
		boolean lit = accessor.getBlockState().getValue(ForgeHearthBlock.LIT);
		if (lit) {
			ProcessTooltips.kindlingLines(true, accessor.getServerData()).forEach(tooltip::add);
			return;
		}

		String fail = accessor.getServerData().getString("ForgeFail").orElse("");
		if (!fail.isEmpty()) {
			tooltip.add(Component.literal("✗ ").append(Component.literal(fail)));
			return;
		}
		if (accessor.getServerData().getBoolean("ForgeOk").orElse(false)) {
			tooltip.add(Component.literal("✓ ").append(Component.translatable("multiblock.from-stone-to-stars.forge_ready")));
		}
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("forge");
	}

	public enum Data implements IServerDataProvider<BlockAccessor> {
		INSTANCE;

		@Override
		public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
			if (accessor.getBlockEntity() instanceof ForgeHearthBlockEntity hearth && hearth.isBurning()) {
				ProcessTooltips.writeKindling(tag, hearth.getProcessProgress(), hearth.getProcessDuration());
				return;
			}

			AssemblyRegistry.get(ModMultiblocks.FORGE_ID).ifPresent(definition -> {
				AssemblyMatch.Attempt attempt = AssemblyMatcher.attempt(accessor.getLevel(), accessor.getPosition(), definition);
				if (attempt.ok()) {
					tag.putBoolean("ForgeOk", true);
				} else {
					tag.putBoolean("ForgeOk", false);
					attempt.failure().ifPresent(failure -> tag.putString("ForgeFail", failure.getString()));
				}
			});
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
