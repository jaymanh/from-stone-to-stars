package jaymanh.fsts.block.ModCustomBlockModels;

import jaymanh.fsts.tools.ModTools;
import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.KindlingBlockEntity;
import jaymanh.fsts.block.entity.ModBlockEntities;
import jaymanh.fsts.multiblock.ModMultiblocks;
import jaymanh.fsts.multiblock.MultiblockDefinition;
import jaymanh.fsts.multiblock.MultiblockMatch;
import jaymanh.fsts.multiblock.MultiblockMatcher;
import jaymanh.fsts.multiblock.MultiblockRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

public class KindlingBlock extends BaseEntityBlock {
	public static final MapCodec<KindlingBlock> CODEC = simpleCodec(KindlingBlock::new);
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public KindlingBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
	}

	@Override
	protected MapCodec<? extends KindlingBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new KindlingBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntities.KINDLING, KindlingBlockEntity::serverTick);
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
		if (state.getValue(LIT)) {
			return InteractionResult.PASS;
		}
		if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE) && !stack.is(ModTools.BOW_AND_DRILL)) {
			return InteractionResult.PASS;
		}

		Optional<MultiblockDefinition> charcoalDefinition = MultiblockRegistry.get(ModMultiblocks.CHARCOAL_PILE_ID);
		Optional<MultiblockDefinition> clayDefinition = MultiblockRegistry.get(ModMultiblocks.BRICK_DRYING_PIT);

		Optional<MultiblockMatch> charcoalMatch = charcoalDefinition.flatMap(definition -> MultiblockMatcher.tryMatch(level, pos, definition));
		Optional<MultiblockMatch> clayMatch = clayDefinition.flatMap(definition -> MultiblockMatcher.tryMatch(level, pos, definition));
		if (charcoalMatch.isEmpty() && clayMatch.isEmpty()) {
			return InteractionResult.PASS;
		}
		MultiblockMatch match = charcoalMatch.orElseGet(clayMatch::get);

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		if (!(level.getBlockEntity(pos) instanceof KindlingBlockEntity kindling)) {
			return InteractionResult.PASS;
		}

		if (!kindling.start(match)) {
			return InteractionResult.PASS;
		}

		level.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_ALL);
		level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);

		if (!player.hasInfiniteMaterials()) {
			if (stack.is(Items.FLINT_AND_STEEL) || stack.is(ModTools.BOW_AND_DRILL)) {
				stack.hurtAndBreak(1, (ServerLevel) level, player instanceof ServerPlayer serverPlayer ? serverPlayer : null, broken -> {
				});
			} else if (stack.is(Items.FIRE_CHARGE)) {
				stack.shrink(1);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (!state.getValue(LIT)) {
			return;
		}
		if (random.nextInt(2) == 0) {
			level.addParticle(
					ParticleTypes.LARGE_SMOKE,
					pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.6,
					pos.getY() + 0.8,
					pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.6,
					0.0,
					0.02,
					0.0
			);
		}
		if (random.nextInt(3) == 0) {
			level.addParticle(
					ParticleTypes.FLAME,
					pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4,
					pos.getY() + 0.5,
					pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4,
					0.0,
					0.01,
					0.0
			);
		}
	}
}
