package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.StoragePotBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

/**
 * Fired clay storage: barrel-sized inventory. Needs a support block or falls and shatters.
 * Only drops itself when mined with silk touch; otherwise shatters and spills contents.
 */
public class StoragePotBlock extends Block implements EntityBlock, Fallable {
	public static final MapCodec<StoragePotBlock> CODEC = simpleCodec(StoragePotBlock::new);

	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

	public StoragePotBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends StoragePotBlock> codec() {
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
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos below = pos.below();
		return Block.canSupportCenter(level, below, Direction.UP)
				|| level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = this.defaultBlockState();
		return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
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
		if (!state.canSurvive(level, pos)) {
			scheduledTicks.scheduleTick(pos, this, 2);
		}
		return state;
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
		if (!state.canSurvive(level, pos)) {
			level.scheduleTick(pos, this, 2);
		}
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.canSurvive(level, pos)) {
			return;
		}

		if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinY()) {
			// Vanilla drops a falling block as an item when it cannot be placed (torch, bottom slab)
			// or when the fall times out. That item skips silk touch, so never let the pot drop.
			FallingBlockEntity entity = FallingBlockEntity.fall(level, pos, state);
			entity.dropItem = false;
			entity.disableDrop();
			return;
		}

		shatter(level, pos, state);
	}

	@Override
	public void onLand(Level level, BlockPos pos, BlockState fallingState, BlockState replaced, FallingBlockEntity entity) {
		if (!level.isClientSide()) {
			shatter(level, pos, fallingState);
		}
	}

	@Override
	public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
		if (!level.isClientSide()) {
			// The pot was never placed here. Do not remove the torch, slab, or other block it landed on.
			playShatterEffects(level, pos, fallingBlock.getBlockState());
		}
	}

	public static void shatter(Level level, BlockPos pos, BlockState state) {
		level.removeBlock(pos, false);
		playShatterEffects(level, pos, state);
	}

	private static void playShatterEffects(Level level, BlockPos pos, BlockState state) {
		level.playSound(null, pos, SoundEvents.DECORATED_POT_BREAK, SoundSource.BLOCKS, 1.0F, 0.9F);
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(
					new BlockParticleOption(ParticleTypes.BLOCK, state),
					pos.getX() + 0.5,
					pos.getY() + 0.5,
					pos.getZ() + 0.5,
					20,
					0.25,
					0.25,
					0.25,
					0.05
			);
		}
	}

	public static boolean isFree(BlockState state) {
		return state.isAir() || state.liquid() || state.canBeReplaced();
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		if (level.getBlockEntity(pos) instanceof StoragePotBlockEntity pot) {
			player.openMenu(pot);
		}
		return InteractionResult.CONSUME;
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
		Containers.updateNeighboursAfterDestroy(state, level, pos);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new StoragePotBlockEntity(pos, state);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(16) == 0 && isFree(level.getBlockState(pos.below()))) {
			double x = pos.getX() + random.nextDouble();
			double y = pos.getY() - 0.05;
			double z = pos.getZ() + random.nextDouble();
			level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), x, y, z, 0.0, 0.0, 0.0);
		}
	}
}
