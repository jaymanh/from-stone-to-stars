package jaymanh.fsts.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

/**
 * Forge input. Players and hoppers fill it from any face; the lit forge's master hearth draws from it.
 * Hoppers cannot pull from it.
 */
public class ForgeInputBlockEntity extends ForgeStoreBlockEntity {
	public ForgeInputBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FORGE_INPUT, pos, state);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.from-stone-to-stars.forge_input");
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
		return this.canPlaceItem(slot, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return false;
	}
}
