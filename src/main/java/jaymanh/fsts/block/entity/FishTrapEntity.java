package jaymanh.fsts.block.entity;

import jaymanh.fsts.tags.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootTable;

public class FishTrapEntity extends SnareBlockEntity {
	public FishTrapEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FISH_TRAP, pos, state);
	}

	public static boolean hasWaterContact(LevelReader level, BlockPos pos) {
		if (level.getFluidState(pos).is(FluidTags.WATER)) {
			return true;
		}
		for (Direction direction : Direction.values()) {
			if (level.getFluidState(pos.relative(direction)).is(FluidTags.WATER)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isBait(ItemStack stack) {
		return !stack.isEmpty() && stack.is(ModTags.FISHING_BAIT);
	}

	@Override
	protected boolean canCatch(ServerLevel level) {
		BlockState state = this.getBlockState();
		if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
			return true;
		}
		return hasWaterContact(level, this.worldPosition);
	}

	@Override
	protected ResourceKey<LootTable> getCatchLootTable(ItemStack bait) {
		return ModTags.FISH_TRAP_LOOT;
	}

	@Override
	protected void playCatchSound(ServerLevel level) {
		level.playSound(null, this.worldPosition, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.BLOCKS, 0.4F, 1.0F);
	}
}
