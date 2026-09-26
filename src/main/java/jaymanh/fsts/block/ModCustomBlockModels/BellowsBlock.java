package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.multiblock.FacingNeighborRule;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.process.ForgeProcess;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Must face the forge hearth (within 2 blocks) for the fuzzy forge assembly to form.
 * Empty-hand click while the forge is lit pumps air and extends burn time.
 */
public class BellowsBlock extends Block {
	public static final MapCodec<BellowsBlock> CODEC = simpleCodec(BellowsBlock::new);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final int FORGE_SEARCH = 2;

	private static final VoxelShape SHAPE_NS = Block.box(2.0, 0.0, 4.0, 14.0, 12.0, 12.0);
	private static final VoxelShape SHAPE_EW = Block.box(4.0, 0.0, 2.0, 12.0, 12.0, 14.0);

	public BellowsBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends BellowsBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(FACING).getAxis() == Direction.Axis.Z ? SHAPE_NS : SHAPE_EW;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		// Face the way the player is looking so you can place from outside the forge toward the hearth.
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
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
