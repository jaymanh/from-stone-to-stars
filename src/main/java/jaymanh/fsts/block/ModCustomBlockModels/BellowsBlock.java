package jaymanh.fsts.block.ModCustomBlockModels;

import java.util.Map;
import java.util.function.Function;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.advancement.AgeAdvancements;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.multiblock.FacingNeighborRule;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.process.ForgeProcess;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Must face the forge hearth (within 2 blocks) for the forge assembly to form.
 * Empty-hand click while the forge is lit pumps air and extends burn time.
 * Connects to adjacent bellows with the same facing, like a fence.
 */
public class BellowsBlock extends Block {
	public static final MapCodec<BellowsBlock> CODEC = simpleCodec(BellowsBlock::new);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;
	public static final BooleanProperty UP = BlockStateProperties.UP;

	private static final int FORGE_SEARCH = 2;

	/** Matches bellows_core bag element. */
	private static final VoxelShape CORE = Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);
	/** Matches bellows_side (north) element. */
	private static final VoxelShape ARM_NORTH = Block.box(5.0, 2.0, 0.0, 11.0, 8.0, 4.0);
	private static final VoxelShape ARM_SOUTH = Block.box(5.0, 2.0, 12.0, 11.0, 8.0, 16.0);
	private static final VoxelShape ARM_WEST = Block.box(0.0, 2.0, 5.0, 4.0, 8.0, 11.0);
	private static final VoxelShape ARM_EAST = Block.box(12.0, 2.0, 5.0, 16.0, 8.0, 11.0);
	/** Matches bellows_up element. */
	private static final VoxelShape ARM_UP = Block.box(5.0, 10.0, 5.0, 11.0, 16.0, 11.0);
	/** Matches bellows_core nozzle (north-facing default). */
	private static final Map<Direction, VoxelShape> NOZZLE = Map.of(
			Direction.NORTH, Block.box(6.0, 3.0, 1.0, 10.0, 7.0, 4.0),
			Direction.SOUTH, Block.box(6.0, 3.0, 12.0, 10.0, 7.0, 15.0),
			Direction.WEST, Block.box(1.0, 3.0, 6.0, 4.0, 7.0, 10.0),
			Direction.EAST, Block.box(12.0, 3.0, 6.0, 15.0, 7.0, 10.0)
	);

	private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = Map.of(
			Direction.NORTH, NORTH,
			Direction.EAST, EAST,
			Direction.SOUTH, SOUTH,
			Direction.WEST, WEST,
			Direction.UP, UP
	);

	private final Function<BlockState, VoxelShape> shapes;

	public BellowsBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState()
				.setValue(FACING, Direction.NORTH)
				.setValue(NORTH, false)
				.setValue(EAST, false)
				.setValue(SOUTH, false)
				.setValue(WEST, false)
				.setValue(UP, false));
		this.shapes = this.getShapeForEachState(this::shapeFor);
	}

	@Override
	protected MapCodec<? extends BellowsBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, NORTH, EAST, SOUTH, WEST, UP);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shapes.apply(state);
	}

	private VoxelShape shapeFor(BlockState state) {
		VoxelShape shape = Shapes.or(CORE, NOZZLE.get(state.getValue(FACING)));
		if (state.getValue(NORTH)) {
			shape = Shapes.or(shape, ARM_NORTH);
		}
		if (state.getValue(EAST)) {
			shape = Shapes.or(shape, ARM_EAST);
		}
		if (state.getValue(SOUTH)) {
			shape = Shapes.or(shape, ARM_SOUTH);
		}
		if (state.getValue(WEST)) {
			shape = Shapes.or(shape, ARM_WEST);
		}
		if (state.getValue(UP)) {
			shape = Shapes.or(shape, ARM_UP);
		}
		return shape;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction facing = context.getHorizontalDirection();
		return this.defaultBlockState()
				.setValue(FACING, facing)
				.setValue(NORTH, connectsTo(level.getBlockState(pos.north()), facing))
				.setValue(EAST, connectsTo(level.getBlockState(pos.east()), facing))
				.setValue(SOUTH, connectsTo(level.getBlockState(pos.south()), facing))
				.setValue(WEST, connectsTo(level.getBlockState(pos.west()), facing))
				.setValue(UP, connectsTo(level.getBlockState(pos.above()), facing));
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
		BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
		if (property == null) {
			return state;
		}
		return state.setValue(property, connectsTo(neighborState, state.getValue(FACING)));
	}

	private static boolean connectsTo(BlockState neighbor, Direction facing) {
		return neighbor.getBlock() instanceof BellowsBlock
				&& neighbor.getValue(FACING) == facing;
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		Direction facing = rotation.rotate(state.getValue(FACING));
		BlockState rotated = switch (rotation) {
			case CLOCKWISE_180 -> state
					.setValue(NORTH, state.getValue(SOUTH))
					.setValue(EAST, state.getValue(WEST))
					.setValue(SOUTH, state.getValue(NORTH))
					.setValue(WEST, state.getValue(EAST));
			case COUNTERCLOCKWISE_90 -> state
					.setValue(NORTH, state.getValue(EAST))
					.setValue(EAST, state.getValue(SOUTH))
					.setValue(SOUTH, state.getValue(WEST))
					.setValue(WEST, state.getValue(NORTH));
			case CLOCKWISE_90 -> state
					.setValue(NORTH, state.getValue(WEST))
					.setValue(EAST, state.getValue(NORTH))
					.setValue(SOUTH, state.getValue(EAST))
					.setValue(WEST, state.getValue(SOUTH));
			default -> state;
		};
		return rotated.setValue(FACING, facing);
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return switch (mirror) {
			case LEFT_RIGHT -> state
					.setValue(NORTH, state.getValue(SOUTH))
					.setValue(SOUTH, state.getValue(NORTH))
					.setValue(FACING, mirror.mirror(state.getValue(FACING)));
			case FRONT_BACK -> state
					.setValue(EAST, state.getValue(WEST))
					.setValue(WEST, state.getValue(EAST))
					.setValue(FACING, mirror.mirror(state.getValue(FACING)));
			default -> super.mirror(state, mirror);
		};
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.PASS;
		}

		Direction facing = state.getValue(FACING);
		ForgeHearthBlockEntity hearth = findLitForge(serverLevel, pos, facing);
		if (hearth == null) {
			return InteractionResult.PASS;
		}

		FormedMultiblock formed = hearth.getFormed();
		if (formed == null || !formed.isActive() || !(formed.process() instanceof ForgeProcess forge)) {
			return InteractionResult.PASS;
		}

		ForgeHearthBlockEntity master = hearth.getMaster();
		int added = forge.pumpBellows(formed.match());
		AgeAdvancements.grantNearby(serverLevel, pos, "age1/hotter_fire", "pumped");
		AgeAdvancements.grantNearby(serverLevel, pos, "age1/bronze_yard", "bellows");
		if (added <= 0) {
			return InteractionResult.SUCCESS;
		}

		serverLevel.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.8F, 0.7F + serverLevel.getRandom().nextFloat() * 0.2F);
		master.setChanged();
		return InteractionResult.SUCCESS;
	}

	private static ForgeHearthBlockEntity findLitForge(ServerLevel level, BlockPos bellowsPos, Direction facing) {
		for (BlockPos pos : BlockPos.betweenClosed(
				bellowsPos.offset(-FORGE_SEARCH, -FORGE_SEARCH, -FORGE_SEARCH),
				bellowsPos.offset(FORGE_SEARCH, FORGE_SEARCH, FORGE_SEARCH)
		)) {
			if (!FacingNeighborRule.facesToward(bellowsPos, facing, pos)) {
				continue;
			}
			if (level.getBlockEntity(pos) instanceof ForgeHearthBlockEntity hearth && hearth.isBurning()) {
				return hearth.getMaster();
			}
		}
		return null;
	}
}
