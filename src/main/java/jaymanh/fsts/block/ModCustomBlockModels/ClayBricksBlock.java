package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClayBricksBlock extends Block {
	public static final MapCodec<ClayBricksBlock> CODEC = simpleCodec(ClayBricksBlock::new);
	public static final IntegerProperty QUANTITY = BlockStateProperties.SEGMENT_AMOUNT;

	protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0);

	public ClayBricksBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(QUANTITY, 1));
	}

	@Override
	protected MapCodec<? extends ClayBricksBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(QUANTITY);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(QUANTITY, 1);
	}

	@Override
	protected InteractionResult useItemOn(
			ItemStack stack,
			BlockState state,
			Level level,
			BlockPos pos,
			Player player,
			InteractionHand hand,
			BlockHitResult hit
	) {
		if (!stack.is(ModBlocks.CLAY_BRICKS.asItem()) && !stack.is(ModBlocks.FIRED_CLAY_BRICKS.asItem())) {
			return InteractionResult.PASS;
		}
		if (!stack.is(state.getBlock().asItem())) {
			return InteractionResult.PASS;
		}

		if (state.getValue(QUANTITY) >= 3) {
			return InteractionResult.PASS;
		}

		level.setBlockAndUpdate(pos, state.setValue(QUANTITY, state.getValue(QUANTITY) + 1));
		if (!player.hasInfiniteMaterials()) {
			stack.shrink(1);
		}

		return InteractionResult.SUCCESS;
	}
}
