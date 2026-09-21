package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;
import jaymanh.fsts.block.entity.FishTrapEntity;
import jaymanh.fsts.block.entity.ModBlockEntities;
import jaymanh.fsts.block.entity.SnareBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class FishTrapBlock extends SnareBlock{
    public static final MapCodec<FishTrapBlock> CODEC = simpleCodec(FishTrapBlock::new);
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);


    public FishTrapBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FishTrapBlock> codec() {
        return CODEC;
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FishTrapEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntities.FISH_TRAP, FishTrapEntity::serverTick);
    }

}
