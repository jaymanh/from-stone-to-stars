package jaymanh.fsts.block.entity;

import jaymanh.fsts.block.ModCustomBlockModels.OreCrusherBlock;
import jaymanh.fsts.recipe.ForgingRecipe;
import jaymanh.fsts.recipe.ModRecipes;
import jaymanh.fsts.rotation.RotationConstants;
import jaymanh.fsts.tags.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

/**
 * Runs the forging stump's crush recipes at trip-hammer pace and drops the crushed ore into the forge input below.
 * Crushed ore that does not fit waits in a holding slot, and crushing pauses until it is gone.
 * Takes ore from hoppers on the top and sides, from the player, and from items dropped on top.
 */
public class OreCrusherBlockEntity extends AbstractRotationBlockEntity implements WorldlyContainer {
	public static final int SLOT_ORE = 0;
	public static final int SLOT_HELD = 1;
	private static final int[] ORE_SLOT = {SLOT_ORE};
	private static final int[] NO_SLOTS = {};
	/** How long the head stays down after each strike, in ticks. */
	private static final int FALL_TICKS = 4;
	private static final int PICKUP_INTERVAL = 8;

	private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
	private int poweredTicks;
	private int hits;

	public OreCrusherBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ORE_CRUSHER, pos, state);
	}

	public boolean isDriving() {
		return this.isSpinning() && this.isNetworkActive();
	}

	public int hits() {
		return this.hits;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, OreCrusherBlockEntity crusher) {
		ForgeInputBlockEntity below = level.getBlockEntity(pos.below()) instanceof ForgeInputBlockEntity input ? input : null;
		ItemStack held = crusher.items.get(SLOT_HELD);
		if (below != null && !held.isEmpty()) {
			ItemStack left = below.store(held);
			if (left != held) {
				crusher.items.set(SLOT_HELD, left);
				crusher.setChanged();
			}
		}
		if (level.getGameTime() % PICKUP_INTERVAL == 0L) {
			crusher.pickUpDroppedOre(level, pos);
		}

		if (!crusher.isDriving()) {
			crusher.poweredTicks = 0;
			crusher.setStruck(level, pos, false);
			return;
		}

		int cycle = RotationConstants.ORE_CRUSHER_TICKS_PER_HIT;
		crusher.poweredTicks++;
		if (crusher.poweredTicks > cycle) {
			crusher.poweredTicks = 1;
		}
		int fallAt = cycle - FALL_TICKS + 1;
		crusher.setStruck(level, pos, crusher.poweredTicks >= fallAt);
		if (crusher.poweredTicks == fallAt) {
			crusher.strike(level, pos, below);
		}
	}

	private void strike(Level level, BlockPos pos, @Nullable ForgeInputBlockEntity below) {
		ItemStack ore = this.items.get(SLOT_ORE);
		if (below == null || ore.isEmpty() || !this.items.get(SLOT_HELD).isEmpty()) {
			return;
		}
		ForgingRecipe recipe = findCrush(level, ore);
		if (recipe == null || ore.getCount() < recipe.ingredientCount()) {
			return;
		}
		level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.6F, 0.7F + level.getRandom().nextFloat() * 0.2F);
		this.hits += RotationConstants.ORE_CRUSHER_HITS_PER_STRIKE;
		if (this.hits >= recipe.hits()) {
			this.hits = 0;
			ore.shrink(recipe.ingredientCount());
			this.items.set(SLOT_HELD, below.store(recipe.resultStack()));
			level.playSound(null, pos, SoundEvents.GRAVEL_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F);
		}
		this.setChanged();
	}

	/** First forging recipe for this ore whose result is a crushed ore. Server only. */
	public static @Nullable ForgingRecipe findCrush(@Nullable Level level, ItemStack stack) {
		if (!(level instanceof ServerLevel)) {
			return null;
		}
		for (var holder : ModRecipes.listForgingForMetal(level, stack)) {
			if (holder.value().resultStack().is(ModTags.CRUSHED_ORES)) {
				return holder.value();
			}
		}
		return null;
	}

	private void pickUpDroppedOre(Level level, BlockPos pos) {
		AABB above = new AABB(pos.getX(), pos.getY() + 0.5, pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.5, pos.getZ() + 1.0);
		for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, above, EntitySelector.ENTITY_STILL_ALIVE)) {
			if (findCrush(level, item.getItem()) != null && HopperBlockEntity.addItem(this, item)) {
				this.setChanged();
			}
		}
	}

	/** Moves as much of a crushable stack as fits into the ore slot. */
	public boolean insertOre(ItemStack stack, Player player) {
		if (findCrush(this.level, stack) == null) {
			return false;
		}
		ItemStack ore = this.items.get(SLOT_ORE);
		int moved;
		if (ore.isEmpty()) {
			moved = Math.min(stack.getCount(), this.getMaxStackSize(stack));
			this.setItem(SLOT_ORE, stack.copyWithCount(moved));
		} else if (ItemStack.isSameItemSameComponents(ore, stack)) {
			moved = Math.min(stack.getCount(), this.getMaxStackSize(ore) - ore.getCount());
			ore.grow(moved);
		} else {
			return false;
		}
		if (moved <= 0) {
			return false;
		}
		stack.consume(moved, player);
		this.setChanged();
		return true;
	}

	/** Hands back the ore and any crushed ore still waiting for room below. */
	public boolean takeAll(Player player) {
		boolean took = false;
		for (int slot = 0; slot < this.items.size(); slot++) {
			ItemStack stack = this.items.get(slot);
			if (!stack.isEmpty()) {
				this.items.set(slot, ItemStack.EMPTY);
				player.getInventory().placeItemBackInInventory(stack);
				took = true;
			}
		}
		if (took) {
			this.hits = 0;
			this.setChanged();
		}
		return took;
	}

	private void setStruck(Level level, BlockPos pos, boolean struck) {
		BlockState current = level.getBlockState(pos);
		if (!current.is(this.getBlockState().getBlock()) || current.getValue(OreCrusherBlock.STRUCK) == struck) {
			return;
		}
		level.setBlock(pos, current.setValue(OreCrusherBlock.STRUCK, struck), Block.UPDATE_CLIENTS);
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level != null) {
			Containers.dropContents(this.level, pos, this.items);
		}
		super.preRemoveSideEffects(pos, state);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		ContainerHelper.saveAllItems(output, this.items);
		output.putInt("Hits", this.hits);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.items.clear();
		ContainerHelper.loadAllItems(input, this.items);
		this.hits = input.getIntOr("Hits", 0);
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.items) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.items.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack removed = ContainerHelper.removeItem(this.items, slot, amount);
		if (!removed.isEmpty()) {
			this.setChanged();
		}
		return removed;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(this.items, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot == SLOT_ORE && !ItemStack.isSameItem(this.items.get(SLOT_ORE), stack)) {
			this.hits = 0;
		}
		this.items.set(slot, stack);
		stack.limitSize(this.getMaxStackSize(stack));
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return slot == SLOT_ORE && findCrush(this.level, stack) != null;
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return side == Direction.DOWN ? NO_SLOTS : ORE_SLOT;
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
