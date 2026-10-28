package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.ModBlockEntities;
import jaymanh.fsts.block.entity.WoodenShaftBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WoodenShaftBlock extends RotationNetworkBlock {
	public static final MapCodec<WoodenShaftBlock> CODEC = simpleCodec(WoodenShaftBlock::new);
	private static final VoxelShape SHAPE_X = Block.box(0.0, 5.0, 5.0, 16.0, 11.0, 11.0);
	private static final VoxelShape SHAPE_Y = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
	private static final VoxelShape SHAPE_Z = Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 16.0);

	public WoodenShaftBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState()
				.setValue(BlockStateProperties.AXIS, Direction.Axis.Y)
				.setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected MapCodec<? extends WoodenShaftBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.AXIS, BlockStateProperties.POWERED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getNearestLookingDirection();
		return this.defaultBlockState()
				.setValue(BlockStateProperties.AXIS, direction.getAxis())
				.setValue(BlockStateProperties.POWERED, false);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(BlockStateProperties.AXIS)) {
			case X -> SHAPE_X;
			case Y -> SHAPE_Y;
			case Z -> SHAPE_Z;
		};
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new WoodenShaftBlockEntity(pos, state);
	}
}
