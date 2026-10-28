package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModCustomBlockModels.ForgeHearthBlock;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.compat.tooltip.ProcessTooltips;
import jaymanh.fsts.multiblock.AssemblyMatch;
import jaymanh.fsts.multiblock.AssemblyMatcher;
import jaymanh.fsts.multiblock.AssemblyRegistry;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.ModMultiblocks;
import jaymanh.fsts.multiblock.process.ForgeProcess;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

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
			if (accessor.getServerData().getBooleanOr("ForgePoweredBellows", false)) {
				tooltip.add(Component.translatable("tooltip.from-stone-to-stars.forge.powered_bellows"));
			}
			int lined = accessor.getServerData().getIntOr("ForgeLinedHearths", 0);
			if (lined > 0) {
				int hearths = accessor.getServerData().getIntOr("ForgeHearths", lined);
				tooltip.add(Component.translatable("tooltip.from-stone-to-stars.forge.lined_hearths", lined, hearths));
			}
			Identifier alloyId = Identifier.tryParse(accessor.getServerData().getStringOr("ForgeAlloyItem", ""));
			if (alloyId != null) {
				BuiltInRegistries.ITEM.getOptional(alloyId).ifPresent(item -> tooltip.add(Component.translatable(
						"tooltip.from-stone-to-stars.forge.alloying",
						accessor.getServerData().getIntOr("ForgeAlloyCount", 1),
						new ItemStack(item).getHoverName()
				)));
			}
			return;
		}
		if (accessor.getBlockState().getValue(ForgeHearthBlock.LINED)) {
			tooltip.add(Component.translatable("tooltip.from-stone-to-stars.forge.lined"));
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
				FormedMultiblock formed = hearth.getFormed();
				if (formed != null && formed.process() instanceof ForgeProcess forge) {
					tag.putBoolean("ForgePoweredBellows", forge.drivingBellows() > 0);
					tag.putInt("ForgeLinedHearths", forge.linedHearths());
					tag.putInt("ForgeHearths", forge.hearthCount());
				}
				ItemStack alloy = hearth.getMaster().alloyResult();
				if (!alloy.isEmpty()) {
					tag.putString("ForgeAlloyItem", BuiltInRegistries.ITEM.getKey(alloy.getItem()).toString());
					tag.putInt("ForgeAlloyCount", alloy.getCount());
				}
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
