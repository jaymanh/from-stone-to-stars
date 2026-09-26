package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import org.jspecify.annotations.Nullable;

import jaymanh.fsts.block.entity.ForgingStumpEntity;
import jaymanh.fsts.block.entity.ModBlockEntities;
import jaymanh.fsts.tools.ModTools;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Age 1 forging station: hammer heated metal into nails, shafts, blades, and plates.
 */
public class ForgingStumpBlock extends BaseEntityBlock {
	public static final MapCodec<ForgingStumpBlock> CODEC = simpleCodec(ForgingStumpBlock::new);

	public static final int MAX_PROGRESS = 7;
	public static final IntegerProperty PROGRESS = IntegerProperty.create("progress", 0, MAX_PROGRESS);

	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 10.0, 15.0);

	public ForgingStumpBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(PROGRESS, 0));
	}

	@Override
	protected MapCodec<? extends ForgingStumpBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(PROGRESS);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ForgingStumpEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide()
				? null
				: createTickerHelper(type, ModBlockEntities.FORGING_STUMP, ForgingStumpEntity::serverTick);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
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
		if (!(level.getBlockEntity(pos) instanceof ForgingStumpEntity stump)) {
			return InteractionResult.PASS;
		}

		if (stack.isEmpty()) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		if (stack.is(ModTools.BRONZE_HAMMER)) {
			if (stump.hammer(player, stack)) {
				level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.4F, 1.2F);
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		}

		if (stump.insertInput(stack, player)) {
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!(level.getBlockEntity(pos) instanceof ForgingStumpEntity stump)) {
			return InteractionResult.PASS;
		}

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		if (player.isShiftKeyDown()) {
			return stump.extractAll(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}

		return stump.takeOutput(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}
}
