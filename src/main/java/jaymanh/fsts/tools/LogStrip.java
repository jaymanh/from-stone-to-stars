package jaymanh.fsts.tools;

import jaymanh.fsts.item.ModItems;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LogStrip {
    public static void initlise(){
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

    }
}
