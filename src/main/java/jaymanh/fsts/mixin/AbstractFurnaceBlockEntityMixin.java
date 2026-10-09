package jaymanh.fsts.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import jaymanh.fsts.block.entity.BasicSmelterEntity;
import jaymanh.fsts.block.entity.ForgeHearthBlockEntity;
import jaymanh.fsts.component.CrucibleCharge;
import jaymanh.fsts.item.CrucibleItem;
import jaymanh.fsts.item.HeatedMetalItem;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
	private static final int SLOT_INPUT = 0;
	private static final int SLOT_FUEL = 1;
	private static final int SLOT_RESULT = 2;

	private static final String ALLOY_EXPERIENCE_KEY = "FstsAlloyExperience";

	/**
	 * Alloy recipes are crafting recipes, and furnace payout casts {@code recipesUsed}
	 * to a cooking recipe. Experience is kept here and popped with the furnace's other XP.
	 */
	@Unique
	private float fsts$storedAlloyExperience;

	@Shadow
	@Final
	protected ContainerData dataAccess;

	@Shadow
	protected abstract int getBurnDuration(FuelValues fuelValues, ItemStack stack);

	/**
	 * Charged crucibles are not blasting recipes. The basic smelter, blast furnace, and forge
	 * fire them here, then skip the normal smelt for that tick.
	 */
	@Inject(method = "serverTick", at = @At("HEAD"), cancellable = true)
	private static void fsts$fireChargedCrucible(
			ServerLevel level,
			BlockPos pos,
			BlockState state,
			AbstractFurnaceBlockEntity furnace,
			CallbackInfo ci
	) {
		if (!fsts$firesAlloys(furnace) || !CrucibleItem.isCharged(furnace.getItem(SLOT_INPUT))) {
			return;
		}
		fsts$tickAlloying(level, pos, state, furnace, (AbstractFurnaceBlockEntityMixin) (Object) furnace);
		HeatedMetalItem.keepHot(furnace, level.getGameTime());
		ci.cancel();
	}

	@Inject(method = "serverTick", at = @At("TAIL"))
	private static void fsts$keepHeatedMetalHot(
			ServerLevel level,
			BlockPos pos,
			BlockState state,
			AbstractFurnaceBlockEntity furnace,
			CallbackInfo ci
	) {
		HeatedMetalItem.keepHot(furnace, level.getGameTime());
	}

	private static boolean fsts$firesAlloys(AbstractFurnaceBlockEntity furnace) {
		return furnace instanceof BasicSmelterEntity
				|| furnace instanceof BlastFurnaceBlockEntity
				|| furnace instanceof ForgeHearthBlockEntity;
	}

	private static void fsts$tickAlloying(
			ServerLevel level,
			BlockPos pos,
			BlockState state,
			AbstractFurnaceBlockEntity furnace,
			AbstractFurnaceBlockEntityMixin access
	) {
		ItemStack input = furnace.getItem(SLOT_INPUT);
		CrucibleCharge charge = CrucibleItem.getCharge(input);
		if (charge == null) {
			return;
		}

		ItemStack fuel = furnace.getItem(SLOT_FUEL);
		ItemStack resultSlot = furnace.getItem(SLOT_RESULT);
		ItemStack cooked = charge.createResult();
		boolean canOutput = fsts$canOutput(furnace, resultSlot, cooked);

		int litTime = access.dataAccess.get(AbstractFurnaceBlockEntity.DATA_LIT_TIME);
		int litDuration = access.dataAccess.get(AbstractFurnaceBlockEntity.DATA_LIT_DURATION);
		int cookingProgress = access.dataAccess.get(AbstractFurnaceBlockEntity.DATA_COOKING_PROGRESS);
		int cookingTotalTime = charge.cookingTime();
		access.dataAccess.set(AbstractFurnaceBlockEntity.DATA_COOKING_TOTAL_TIME, cookingTotalTime);

		boolean wasLit = litTime > 0;
		boolean changed = false;

		if (litTime > 0) {
			litTime--;
		}

		if (canOutput && litTime == 0 && fsts$isFuel(level.fuelValues(), fuel)) {
			litDuration = access.getBurnDuration(level.fuelValues(), fuel);
			litTime = litDuration;
			if (litTime > 0) {
				changed = true;
				var remainder = fuel.getItem().getCraftingRemainder();
				fuel.shrink(1);
				if (fuel.isEmpty()) {
					ItemStack leftoverFuel = remainder == null ? ItemStack.EMPTY : remainder.create();
					furnace.setItem(SLOT_FUEL, leftoverFuel.isEmpty() ? ItemStack.EMPTY : leftoverFuel);
				}
			}
		}

		if (litTime > 0 && canOutput) {
			cookingProgress++;
			if (cookingProgress >= cookingTotalTime) {
				cookingProgress = 0;
				furnace.setItem(SLOT_INPUT, CrucibleItem.finishFiring(input));
				if (resultSlot.isEmpty()) {
					furnace.setItem(SLOT_RESULT, cooked);
				} else {
					resultSlot.grow(cooked.getCount());
					furnace.setItem(SLOT_RESULT, resultSlot);
				}
				float experience = charge.experience();
				if (experience > 0.0F) {
					access.fsts$storedAlloyExperience += experience;
				}
				changed = true;
			}
		} else {
			cookingProgress = Mth.clamp(cookingProgress - 2, 0, cookingTotalTime);
		}

		access.dataAccess.set(AbstractFurnaceBlockEntity.DATA_LIT_TIME, litTime);
		access.dataAccess.set(AbstractFurnaceBlockEntity.DATA_LIT_DURATION, litDuration);
		access.dataAccess.set(AbstractFurnaceBlockEntity.DATA_COOKING_PROGRESS, cookingProgress);

		boolean isLit = litTime > 0;
		if (wasLit != isLit) {
			changed = true;
			state = state.setValue(AbstractFurnaceBlock.LIT, isLit);
			level.setBlock(pos, state, 3);
		}

		if (changed) {
			furnace.setChanged();
		}
	}

	/**
	 * Same moment smelting XP is spawned: taking the result, or breaking the furnace.
	 * The first call consumes the stored amount, so a shift-click's second pass pays nothing.
	 */
	@Inject(method = "getRecipesToAwardAndPopExperience", at = @At("RETURN"))
	private void fsts$popAlloyExperience(ServerLevel level, Vec3 position, CallbackInfoReturnable<List<RecipeHolder<?>>> cir) {
		float stored = this.fsts$storedAlloyExperience;
		if (stored <= 0.0F) {
			return;
		}
		this.fsts$storedAlloyExperience = 0.0F;
		int xpReward = Mth.floor(stored);
		float fraction = Mth.frac(stored);
		if (fraction != 0.0F && level.getRandom().nextFloat() < fraction) {
			xpReward++;
		}
		if (xpReward > 0) {
			ExperienceOrb.award(level, position, xpReward);
		}
	}

	@Inject(method = "saveAdditional", at = @At("TAIL"))
	private void fsts$saveAlloyExperience(ValueOutput output, CallbackInfo ci) {
		if (this.fsts$storedAlloyExperience > 0.0F) {
			output.putFloat(ALLOY_EXPERIENCE_KEY, this.fsts$storedAlloyExperience);
		}
	}

	@Inject(method = "loadAdditional", at = @At("TAIL"))
	private void fsts$loadAlloyExperience(ValueInput input, CallbackInfo ci) {
		this.fsts$storedAlloyExperience = input.getFloatOr(ALLOY_EXPERIENCE_KEY, 0.0F);
	}

	private static boolean fsts$canOutput(AbstractFurnaceBlockEntity furnace, ItemStack resultSlot, ItemStack cooked) {
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
		return combined <= resultSlot.getMaxStackSize() && combined <= furnace.getMaxStackSize();
	}

	private static boolean fsts$isFuel(FuelValues fuelValues, ItemStack fuel) {
		return !fuel.isEmpty() && fuelValues.isFuel(fuel);
	}
}
