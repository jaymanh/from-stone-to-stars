package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Hewn stump awaiting a leather-strip lash before it becomes a {@link WorkStumpBlock}.
 */
public class UnfinishedWorkStumpBlock extends Block {
	public static final MapCodec<UnfinishedWorkStumpBlock> CODEC = simpleCodec(UnfinishedWorkStumpBlock::new);

	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 10.0, 15.0);

	public UnfinishedWorkStumpBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends UnfinishedWorkStumpBlock> codec() {
		return CODEC;
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
		if (!stack.is(ModItems.LEATHER_STRIPS)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		level.setBlockAndUpdate(pos, ModBlocks.WORK_STUMP.defaultBlockState());
		level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.BLOCKS, 1.0F, 1.0F);

		if (!player.hasInfiniteMaterials()) {
			stack.shrink(1);
		}

		return InteractionResult.SUCCESS;
	}
}
