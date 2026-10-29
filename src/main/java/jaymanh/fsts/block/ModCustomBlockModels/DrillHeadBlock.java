package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.DrillHeadBlockEntity;
import jaymanh.fsts.block.entity.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;

/**
 * Bore rig controller. Use shows its status; sneak-use with an empty hand recalls the drill string.
 */
public class DrillHeadBlock extends BaseEntityBlock {
	public static final MapCodec<DrillHeadBlock> CODEC = simpleCodec(DrillHeadBlock::new);

	public DrillHeadBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DrillHeadBlock> codec() {
		return CODEC;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DrillHeadBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide()
				? null
				: createTickerHelper(type, ModBlockEntities.DRILL_HEAD, DrillHeadBlockEntity::serverTick);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!level.isClientSide()
				&& level.getBlockEntity(pos) instanceof DrillHeadBlockEntity head
				&& player instanceof ServerPlayer serverPlayer) {
			serverPlayer.sendSystemMessage(player.isSecondaryUseActive() ? head.toggleRecall() : head.statusMessage(), true);
		}
		return InteractionResult.SUCCESS;
	}
}
