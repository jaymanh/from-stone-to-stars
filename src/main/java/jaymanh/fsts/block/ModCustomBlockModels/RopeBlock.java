package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RopeBlock extends Block {
    public static final MapCodec<RopeBlock> CODEC = simpleCodec(RopeBlock::new);
    public RopeBlock(Properties properties) {
        super(properties);
    }
    protected static final VoxelShape SHAPE = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends RopeBlock> codec() {
        return CODEC;
    }
}
