package jaymanh.fsts.block;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.ModBlockEntities;
import jaymanh.fsts.block.entity.TanningRackBlockEntity;
import jaymanh.fsts.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TanningRackBlock extends BaseEntityBlock {
	public static final MapCodec<TanningRackBlock> CODEC = simpleCodec(TanningRackBlock::new);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<HideStage> SLOT_0 = EnumProperty.create("slot_0", HideStage.class);
	public static final EnumProperty<HideStage> SLOT_1 = EnumProperty.create("slot_1", HideStage.class);
	public static final EnumProperty<HideStage> SLOT_2 = EnumProperty.create("slot_2", HideStage.class);
	public static final EnumProperty<HideStage> SLOT_3 = EnumProperty.create("slot_3", HideStage.class);
	@SuppressWarnings("unchecked")
	public static final EnumProperty<HideStage>[] SLOTS = new EnumProperty[] { SLOT_0, SLOT_1, SLOT_2, SLOT_3 };

	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

	public TanningRackBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState()
				.setValue(FACING, Direction.NORTH)
				.setValue(SLOT_0, HideStage.EMPTY)
				.setValue(SLOT_1, HideStage.EMPTY)
				.setValue(SLOT_2, HideStage.EMPTY)
				.setValue(SLOT_3, HideStage.EMPTY));
	}

	@Override
	protected MapCodec<? extends TanningRackBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, SLOT_0, SLOT_1, SLOT_2, SLOT_3);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TanningRackBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntities.TANNING_RACK, TanningRackBlockEntity::serverTick);
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
		if (!(level.getBlockEntity(pos) instanceof TanningRackBlockEntity rack)) {
			return InteractionResult.PASS;
		}

		if (stack.is(ModItems.RAW_LEATHER)) {
			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			if (rack.insertRawLeather()) {
				if (!player.hasInfiniteMaterials()) {
					stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		}

		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!(level.getBlockEntity(pos) instanceof TanningRackBlockEntity rack)) {
			return InteractionResult.PASS;
		}

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		if (player.isShiftKeyDown()) {
			return rack.removeAll(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}

		return rack.removeOutput(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	public enum HideStage implements StringRepresentable {
		EMPTY("empty"),
		RAW("raw"),
		TANNED("tanned");

		private final String name;

		HideStage(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
}
