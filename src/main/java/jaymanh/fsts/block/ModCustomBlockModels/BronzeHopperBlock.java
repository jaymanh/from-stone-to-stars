package jaymanh.fsts.block.ModCustomBlockModels;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.BronzeHopperBlockEntity;
import jaymanh.fsts.block.entity.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

/**
 * Bronze hopper. The spout faces the output. A turning shaft powers it; redstone does not.
 */
public class BronzeHopperBlock extends RotationNetworkBlock {
	public static final MapCodec<BronzeHopperBlock> CODEC = simpleCodec(BronzeHopperBlock::new);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING_HOPPER;

	private final Function<BlockState, VoxelShape> shapes;
	private final Map<Direction, VoxelShape> interactionShapes;

	public BronzeHopperBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.DOWN));
		VoxelShape inside = Block.column(12.0, 11.0, 16.0);
		this.shapes = this.makeShapes(inside);
		this.interactionShapes = ImmutableMap.<Direction, VoxelShape>builderWithExpectedSize(5)
				.putAll(Shapes.rotateHorizontal(Shapes.or(inside, Block.boxZ(4.0, 8.0, 10.0, 0.0, 4.0))))
				.put(Direction.DOWN, inside)
				.build();
	}

	private Function<BlockState, VoxelShape> makeShapes(VoxelShape inside) {
		VoxelShape spoutlessOutline = Shapes.or(Block.column(16.0, 10.0, 16.0), Block.column(8.0, 4.0, 10.0));
		VoxelShape spoutless = Shapes.join(spoutlessOutline, inside, BooleanOp.ONLY_FIRST);
		Map<Direction, VoxelShape> spouts = Shapes.rotateAll(
				Block.boxZ(4.0, 4.0, 8.0, 0.0, 8.0),
				new Vec3(8.0, 6.0, 8.0).scale(0.0625)
		);
		return this.getShapeForEachState(state -> Shapes.or(
				spoutless,
				Shapes.join(spouts.get(state.getValue(FACING)), Shapes.block(), BooleanOp.AND)
		));
	}

	@Override
	protected MapCodec<? extends BronzeHopperBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace().getOpposite();
		if (direction.getAxis() == Direction.Axis.Y) {
			direction = Direction.DOWN;
		}
		return this.defaultBlockState().setValue(FACING, direction);
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
	protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return this.interactionShapes.get(state.getValue(FACING));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BronzeHopperBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide()
				? null
				: createTickerHelper(type, ModBlockEntities.BRONZE_HOPPER, BronzeHopperBlockEntity::serverTick);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!level.isClientSide() && level.getBlockEntity(pos) instanceof BronzeHopperBlockEntity hopper) {
			player.openMenu(hopper);
			player.awardStat(Stats.INSPECT_HOPPER);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
		Containers.updateNeighboursAfterDestroy(state, level, pos);
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void entityInside(
			BlockState state,
			Level level,
			BlockPos pos,
			Entity entity,
			InsideBlockEffectApplier effectApplier,
			boolean isPrecise
	) {
		if (level.getBlockEntity(pos) instanceof BronzeHopperBlockEntity hopper) {
			hopper.captureInside(level, pos, state, entity);
		}
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
}
