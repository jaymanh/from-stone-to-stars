package jaymanh.fsts.item;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.ModCustomBlockModels.PlacedBoneSawBlock;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.multiblock.ActiveMultiblocks;
import jaymanh.fsts.tools.SawToolItem;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class UseInteractions {
    WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
    public static void initialise(){
        //Strip log for bark
        UseBlockCallback.EVENT.register(((player, level, hand, hitResult) -> {
            if (level.isClientSide()){
                return InteractionResult.PASS;
            }
            ItemStack tool = player.getItemInHand(hand);
            if (!(tool.getItem() instanceof AxeItem)){
                return InteractionResult.PASS;
            }

            BlockPos pos = hitResult.getBlockPos();
            Direction face = hitResult.getDirection();
            BlockState state = level.getBlockState(pos);

            if (StrippableBlockRegistry.getStrippedBlockState(state) == null) {
                return InteractionResult.PASS;
            }
            long worldSeed = state.getSeed(pos);
            RandomSource randomSource = RandomSource.create(worldSeed);
            Block.popResourceFromFace(level, pos, face, new ItemStack(ModItems.BARK,randomSource.nextInt(4)+1));
            return InteractionResult.PASS;
        }));

        // Place bone saw on top of a log
        UseBlockCallback.EVENT.register(((player, level, hand, hitResult) -> {
            ItemStack tool = player.getItemInHand(hand);
            if (!(tool.getItem() instanceof SawToolItem)){
                return InteractionResult.PASS;
            }

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);

            if (!state.is(BlockTags.LOGS)) {
                return InteractionResult.PASS;
            }

            BlockPos placePos = pos.above();
            if (!level.getBlockState(placePos).canBeReplaced()) {
                return InteractionResult.PASS;
            }

            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            BlockState sawState = ModBlocks.PLACED_BONE_SAW.defaultBlockState()
                    .setValue(PlacedBoneSawBlock.FACING, player.getDirection());
            level.setBlockAndUpdate(placePos, sawState);

            if (!player.hasInfiniteMaterials()) {
                tool.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }));

        // Hew an already-stripped log into an unfinished work stump
        UseBlockCallback.EVENT.register(((player, level, hand, hitResult) -> {
            ItemStack tool = player.getItemInHand(hand);
            if (!(tool.getItem() instanceof AxeItem)) {
                return InteractionResult.PASS;
            }

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (!state.is(BlockTags.LOGS)) {
                return InteractionResult.PASS;
            }
            // Still barkable → leave to vanilla strip. Second axe pass on stripped wood hews the stump.
            if (StrippableBlockRegistry.getStrippedBlockState(state) != null) {
                return InteractionResult.PASS;
            }

            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            level.setBlockAndUpdate(pos, ModBlocks.UNFINISHED_WORK_STUMP.defaultBlockState());
            level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 0.9F);

            if (!player.hasInfiniteMaterials()) {
                tool.hurtAndBreak(1, player, hand);
            }

            return InteractionResult.SUCCESS;
        }));

		// Vanilla brick item places the modded fired clay brick pile (full bricks block stays vanilla).
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			ItemStack stack = player.getItemInHand(hand);
			if (!stack.is(Items.BRICK)) {
				return InteractionResult.PASS;
			}

			BlockPos clickedPos = hitResult.getBlockPos();
			BlockState clickedState = level.getBlockState(clickedPos);
			// Stacking onto an existing pile is handled by ClayBricksBlock.useItemOn.
			if (clickedState.is(ModBlocks.FIRED_CLAY_BRICKS)) {
				return InteractionResult.PASS;
			}

			UseOnContext useOn = new UseOnContext(player, hand, hitResult);
			BlockPlaceContext placeContext = new BlockPlaceContext(useOn);
			BlockPos placePos = placeContext.getClickedPos();
			BlockState toPlace = ModBlocks.FIRED_CLAY_BRICKS.getStateForPlacement(placeContext);
			if (toPlace == null || !placeContext.canPlace() || !level.isUnobstructed(toPlace, placePos, CollisionContext.placementContext(player))) {
				return InteractionResult.PASS;
			}

			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}

			level.setBlock(placePos, toPlace, 11);
			SoundType sound = toPlace.getSoundType();
			level.playSound(null, placePos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
			level.gameEvent(GameEvent.BLOCK_PLACE, placePos, GameEvent.Context.of(player, toPlace));

			if (!player.hasInfiniteMaterials()) {
				stack.shrink(1);
			}

			return InteractionResult.SUCCESS;
		});
		// Forge access air gap: right-clicking through the opening opens the shared hearth UI.
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			if (!player.getItemInHand(hand).isEmpty()) {
				return InteractionResult.PASS;
			}
			var eye = player.getEyePosition(1.0F);
			var end = hitResult.getLocation();
			var delta = end.subtract(eye);
			double length = delta.length();
			if (length < 1.0E-4) {
				return InteractionResult.PASS;
			}
			var step = delta.normalize().scale(0.25);
			var cursor = eye;
			double travelled = 0.0;
			while (travelled <= length + 0.25) {
				BlockPos pos = BlockPos.containing(cursor);
				var formed = ActiveMultiblocks.get(level, pos);
				if (formed.isPresent() && level.getBlockState(pos).isAir()) {
					BlockPos origin = formed.get().match().origin();
					if (level.getBlockEntity(origin) instanceof ForgeHearthBlockEntity master) {
						if (level.isClientSide()) {
							return InteractionResult.SUCCESS;
						}
						player.openMenu(master);
						return InteractionResult.SUCCESS;
					}
				}
				cursor = cursor.add(step);
				travelled += 0.25;
			}
			return InteractionResult.PASS;
		});
    }
}
