package jaymanh.fsts.tools;

import java.util.function.Consumer;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class LeatherSkinItem extends Item {
	public static final int CAPACITY = 16;

	public LeatherSkinItem(Properties properties) {
		super(properties);
	}

	public static int getWaterUses(ItemStack stack) {
		return Mth.clamp(CAPACITY - stack.getDamageValue(), 0, CAPACITY);
	}

	public static boolean hasWater(ItemStack stack) {
		return getWaterUses(stack) > 0;
	}

	public static boolean isFull(ItemStack stack) {
		return stack.getDamageValue() <= 0;
	}

	public static ItemStack createEmpty() {
		ItemStack stack = new ItemStack(ModTools.LEATHER_SKIN);
		stack.setDamageValue(CAPACITY);
		return stack;
	}

	@Override
	public @Nullable ItemStackTemplate getCraftingRemainder(ItemStack stack) {
		ItemStack leftover = stack.copyWithCount(1);
		if (hasWater(leftover)) {
			leftover.setDamageValue(Math.min(leftover.getDamageValue() + 1, CAPACITY));
		}
		return ItemStackTemplate.fromNonEmptyStack(leftover);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (isFull(stack)) {
			return InteractionResult.PASS;
		}

		BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		if (hit.getType() != HitResult.Type.BLOCK) {
			return InteractionResult.PASS;
		}

		BlockPos pos = hit.getBlockPos();
		if (!level.mayInteract(player, pos)) {
			return InteractionResult.PASS;
		}

		if (!level.getFluidState(pos).is(FluidTags.WATER)) {
			return InteractionResult.PASS;
		}

		fill(stack, level, player, pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		if (isFull(stack)) {
			return InteractionResult.PASS;
		}

		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (!state.is(Blocks.WATER_CAULDRON)) {
			return InteractionResult.PASS;
		}

		Player player = context.getPlayer();
		if (player != null && !level.mayInteract(player, pos)) {
			return InteractionResult.PASS;
		}

		fill(stack, level, player, pos);
		if (!level.isClientSide()) {
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
		}
		return InteractionResult.SUCCESS;
	}

	private void fill(ItemStack stack, Level level, @Nullable Player player, BlockPos pos) {
		if (!level.isClientSide()) {
			stack.setDamageValue(0);
		}

		level.playSound(
				player,
				pos,
				SoundEvents.BOTTLE_FILL,
				SoundSource.NEUTRAL,
				1.0F,
				1.0F
		);
		level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);

		if (player != null) {
			player.awardStat(Stats.ITEM_USED.get(this));
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round(13.0F * getWaterUses(stack) / (float) CAPACITY);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return 0x3F76E4;
	}

	@Override
	public void appendHoverText(
			ItemStack stack,
			TooltipContext context,
			TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder,
			TooltipFlag flag
	) {
		tooltipAdder.accept(Component.translatable(
				"item.from-stone-to-stars.leather_skin.uses",
				getWaterUses(stack),
				CAPACITY
		));
	}
}
