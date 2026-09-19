package jaymanh.fsts.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Primitive blast furnace: blasting recipes at half speed.
 */
public class BasicSmelterEntity extends AbstractFurnaceBlockEntity {
	/** Process furnace logic every N ticks (2 = half blast-furnace speed). */
	public static final int COOK_INTERVAL = 2;

	public BasicSmelterEntity(BlockPos worldPosition, BlockState blockState) {
		super(ModBlockEntities.BASIC_SMELTER, worldPosition, blockState, RecipeType.BLASTING);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.from-stone-to-stars.basic_smelter");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new BlastFurnaceMenu(containerId, inventory, this, this.dataAccess);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, BasicSmelterEntity smelter) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}
		if (serverLevel.getGameTime() % COOK_INTERVAL != 0L) {
			return;
		}
		AbstractFurnaceBlockEntity.serverTick(serverLevel, pos, state, smelter);
	}
}
