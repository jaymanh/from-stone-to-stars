package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.entity.SawArborBlockEntity;
import jaymanh.fsts.block.entity.SawmillPortBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Log intake or plank outfeed of a sawmill. Every port of the same kind shares
 * the saw arbor's buffer, so hoppers and players can use any of them.
 */
public class SawmillPortBlock extends BaseEntityBlock {
	public static final MapCodec<SawmillPortBlock> CODEC = simpleCodec(SawmillPortBlock::new);
	/** Direction logs travel through the mill; set by the saw arbor when formed. */
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	/** True while the sawmill is cutting; drives the client animation. */
	public static final BooleanProperty WORKING = BooleanProperty.create("working");

	public SawmillPortBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WORKING, false));
	}

	@Override
	protected MapCodec<? extends SawmillPortBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WORKING);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SawmillPortBlockEntity(pos, state);
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
		if (stack.isEmpty() || !state.is(ModBlocks.LOG_INTAKE)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		SawArborBlockEntity arbor = arborFor(level, pos);
		if (arbor == null) {
			notFormed(player);
			return InteractionResult.SUCCESS;
		}
		if (!SawArborBlockEntity.isSawable(stack)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		int moved = arbor.insertInput(stack, player.hasInfiniteMaterials());
		return moved > 0 ? InteractionResult.SUCCESS : InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		SawArborBlockEntity arbor = arborFor(level, pos);
		if (arbor == null) {
			notFormed(player);
			return InteractionResult.SUCCESS;
		}
		if (state.is(ModBlocks.PLANK_OUTFEED) && arbor.takeAllOutput(player)) {
			return InteractionResult.SUCCESS;
		}
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.sendSystemMessage(arbor.statusMessage(), true);
		}
		return InteractionResult.SUCCESS;
	}

	private static SawArborBlockEntity arborFor(Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof SawmillPortBlockEntity port ? port.arbor() : null;
	}

	private static void notFormed(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.sendSystemMessage(Component.translatable("multiblock.from-stone-to-stars.sawmill.port_unformed"), true);
		}
	}
}
