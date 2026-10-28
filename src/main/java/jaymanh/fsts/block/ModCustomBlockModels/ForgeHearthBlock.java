package jaymanh.fsts.block.ModCustomBlockModels;

import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.block.entity.ModBlockEntities;
import jaymanh.fsts.multiblock.AssemblyMatch;
import jaymanh.fsts.multiblock.AssemblyMatcher;
import jaymanh.fsts.multiblock.AssemblyRegistry;
import jaymanh.fsts.item.ModItems;
import jaymanh.fsts.multiblock.ModMultiblocks;
import jaymanh.fsts.tools.ModTools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Fuzzy forge controller: blast-furnace smelting while the assembly burn stays lit.
 * Light with bow-and-drill / flint and steel / fire charge.
 */
public class ForgeHearthBlock extends AbstractFurnaceBlock {
	public static final MapCodec<ForgeHearthBlock> CODEC = simpleCodec(ForgeHearthBlock::new);
	/** Lined with a steel sheet. A lined hearth counts twice toward the forge's cook speed. */
	public static final BooleanProperty LINED = BooleanProperty.create("lined");

	private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

	public ForgeHearthBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(LINED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LINED);
	}

	@Override
	protected MapCodec<? extends ForgeHearthBlock> codec() {
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
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ForgeHearthBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide()
				? null
				: createTickerHelper(type, ModBlockEntities.FORGE_HEARTH, ForgeHearthBlockEntity::serverTick);
	}

	@Override
	protected void openContainer(Level level, BlockPos pos, Player player) {
		if (level.getBlockEntity(pos) instanceof ForgeHearthBlockEntity hearth) {
			player.openMenu(hearth.getMaster());
		}
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
		if (stack.is(ModItems.STEEL_SHEET)) {
			return line(stack, state, level, pos, player);
		}
		if (state.getValue(LIT)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE) && !stack.is(ModTools.BOW_AND_DRILL)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}

		Optional<AssemblyMatch.Attempt> attempt = AssemblyRegistry.get(ModMultiblocks.FORGE_ID)
				.map(definition -> AssemblyMatcher.attempt(level, pos, definition));
		if (attempt.isEmpty()) {
			return InteractionResult.PASS;
		}

		AssemblyMatch.Attempt result = attempt.get();
		if (!result.ok()) {
			if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
				serverPlayer.sendSystemMessage(result.failure().orElse(Component.literal("Forge incomplete")), true);
			}
			return InteractionResult.SUCCESS;
		}

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		if (!(level.getBlockEntity(pos) instanceof ForgeHearthBlockEntity hearth)) {
			return InteractionResult.PASS;
		}
		if (!hearth.start(result.match().orElseThrow())) {
			return InteractionResult.PASS;
		}

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

	private static InteractionResult line(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player) {
		if (state.getValue(LINED)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		level.setBlock(pos, state.setValue(LINED, true), Block.UPDATE_ALL);
		level.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
		stack.consume(1, player);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (!state.getValue(LIT)) {
			return;
		}
		if (random.nextInt(2) == 0) {
			level.addParticle(
					ParticleTypes.FLAME,
					pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4,
					pos.getY() + 0.55,
					pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4,
					0.0,
					0.01,
					0.0
			);
		}

		double x = pos.getX() + 0.5;
		double y = pos.getY();
		double z = pos.getZ() + 0.5;
		if (random.nextDouble() < 0.1) {
			level.playLocalSound(x, y, z, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
		}

		Direction facing = state.getValue(FACING);
		Direction.Axis axis = facing.getAxis();
		double offset = 0.52;
		double across = random.nextDouble() * 0.6 - 0.3;
		double dx = axis == Direction.Axis.X ? facing.getStepX() * offset : across;
		double dy = random.nextDouble() * 6.0 / 16.0;
		double dz = axis == Direction.Axis.Z ? facing.getStepZ() * offset : across;
		level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
	}
}
