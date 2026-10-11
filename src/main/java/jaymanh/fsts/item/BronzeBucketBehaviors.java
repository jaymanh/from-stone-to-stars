package jaymanh.fsts.item;

import java.util.Optional;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;

/**
 * Keeps bronze buckets on the bronze item set. Vanilla pickup results are iron buckets.
 */
public final class BronzeBucketBehaviors {
	private BronzeBucketBehaviors() {
	}

	public static void registerDispensers() {
		DispenserBlock.registerBehavior(ModItems.BRONZE_BUCKET, new DefaultDispenseItemBehavior() {
			@Override
			public ItemStack execute(BlockSource source, ItemStack dispensed) {
				LevelAccessor level = source.level();
				BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
				BlockState state = level.getBlockState(target);
				if (!(state.getBlock() instanceof BucketPickup pickup)) {
					return super.execute(source, dispensed);
				}

				ItemStack taken = remapContents(dispensed, pickup.pickupBlock(null, level, target, state));
				if (taken.isEmpty()) {
					return super.execute(source, dispensed);
				}

				level.gameEvent(null, GameEvent.FLUID_PICKUP, target);
				return this.consumeWithRemainder(source, dispensed, taken);
			}
		});

		DispenseItemBehavior place = new DefaultDispenseItemBehavior() {
			@Override
			public ItemStack execute(BlockSource source, ItemStack dispensed) {
				if (!(dispensed.getItem() instanceof DispensibleContainerItem bucket)) {
					return super.execute(source, dispensed);
				}

				BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
				Level level = source.level();
				if (!bucket.emptyContents(null, level, target, null)) {
					return super.execute(source, dispensed);
				}

				bucket.checkExtraContent(null, level, dispensed, target);
				return this.consumeWithRemainder(source, dispensed, new ItemStack(ModItems.BRONZE_BUCKET));
			}
		};
		DispenserBlock.registerBehavior(ModItems.BRONZE_WATER_BUCKET, place);
		DispenserBlock.registerBehavior(ModItems.BRONZE_LAVA_BUCKET, place);
		DispenserBlock.registerBehavior(ModItems.BRONZE_POWDER_SNOW_BUCKET, place);
		DispenserBlock.registerBehavior(ModItems.BRONZE_COD_BUCKET, place);
		DispenserBlock.registerBehavior(ModItems.BRONZE_SALMON_BUCKET, place);
		DispenserBlock.registerBehavior(ModItems.BRONZE_PUFFERFISH_BUCKET, place);
		DispenserBlock.registerBehavior(ModItems.BRONZE_TROPICAL_FISH_BUCKET, place);
		DispenserBlock.registerBehavior(ModItems.BRONZE_AXOLOTL_BUCKET, place);
		DispenserBlock.registerBehavior(ModItems.BRONZE_TADPOLE_BUCKET, place);
		DispenserBlock.registerBehavior(ModItems.BRONZE_SULFUR_CUBE_BUCKET, place);
	}

	public static boolean emptiesToBronze(Item item) {
		return item == ModItems.BRONZE_WATER_BUCKET
				|| item == ModItems.BRONZE_LAVA_BUCKET
				|| item == ModItems.BRONZE_POWDER_SNOW_BUCKET
				|| item == ModItems.BRONZE_MILK_BUCKET
				|| item == ModItems.BRONZE_COD_BUCKET
				|| item == ModItems.BRONZE_SALMON_BUCKET
				|| item == ModItems.BRONZE_PUFFERFISH_BUCKET
				|| item == ModItems.BRONZE_TROPICAL_FISH_BUCKET
				|| item == ModItems.BRONZE_AXOLOTL_BUCKET
				|| item == ModItems.BRONZE_TADPOLE_BUCKET
				|| item == ModItems.BRONZE_SULFUR_CUBE_BUCKET;
	}

	/**
	 * Swaps an iron bucket result for the bronze one when the stack in hand is bronze.
	 */
	public static ItemStack remapContents(ItemStack hand, ItemStack result) {
		if (!hand.is(ModItems.BRONZE_BUCKET) && !emptiesToBronze(hand.getItem())) {
			return result;
		}
		if (result.is(Items.WATER_BUCKET)) {
			return new ItemStack(ModItems.BRONZE_WATER_BUCKET);
		}
		if (result.is(Items.LAVA_BUCKET)) {
			return new ItemStack(ModItems.BRONZE_LAVA_BUCKET);
		}
		if (result.is(Items.POWDER_SNOW_BUCKET)) {
			return new ItemStack(ModItems.BRONZE_POWDER_SNOW_BUCKET);
		}
		if (result.is(Items.BUCKET)) {
			return new ItemStack(ModItems.BRONZE_BUCKET);
		}
		Item mobBucket = mobEquivalent(result.getItem());
		if (mobBucket != null) {
			return result.transmuteCopy(mobBucket);
		}
		return result;
	}

	public static Item mobEquivalent(Item vanillaBucket) {
		if (vanillaBucket == Items.COD_BUCKET) {
			return ModItems.BRONZE_COD_BUCKET;
		}
		if (vanillaBucket == Items.SALMON_BUCKET) {
			return ModItems.BRONZE_SALMON_BUCKET;
		}
		if (vanillaBucket == Items.PUFFERFISH_BUCKET) {
			return ModItems.BRONZE_PUFFERFISH_BUCKET;
		}
		if (vanillaBucket == Items.TROPICAL_FISH_BUCKET) {
			return ModItems.BRONZE_TROPICAL_FISH_BUCKET;
		}
		if (vanillaBucket == Items.AXOLOTL_BUCKET) {
			return ModItems.BRONZE_AXOLOTL_BUCKET;
		}
		if (vanillaBucket == Items.TADPOLE_BUCKET) {
			return ModItems.BRONZE_TADPOLE_BUCKET;
		}
		if (vanillaBucket == Items.SULFUR_CUBE_BUCKET) {
			return ModItems.BRONZE_SULFUR_CUBE_BUCKET;
		}
		return null;
	}

	/**
	 * @return null when this hand should fall through to the vanilla pickup check.
	 * Otherwise the pickup result for a bronze water or empty bucket.
	 */
	public static @Nullable Optional<InteractionResult> tryBucketMob(Player player, InteractionHand hand, LivingEntity entity) {
		ItemStack held = player.getItemInHand(hand);
		if (!(entity instanceof Bucketable bucketable) || !entity.isAlive()) {
			return null;
		}

		ItemStack probe;
		if (held.is(ModItems.BRONZE_WATER_BUCKET)) {
			probe = new ItemStack(Items.WATER_BUCKET);
		} else if (held.is(ModItems.BRONZE_BUCKET)) {
			probe = new ItemStack(Items.BUCKET);
		} else {
			return null;
		}
		if (!bucketable.canBePickedUpWithBucket(probe)) {
			return null;
		}

		Item bronze = mobEquivalent(bucketable.getBucketItemStack().getItem());
		if (bronze == null) {
			return null;
		}

		entity.playSound(bucketable.getPickupSound(), 1.0F, 1.0F);
		ItemStack filled = new ItemStack(bronze);
		bucketable.saveToBucketTag(filled);
		player.setItemInHand(hand, ItemUtils.createFilledResult(held, player, filled, false));
		if (!entity.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
			CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, filled);
		}
		if (entity instanceof Leashable leashable) {
			leashable.dropLeash();
		}
		entity.discard();
		return Optional.of(InteractionResult.SUCCESS);
	}

	/**
	 * @return null when the stack is not a bronze bucket, so the cauldron keeps its own behavior.
	 */
	public static InteractionResult interactWithCauldron(
			BlockState state,
			Level level,
			BlockPos pos,
			Player player,
			InteractionHand hand,
			ItemStack stack
	) {
		if (stack.is(ModItems.BRONZE_BUCKET)) {
			if (state.is(Blocks.WATER_CAULDRON)) {
				return fillFromCauldron(state, level, pos, player, hand, stack, new ItemStack(ModItems.BRONZE_WATER_BUCKET), true, SoundEvents.BUCKET_FILL);
			}
			if (state.is(Blocks.LAVA_CAULDRON)) {
				return fillFromCauldron(state, level, pos, player, hand, stack, new ItemStack(ModItems.BRONZE_LAVA_BUCKET), false, SoundEvents.BUCKET_FILL_LAVA);
			}
			if (state.is(Blocks.POWDER_SNOW_CAULDRON)) {
				return fillFromCauldron(state, level, pos, player, hand, stack, new ItemStack(ModItems.BRONZE_POWDER_SNOW_BUCKET), true, SoundEvents.BUCKET_FILL_POWDER_SNOW);
			}
			return null;
		}
		if (stack.is(ModItems.BRONZE_WATER_BUCKET)) {
			return emptyIntoCauldron(level, pos, player, hand, stack, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);
		}
		if (stack.is(ModItems.BRONZE_LAVA_BUCKET)) {
			if (isUnderWater(level, pos)) {
				return InteractionResult.CONSUME;
			}
			return emptyIntoCauldron(level, pos, player, hand, stack, Blocks.LAVA_CAULDRON.defaultBlockState(), SoundEvents.BUCKET_EMPTY_LAVA);
		}
		if (stack.is(ModItems.BRONZE_POWDER_SNOW_BUCKET)) {
			if (isUnderWater(level, pos)) {
				return InteractionResult.CONSUME;
			}
			return emptyIntoCauldron(
					level,
					pos,
					player,
					hand,
					stack,
					Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3),
					SoundEvents.BUCKET_EMPTY_POWDER_SNOW
			);
		}
		return null;
	}

	private static InteractionResult fillFromCauldron(
			BlockState state,
			Level level,
			BlockPos pos,
			Player player,
			InteractionHand hand,
			ItemStack stack,
			ItemStack filled,
			boolean needsFullLayer,
			SoundEvent sound
	) {
		if (needsFullLayer && state.getValue(LayeredCauldronBlock.LEVEL) != 3) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (!level.isClientSide()) {
			Item used = stack.getItem();
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, filled));
			player.awardStat(Stats.USE_CAULDRON);
			player.awardStat(Stats.ITEM_USED.get(used));
			level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
			level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
		}
		return InteractionResult.SUCCESS;
	}

	private static InteractionResult emptyIntoCauldron(
			Level level,
			BlockPos pos,
			Player player,
			InteractionHand hand,
			ItemStack stack,
			BlockState filledState,
			SoundEvent sound
	) {
		if (!level.isClientSide()) {
			Item used = stack.getItem();
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.BRONZE_BUCKET)));
			player.awardStat(Stats.FILL_CAULDRON);
			player.awardStat(Stats.ITEM_USED.get(used));
			level.setBlockAndUpdate(pos, filledState);
			level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
		}
		return InteractionResult.SUCCESS;
	}

	private static boolean isUnderWater(Level level, BlockPos pos) {
		FluidState fluid = level.getFluidState(pos.above());
		return fluid.is(FluidTags.WATER);
	}
}
