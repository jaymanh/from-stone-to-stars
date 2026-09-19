package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.MortarAndPestleEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MortarAndPestle extends BaseEntityBlock {
	public static final MapCodec<MortarAndPestle> CODEC = simpleCodec(MortarAndPestle::new);

	/** Grind clicks completed on the current wheat (0–3). Completes at 4. */
	public static final IntegerProperty PROGRESS = IntegerProperty.create("progress", 0, MortarAndPestleEntity.GRIND_CLICKS - 1);

	private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0);

	public MortarAndPestle(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(PROGRESS, 0));
	}

	@Override
	protected MapCodec<? extends MortarAndPestle> codec() {
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
		return new MortarAndPestleEntity(pos, state);
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
		if (!(level.getBlockEntity(pos) instanceof MortarAndPestleEntity mortar)) {
			return InteractionResult.PASS;
		}

		if (stack.is(Items.WHEAT)) {
			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			return mortar.insertWheat(stack, player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}

		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!(level.getBlockEntity(pos) instanceof MortarAndPestleEntity mortar)) {
			return InteractionResult.PASS;
		}

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		if (player.isShiftKeyDown()) {
			return mortar.extractAll(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}

		if (mortar.takeFlour(player)) {
			return InteractionResult.SUCCESS;
		}

		if (mortar.grind()) {
			level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.4F, 1.2F);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}
}
