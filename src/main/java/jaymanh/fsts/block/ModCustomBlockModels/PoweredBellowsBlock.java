package jaymanh.fsts.block.ModCustomBlockModels;

import java.util.Map;
import java.util.function.Function;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.ModBlockEntities;
import jaymanh.fsts.block.entity.PoweredBellowsBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

/**
 * Steel-framed bellows for the forge's bellows bank. A shaft into the back works it.
 * While it spins, the forge burn climbs to the coal-pad max and holds there.
 */
public class PoweredBellowsBlock extends RotationNetworkBlock {
	public static final MapCodec<PoweredBellowsBlock> CODEC = simpleCodec(PoweredBellowsBlock::new);
	public static final BooleanProperty COMPRESSED = BooleanProperty.create("compressed");

	/** North-facing: nozzle to the north, crank housing and axle to the south. */
	private static final Map<Direction, VoxelShape> BODY = Shapes.rotateHorizontal(Shapes.or(
			Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0),
			Block.box(6.0, 3.0, 1.0, 10.0, 7.0, 4.0),
			Block.box(4.0, 0.0, 12.0, 12.0, 7.0, 15.0),
			Block.box(6.0, 3.0, 15.0, 10.0, 7.0, 16.0)
	));

	private final Function<BlockState, VoxelShape> shapes;

	public PoweredBellowsBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState()
				.setValue(BellowsBlock.FACING, Direction.NORTH)
				.setValue(BellowsBlock.NORTH, false)
				.setValue(BellowsBlock.EAST, false)
				.setValue(BellowsBlock.SOUTH, false)
				.setValue(BellowsBlock.WEST, false)
				.setValue(BellowsBlock.UP, false)
				.setValue(COMPRESSED, false));
		this.shapes = this.getShapeForEachState(state -> BellowsBlock.withArms(BODY.get(state.getValue(BellowsBlock.FACING)), state), COMPRESSED);
	}

	@Override
	protected MapCodec<? extends PoweredBellowsBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(
				BellowsBlock.FACING,
				BellowsBlock.NORTH,
				BellowsBlock.EAST,
				BellowsBlock.SOUTH,
				BellowsBlock.WEST,
				BellowsBlock.UP,
				COMPRESSED
		);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		return BellowsBlock.withConnections(
				this.defaultBlockState().setValue(BellowsBlock.FACING, context.getHorizontalDirection()),
				context.getLevel(),
				context.getClickedPos()
		);
	}

	@Override
	protected BlockState updateShape(
			BlockState state,
			LevelReader level,
			ScheduledTickAccess scheduledTicks,
			BlockPos pos,
			Direction direction,
			BlockPos neighborPos,
			BlockState neighborState,
			RandomSource random
	) {
		return BellowsBlock.updateConnection(state, direction, neighborState);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shapes.apply(state);
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return BellowsBlock.rotateBellows(state, rotation);
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return BellowsBlock.mirrorBellows(state, mirror);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		return BellowsBlock.pumpByHand(state, level, pos);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PoweredBellowsBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide()
				? null
				: createTickerHelper(type, ModBlockEntities.POWERED_BELLOWS, PoweredBellowsBlockEntity::serverTick);
	}
}
