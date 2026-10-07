package jaymanh.fsts.block.ModCustomBlockModels;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.block.entity.BronzeChestBlockEntity;
import jaymanh.fsts.block.entity.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import org.jspecify.annotations.Nullable;

/**
 * Wooden chest banded in bronze. Pairs with another bronze chest the same way a vanilla chest does.
 * It does not connect to a vanilla chest.
 */
public class BronzeChestBlock extends ChestBlock {
	public static final MapCodec<BronzeChestBlock> CODEC = simpleCodec(BronzeChestBlock::new);

	public BronzeChestBlock(Properties properties) {
		super(() -> ModBlockEntities.BRONZE_CHEST, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties);
	}

	@Override
	public MapCodec<? extends BronzeChestBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BronzeChestBlockEntity(pos, state);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		MenuProvider provider = super.getMenuProvider(state, level, pos);
		if (provider == null || state.getValue(TYPE) == ChestType.SINGLE) {
			return provider;
		}
		Component title = customName(level, pos);
		if (title == null) {
			title = customName(level, getConnectedBlockPos(pos, state));
		}
		if (title == null) {
			title = Component.translatable("container.from-stone-to-stars.bronze_chest_double");
		}
		Component shown = title;
		return new SimpleMenuProvider((id, inventory, player) -> provider.createMenu(id, inventory, player), shown);
	}

	private static @Nullable Component customName(Level level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof Nameable nameable) {
			return nameable.getCustomName();
		}
		return null;
	}
}
