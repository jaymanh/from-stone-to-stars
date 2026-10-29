package jaymanh.fsts.compat.tooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jaymanh.fsts.block.ModCustomBlockModels.ForgeHearthBlock;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.block.entity.ForgeInputBlockEntity;
import jaymanh.fsts.block.entity.ForgeStoreBlockEntity;
import jaymanh.fsts.block.entity.OreCrusherBlockEntity;
import jaymanh.fsts.block.entity.PoweredBellowsBlockEntity;
import jaymanh.fsts.block.entity.SawArborBlockEntity;
import jaymanh.fsts.block.entity.SawmillPortBlockEntity;
import jaymanh.fsts.multiblock.ActiveMultiblocks;
import jaymanh.fsts.multiblock.AssemblyMatch;
import jaymanh.fsts.multiblock.AssemblyMatcher;
import jaymanh.fsts.multiblock.AssemblyRegistry;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.ModMultiblocks;
import jaymanh.fsts.multiblock.process.ForgeProcess;
import jaymanh.fsts.recipe.ForgingRecipe;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Server data and tooltip lines for the forge, its ironworks fittings, the ore crusher, and the
 * sawmill. Shared by the Jade and WTHIT plugins so both show the same thing.
 */
public final class MachineTooltips {
	private static final String FORGE_POWERED_BELLOWS = "ForgePoweredBellowsCount";
	private static final String FORGE_DRIVING_BELLOWS = "ForgeDrivingBellows";
	private static final String FORGE_LINED = "ForgeLinedHearths";
	private static final String FORGE_HEARTHS = "ForgeHearths";
	private static final String FORGE_INPUTS = "ForgeInputs";
	private static final String FORGE_SPOUTS = "ForgeSpouts";
	private static final String FORGE_CRUSHERS = "ForgeCrushers";
	private static final String FORGE_IRONWORKS = "ForgeIronworks";
	private static final String FORGE_ALLOY_ITEM = "ForgeAlloyItem";
	private static final String FORGE_ALLOY_COUNT = "ForgeAlloyCount";
	private static final String FORGE_OK = "ForgeOk";
	private static final String FORGE_FAIL = "ForgeFail";

	/** 0: no lit forge, 1: a lit forge, 2: a lit forge that is a full ironworks. */
	private static final String FITTING_FORGE = "FstsFittingForge";
	private static final String STORE_USED = "FstsStoreUsed";
	private static final String STORE_SIZE = "FstsStoreSize";

	private static final String CRUSHER_INPUT = "FstsCrusherInput";
	private static final String CRUSHER_ORE = "FstsCrusherOre";
	private static final String CRUSHER_ORE_COUNT = "FstsCrusherOreCount";
	private static final String CRUSHER_HITS = "FstsCrusherHits";
	private static final String CRUSHER_HITS_NEEDED = "FstsCrusherHitsNeeded";
	private static final String CRUSHER_HELD = "FstsCrusherHeld";

	private static final String SAWMILL_STATUS = "FstsSawmillStatus";

	private MachineTooltips() {
	}

	// ---- Forge hearth ----

	public static void writeForge(CompoundTag tag, ForgeHearthBlockEntity hearth, HolderLookup.Provider registries) {
		Level level = hearth.getLevel();
		if (level == null) {
			return;
		}
		if (hearth.isBurning()) {
			ProcessTooltips.writeKindling(tag, hearth.getProcessProgress(), hearth.getProcessDuration());
			FormedMultiblock formed = hearth.getFormed();
			if (formed != null && formed.process() instanceof ForgeProcess forge) {
				tag.putInt(FORGE_POWERED_BELLOWS, forge.poweredBellows());
				tag.putInt(FORGE_DRIVING_BELLOWS, forge.drivingBellows());
				tag.putInt(FORGE_LINED, forge.linedHearths());
				tag.putInt(FORGE_HEARTHS, forge.hearthCount());
				tag.putInt(FORGE_INPUTS, forge.inputs().size());
				tag.putInt(FORGE_SPOUTS, forge.spouts().size());
				tag.putInt(FORGE_CRUSHERS, forge.crushers());
				tag.putBoolean(FORGE_IRONWORKS, forge.isIronworks());
			}
			ItemStack alloy = hearth.getMaster().alloyResult();
			if (!alloy.isEmpty()) {
				tag.putString(FORGE_ALLOY_ITEM, BuiltInRegistries.ITEM.getKey(alloy.getItem()).toString());
				tag.putInt(FORGE_ALLOY_COUNT, alloy.getCount());
			}
			return;
		}

		AssemblyRegistry.get(ModMultiblocks.FORGE_ID).ifPresent(definition -> {
			AssemblyMatch.Attempt attempt = AssemblyMatcher.attempt(level, hearth.getBlockPos(), definition);
			tag.putBoolean(FORGE_OK, attempt.ok());
			attempt.failure().ifPresent(failure -> putComponent(tag, FORGE_FAIL, failure, registries));
		});
	}

	public static List<Component> forgeLines(BlockState state, CompoundTag data, HolderLookup.Provider registries) {
		List<Component> lines = new ArrayList<>();
		if (state.getValue(ForgeHearthBlock.LIT)) {
			lines.addAll(ProcessTooltips.kindlingLines(true, data));
			if (data.getBooleanOr(FORGE_IRONWORKS, false)) {
				lines.add(Component.translatable("tooltip.from-stone-to-stars.forge.ironworks").withStyle(ChatFormatting.GOLD));
			}
			int powered = data.getIntOr(FORGE_POWERED_BELLOWS, 0);
			if (data.getIntOr(FORGE_DRIVING_BELLOWS, 0) > 0) {
				lines.add(Component.translatable("tooltip.from-stone-to-stars.forge.powered_bellows"));
			} else if (powered > 0) {
				lines.add(Component.translatable("tooltip.from-stone-to-stars.forge.powered_bellows_idle"));
			}
			int lined = data.getIntOr(FORGE_LINED, 0);
			if (lined > 0) {
				lines.add(Component.translatable("tooltip.from-stone-to-stars.forge.lined_hearths", lined, data.getIntOr(FORGE_HEARTHS, lined)));
			}
			addCount(lines, data, FORGE_INPUTS, "tooltip.from-stone-to-stars.forge.inputs");
			addCount(lines, data, FORGE_CRUSHERS, "tooltip.from-stone-to-stars.forge.crushers");
			addCount(lines, data, FORGE_SPOUTS, "tooltip.from-stone-to-stars.forge.spouts");
			Identifier alloyId = Identifier.tryParse(data.getStringOr(FORGE_ALLOY_ITEM, ""));
			if (alloyId != null) {
				BuiltInRegistries.ITEM.getOptional(alloyId).ifPresent(item -> lines.add(Component.translatable(
						"tooltip.from-stone-to-stars.forge.alloying",
						data.getIntOr(FORGE_ALLOY_COUNT, 1),
						new ItemStack(item).getHoverName()
				)));
			}
			return lines;
		}

		if (state.getValue(ForgeHearthBlock.LINED)) {
			lines.add(Component.translatable("tooltip.from-stone-to-stars.forge.lined"));
		}
		Optional<Component> fail = getComponent(data, FORGE_FAIL, registries);
		if (fail.isPresent()) {
			lines.add(Component.literal("✗ ").append(fail.get()));
		} else if (data.getBooleanOr(FORGE_OK, false)) {
			lines.add(Component.literal("✓ ").append(Component.translatable("multiblock.from-stone-to-stars.forge_ready")));
		}
		return lines;
	}

	private static void addCount(List<Component> lines, CompoundTag data, String key, String translation) {
		int count = data.getIntOr(key, 0);
		if (count > 0) {
			lines.add(Component.translatable(translation, count));
		}
	}

	// ---- Fittings, crusher, sawmill ----

	/** Writes whatever the machine overlay needs for this block entity. Unknown types write nothing. */
	public static void writeMachine(CompoundTag tag, BlockEntity entity, HolderLookup.Provider registries) {
		Level level = entity.getLevel();
		if (level == null) {
			return;
		}
		BlockPos pos = entity.getBlockPos();
		switch (entity) {
			case ForgeStoreBlockEntity store -> {
				tag.putInt(FITTING_FORGE, forgeState(level, pos));
				int used = 0;
				for (int slot = 0; slot < store.getContainerSize(); slot++) {
					if (!store.getItem(slot).isEmpty()) {
						used++;
					}
				}
				tag.putInt(STORE_USED, used);
				tag.putInt(STORE_SIZE, store.getContainerSize());
			}
			case PoweredBellowsBlockEntity ignored -> tag.putInt(FITTING_FORGE, forgeState(level, pos));
			case OreCrusherBlockEntity crusher -> writeCrusher(tag, crusher, level, pos);
			case SawArborBlockEntity arbor -> writeSawmill(tag, arbor, registries);
			case SawmillPortBlockEntity port -> {
				SawArborBlockEntity arbor = port.arbor();
				if (arbor != null) {
					writeSawmill(tag, arbor, registries);
				} else {
					putComponent(tag, SAWMILL_STATUS, Component.translatable("multiblock.from-stone-to-stars.sawmill.port_unformed"), registries);
				}
			}
			default -> {
			}
		}
	}

	public static List<Component> machineLines(CompoundTag data, HolderLookup.Provider registries) {
		List<Component> lines = new ArrayList<>();
		if (data.contains(FITTING_FORGE)) {
			lines.add(switch (data.getIntOr(FITTING_FORGE, 0)) {
				case 2 -> Component.translatable("tooltip.from-stone-to-stars.fitting.ironworks").withStyle(ChatFormatting.GOLD);
				case 1 -> Component.translatable("tooltip.from-stone-to-stars.fitting.lit_forge");
				default -> Component.translatable("tooltip.from-stone-to-stars.fitting.no_forge").withStyle(ChatFormatting.GRAY);
			});
		}
		if (data.contains(STORE_SIZE)) {
			lines.add(Component.translatable("tooltip.from-stone-to-stars.fitting.stacks", data.getIntOr(STORE_USED, 0), data.getIntOr(STORE_SIZE, 0)));
		}
		if (data.contains(CRUSHER_INPUT)) {
			crusherLines(data, lines);
		}
		getComponent(data, SAWMILL_STATUS, registries).ifPresent(lines::add);
		if (data.contains(SAWMILL_STATUS)) {
			lines.addAll(ProcessTooltips.progressLines(data));
		}
		return lines;
	}

	/** 0 when no lit forge holds this position, 1 for a lit forge, 2 for a lit full ironworks. */
	private static int forgeState(Level level, BlockPos pos) {
		return ActiveMultiblocks.get(level, pos)
				.map(formed -> formed.process() instanceof ForgeProcess forge ? (forge.isIronworks() ? 2 : 1) : 0)
				.orElse(0);
	}

	private static void writeCrusher(CompoundTag tag, OreCrusherBlockEntity crusher, Level level, BlockPos pos) {
		boolean onInput = level.getBlockEntity(pos.below()) instanceof ForgeInputBlockEntity;
		tag.putBoolean(CRUSHER_INPUT, onInput);
		if (onInput) {
			tag.putInt(FITTING_FORGE, forgeState(level, pos.below()));
		}
		ItemStack ore = crusher.getItem(OreCrusherBlockEntity.SLOT_ORE);
		if (!ore.isEmpty()) {
			ForgingRecipe recipe = OreCrusherBlockEntity.findCrush(level, ore);
			tag.putString(CRUSHER_ORE, BuiltInRegistries.ITEM.getKey(ore.getItem()).toString());
			tag.putInt(CRUSHER_ORE_COUNT, ore.getCount());
			tag.putInt(CRUSHER_HITS, crusher.hits());
			tag.putInt(CRUSHER_HITS_NEEDED, recipe == null ? 0 : recipe.hits());
		}
		tag.putBoolean(CRUSHER_HELD, !crusher.getItem(OreCrusherBlockEntity.SLOT_HELD).isEmpty());
	}

	private static void crusherLines(CompoundTag data, List<Component> lines) {
		if (!data.getBooleanOr(CRUSHER_INPUT, false)) {
			lines.add(Component.translatable("block.from-stone-to-stars.ore_crusher.needs_input").withStyle(ChatFormatting.RED));
		}
		Identifier oreId = Identifier.tryParse(data.getStringOr(CRUSHER_ORE, ""));
		int needed = data.getIntOr(CRUSHER_HITS_NEEDED, 0);
		if (oreId != null && needed > 0) {
			BuiltInRegistries.ITEM.getOptional(oreId).ifPresent(item -> lines.add(Component.translatable(
					"tooltip.from-stone-to-stars.crusher.crushing",
					new ItemStack(item).getHoverName(),
					data.getIntOr(CRUSHER_ORE_COUNT, 1),
					Math.min(data.getIntOr(CRUSHER_HITS, 0), needed),
					needed
			)));
		}
		if (data.getBooleanOr(CRUSHER_HELD, false)) {
			lines.add(Component.translatable("tooltip.from-stone-to-stars.crusher.input_full").withStyle(ChatFormatting.YELLOW));
		}
	}

	private static void writeSawmill(CompoundTag tag, SawArborBlockEntity arbor, HolderLookup.Provider registries) {
		putComponent(tag, SAWMILL_STATUS, arbor.statusMessage(), registries);
		Level level = arbor.getLevel();
		if (level != null && arbor.isFormed()) {
			ActiveMultiblocks.get(level, arbor.getBlockPos()).ifPresent(formed ->
					ProcessTooltips.writeKindling(tag, formed.getProcessProgress(), formed.getProcessDuration())
			);
		}
	}

	// ---- Components travel as NBT so translations resolve on the client ----

	private static void putComponent(CompoundTag tag, String key, Component component, HolderLookup.Provider registries) {
		ComponentSerialization.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), component)
				.result()
				.ifPresent(encoded -> tag.put(key, encoded));
	}

	private static Optional<Component> getComponent(CompoundTag tag, String key, HolderLookup.Provider registries) {
		Tag encoded = tag.get(key);
		if (encoded == null) {
			return Optional.empty();
		}
		return ComponentSerialization.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), encoded).result();
	}
}
