package jaymanh.fsts.item;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static jaymanh.fsts.item.InventoryHelper.*;

public class UseInteractions {
    public static void initlise(){
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

            Block.popResourceFromFace(level, pos, face, new ItemStack(ModItems.BARK));
            return InteractionResult.PASS;
        }));

        //Use leather on log for 3x3 crafting
        UseBlockCallback.EVENT.register(((player, level, hand, hitResult) -> {
            if (level.isClientSide()){
                return InteractionResult.PASS;
            }
            ItemStack item = player.getItemInHand(hand);
            if (!(item.getItem() == Items.LEATHER)){
                return InteractionResult.PASS;
            }

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);

            if (!(state.is(BlockTags.LOGS))){
                return InteractionResult.PASS;
            }

            level.setBlockAndUpdate(pos, Blocks.CRAFTING_TABLE.defaultBlockState());

            ItemStack leather = new ItemStack(Items.LEATHER);
            int itemSlot = findItemSlot(player, leather);
            removeItemInInventory(player, itemSlot);

            return InteractionResult.PASS;

        }));
    }
}
