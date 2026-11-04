package jaymanh.fsts.tools;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jaymanh.fsts.advancement.AgeAdvancements;
import jaymanh.fsts.block.entity.DrillHeadBlockEntity;
import jaymanh.fsts.tags.ModTags;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Reads the ground for a bore rig. Use it on the block a drill head would stand on: it counts the
 * {@link ModTags#DRILL_ORES} veins that the 3×3 column a bore from there would touch, all the way down.
 */
public class ProspectorsPickItem extends Item {
	public static final int COOLDOWN_TICKS = 40;

	public ProspectorsPickItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		if (!(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) {
			return InteractionResult.SUCCESS;
		}
		BlockPos top = context.getClickedPos();
		Map<String, Integer> veins = survey(serverLevel, top);

		if (veins.isEmpty()) {
			serverPlayer.sendSystemMessage(Component.translatable("item.from-stone-to-stars.prospectors_pick.nothing")
					.withStyle(ChatFormatting.GRAY), false);
		} else {
			serverPlayer.sendSystemMessage(Component.translatable("item.from-stone-to-stars.prospectors_pick.header")
					.withStyle(ChatFormatting.GOLD), false);
			veins.forEach((family, count) -> serverPlayer.sendSystemMessage(line(family, count), false));
		}

		AgeAdvancements.grant(serverPlayer, "age2/prospector", "surveyed");
		level.playSound(null, top, SoundEvents.STONE_HIT, SoundSource.PLAYERS, 1.0F, 1.4F);
		ItemStack stack = context.getItemInHand();
		stack.hurtAndBreak(1, player, context.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
		player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
		return InteractionResult.SUCCESS;
	}

	private static Component line(String family, int count) {
		Identifier id = Identifier.parse(family);
		MutableComponent name = BuiltInRegistries.BLOCK.getOptional(id)
				.map(Block::getName)
				.orElseGet(() -> Component.literal(id.getPath()))
				.withStyle(style -> style.withColor(TextColor.fromRgb(colour(id.getPath()))));
		String key = count == 1 ? "item.from-stone-to-stars.prospectors_pick.vein" : "item.from-stone-to-stars.prospectors_pick.veins";
		return Component.translatable("item.from-stone-to-stars.prospectors_pick.line",
				name,
				Component.translatable(key, count).withStyle(ChatFormatting.WHITE)
		).withStyle(ChatFormatting.DARK_GRAY);
	}

	private static int colour(String path) {
		if (path.contains("iron")) return 0xD8AF93;
		if (path.contains("copper")) return 0xE77C56;
		if (path.contains("gold")) return 0xFCEE4B;
		if (path.contains("coal")) return 0xA0A0A0;
		if (path.contains("redstone")) return 0xFF4040;
		if (path.contains("lapis")) return 0x5B7FFF;
		if (path.contains("zinc")) return 0x9FD8CB;
		return 0xFFFFFF;
	}

	/**
	 * Counts each vein the column touches once, by family, in the order the bore would meet them.
	 * A vein is traced the way the rig traces it: same family, 26-way neighbours, capped at the rig's vein size.
	 */
	private static Map<String, Integer> survey(ServerLevel level, BlockPos top) {
		Map<String, Integer> veins = new LinkedHashMap<>();
		Set<BlockPos> traced = new HashSet<>();
		for (int y = top.getY(); y >= level.getMinY(); y--) {
			for (int dx = -1; dx <= 1; dx++) {
				for (int dz = -1; dz <= 1; dz++) {
					BlockPos pos = new BlockPos(top.getX() + dx, y, top.getZ() + dz);
					if (traced.contains(pos)) {
						continue;
					}
					BlockState state = level.getBlockState(pos);
					if (!state.is(ModTags.DRILL_ORES)) {
						continue;
					}
					String family = family(state);
					trace(level, pos, family, traced);
					veins.merge(family, 1, Integer::sum);
				}
			}
		}
		return veins;
	}

	private static void trace(ServerLevel level, BlockPos start, String family, Set<BlockPos> traced) {
		Queue<BlockPos> frontier = new ArrayDeque<>();
		frontier.add(start);
		traced.add(start);
		int size = 1;
		while (!frontier.isEmpty() && size < DrillHeadBlockEntity.MAX_VEIN) {
			BlockPos current = frontier.remove();
			for (BlockPos neighbour : BlockPos.betweenClosed(current.offset(-1, -1, -1), current.offset(1, 1, 1))) {
				if (size >= DrillHeadBlockEntity.MAX_VEIN || traced.contains(neighbour)) {
					continue;
				}
				BlockState state = level.getBlockState(neighbour);
				if (state.is(ModTags.DRILL_ORES) && family(state).equals(family)) {
					BlockPos found = neighbour.immutable();
					traced.add(found);
					frontier.add(found);
					size++;
				}
			}
		}
	}

	/** Deepslate and stone forms of an ore count as one, named by the stone form. */
	private static String family(BlockState state) {
		Identifier id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
		return id.getNamespace() + ":" + id.getPath().replace("deepslate_", "");
	}
}
