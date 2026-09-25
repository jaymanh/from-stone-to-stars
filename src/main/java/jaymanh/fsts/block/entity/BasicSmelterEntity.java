package jaymanh.fsts.block.entity;

import jaymanh.fsts.component.CrucibleCharge;
import jaymanh.fsts.item.CrucibleItem;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Primitive blast furnace: blasting recipes at half speed, plus charged-crucible alloying.
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

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == SLOT_INPUT) {
			return CrucibleItem.isCharged(stack) || super.canPlaceItem(slot, stack);
		}
		return super.canPlaceItem(slot, stack);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, BasicSmelterEntity smelter) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		ItemStack input = smelter.getItem(SLOT_INPUT);
		if (CrucibleItem.isCharged(input)) {
			tickAlloying(serverLevel, pos, state, smelter);
			return;
		}

		if (serverLevel.getGameTime() % COOK_INTERVAL != 0L) {
			return;
		}
		AbstractFurnaceBlockEntity.serverTick(serverLevel, pos, state, smelter);
	}

	private static void tickAlloying(ServerLevel level, BlockPos pos, BlockState state, BasicSmelterEntity smelter) {
		ItemStack input = smelter.getItem(SLOT_INPUT);
		CrucibleCharge charge = CrucibleItem.getCharge(input);
		if (charge == null) {
			return;
		}

		ItemStack fuel = smelter.getItem(SLOT_FUEL);
		ItemStack resultSlot = smelter.getItem(SLOT_RESULT);
		ItemStack cooked = charge.createResult();
		boolean canOutput = canOutput(resultSlot, cooked);

		int litTime = smelter.dataAccess.get(DATA_LIT_TIME);
		int litDuration = smelter.dataAccess.get(DATA_LIT_DURATION);
		int cookingProgress = smelter.dataAccess.get(DATA_COOKING_PROGRESS);
		int cookingTotalTime = charge.cookingTime();
		smelter.dataAccess.set(DATA_COOKING_TOTAL_TIME, cookingTotalTime);

		boolean wasLit = litTime > 0;
		boolean changed = false;

		if (litTime > 0) {
			litTime--;
		}

		FuelValues fuelValues = level.fuelValues();
		if (canOutput && litTime == 0 && isFuel(fuelValues, fuel)) {
			litDuration = smelter.getBurnDuration(fuelValues, fuel);
			litTime = litDuration;
			if (litTime > 0) {
				changed = true;
				var remainder = fuel.getItem().getCraftingRemainder();
				fuel.shrink(1);
				if (fuel.isEmpty()) {
					ItemStack leftoverFuel = remainder.create();
					smelter.setItem(SLOT_FUEL, leftoverFuel.isEmpty() ? ItemStack.EMPTY : leftoverFuel);
				}
			}
		}

		if (litTime > 0 && canOutput) {
			cookingProgress++;
			if (cookingProgress >= cookingTotalTime) {
				cookingProgress = 0;
				ItemStack leftoverCrucible = CrucibleItem.finishFiring(input);
				smelter.setItem(SLOT_INPUT, leftoverCrucible);

				if (resultSlot.isEmpty()) {
					smelter.setItem(SLOT_RESULT, cooked);
				} else {
					resultSlot.grow(cooked.getCount());
					smelter.setItem(SLOT_RESULT, resultSlot);
				}
				changed = true;
			}
		} else {
			cookingProgress = Mth.clamp(cookingProgress - 2, 0, cookingTotalTime);
		}

		smelter.dataAccess.set(DATA_LIT_TIME, litTime);
		smelter.dataAccess.set(DATA_LIT_DURATION, litDuration);
		smelter.dataAccess.set(DATA_COOKING_PROGRESS, cookingProgress);

		boolean isLit = litTime > 0;
		if (wasLit != isLit) {
			changed = true;
			state = state.setValue(AbstractFurnaceBlock.LIT, isLit);
			level.setBlock(pos, state, 3);
		}

		if (changed) {
			setChanged(level, pos, state);
		}
	}

	private static boolean canOutput(ItemStack resultSlot, ItemStack cooked) {
		if (cooked.isEmpty()) {
			return false;
		}
		if (resultSlot.isEmpty()) {
			return true;
		}
		if (!ItemStack.isSameItemSameComponents(resultSlot, cooked)) {
			return false;
		}
		int combined = resultSlot.getCount() + cooked.getCount();
		return combined <= resultSlot.getMaxStackSize() && combined <= smelterMaxStack();
	}

	private static int smelterMaxStack() {
		return 64;
	}

	private static boolean isFuel(FuelValues fuelValues, ItemStack fuel) {
		return !fuel.isEmpty() && fuelValues.isFuel(fuel);
	}
}
