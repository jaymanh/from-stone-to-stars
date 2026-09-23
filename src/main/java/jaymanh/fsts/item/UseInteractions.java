package jaymanh.fsts.item;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.ModCustomBlockModels.PlacedBoneSawBlock;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
    }
}
