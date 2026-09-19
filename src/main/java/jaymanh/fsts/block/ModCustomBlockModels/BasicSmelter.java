package jaymanh.fsts.block.ModCustomBlockModels;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.BasicSmelterEntity;
import jaymanh.fsts.block.entity.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BasicSmelter extends AbstractFurnaceBlock {
	public static final MapCodec<BasicSmelter> CODEC = simpleCodec(BasicSmelter::new);

	public BasicSmelter(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BasicSmelter> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BasicSmelterEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide()
				? null
				: createTickerHelper(type, ModBlockEntities.BASIC_SMELTER, BasicSmelterEntity::serverTick);
	}

	@Override
	protected void openContainer(Level level, BlockPos pos, Player player) {
		if (level.getBlockEntity(pos) instanceof BasicSmelterEntity smelter) {
			player.openMenu(smelter);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (!state.getValue(LIT)) {
			return;
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
		level.addParticle(ParticleTypes.FLAME, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
	}
}
