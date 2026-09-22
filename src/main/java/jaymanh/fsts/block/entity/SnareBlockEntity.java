package jaymanh.fsts.block.entity;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import jaymanh.fsts.block.ModCustomBlockModels.SnareBlock;
import jaymanh.fsts.tags.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Bait in slot 0 (player insert only). Catch loot in later slots (player take only).
 * Faces are closed so hoppers cannot insert or extract.
 */
public class SnareBlockEntity extends BlockEntity implements WorldlyContainer {
	public static final int BAIT_SLOT = 0;
	public static final int LOOT_SLOTS = 8;
	public static final int SLOT_COUNT = 1 + LOOT_SLOTS;
	public static final int BAIT_CAPACITY = 16;
	public static final int CATCH_INTERVAL_TICKS = 20 * 60 * 2;
	public static final int SHY_RANGE = 24;

	private static final int[] NO_SLOTS = new int[0];

	private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private int catchTicks;

	public SnareBlockEntity(BlockPos pos, BlockState state) {
		this(ModBlockEntities.SNARE, pos, state);
	}

	protected SnareBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean isBait(ItemStack stack) {
		return isMeatBait(stack) || isPlantBait(stack);
	}

	public static boolean isMeatBait(ItemStack stack) {
		return !stack.isEmpty() && stack.is(ModTags.SNARE_MEAT_BAIT);
	}

	public static boolean isPlantBait(ItemStack stack) {
		return !stack.isEmpty() && stack.is(ModTags.SNARE_PLANT_BAIT);
	}

	public boolean insertBait(ItemStack stack, Player player) {
		if (!isBait(stack)) {
			return false;
		}

		ItemStack bait = this.items.get(BAIT_SLOT);
		if (bait.isEmpty()) {
			int moved = Math.min(BAIT_CAPACITY, stack.getCount());
			this.items.set(BAIT_SLOT, stack.copyWithCount(moved));
			this.catchTicks = 0;
			if (!player.hasInfiniteMaterials()) {
				stack.shrink(moved);
			}
			this.syncState();
			return true;
		}

		if (!ItemStack.isSameItemSameComponents(bait, stack)) {
			return false;
		}

		int space = BAIT_CAPACITY - bait.getCount();
		if (space <= 0) {
			return false;
		}

		int moved = Math.min(space, stack.getCount());
		bait.grow(moved);
		if (!player.hasInfiniteMaterials()) {
			stack.shrink(moved);
		}
		this.syncState();
		return true;
	}

	public boolean takeLoot(Player player) {
		for (int slot = 1; slot < SLOT_COUNT; slot++) {
			ItemStack loot = this.items.get(slot);
			if (loot.isEmpty()) {
				continue;
			}
			this.items.set(slot, ItemStack.EMPTY);
			player.getInventory().placeItemBackInInventory(loot);
			this.syncState();
			return true;
		}
		return false;
	}

	public boolean extractAll(Player player) {
		boolean removed = this.takeAllLoot(player);

		ItemStack bait = this.items.get(BAIT_SLOT);
		if (!bait.isEmpty()) {
			this.items.set(BAIT_SLOT, ItemStack.EMPTY);
			this.catchTicks = 0;
			player.getInventory().placeItemBackInInventory(bait);
			removed = true;
		}

		if (removed) {
			this.syncState();
		}
		return removed;
	}

	private boolean takeAllLoot(Player player) {
		boolean removed = false;
		for (int slot = 1; slot < SLOT_COUNT; slot++) {
			ItemStack loot = this.items.get(slot);
			if (loot.isEmpty()) {
				continue;
			}
			this.items.set(slot, ItemStack.EMPTY);
			player.getInventory().placeItemBackInInventory(loot);
			removed = true;
		}
		return removed;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, SnareBlockEntity snare) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}
		snare.tickCatch(serverLevel);
	}

	private void tickCatch(ServerLevel level) {
		ItemStack bait = this.items.get(BAIT_SLOT);
		if (bait.isEmpty()) {
			this.catchTicks = 0;
			return;
		}

		if (!this.canCatch(level) || this.isPlayerNearby(level)) {
			return;
		}

		this.catchTicks++;
		int interval = CATCH_INTERVAL_TICKS + level.getRandom().nextInt(CATCH_INTERVAL_TICKS / 4);
		if (this.catchTicks < interval) {
			return;
		}

		this.catchTicks = 0;
		this.tryCatch(level);
	}

	private boolean isPlayerNearby(ServerLevel level) {
		AABB area = new AABB(this.worldPosition).inflate(SHY_RANGE);
		return !level.getEntitiesOfClass(Player.class, area, player -> !player.isSpectator()).isEmpty();
	}

	protected boolean canCatch(ServerLevel level) {
		return true;
	}

	protected ResourceKey<LootTable> getCatchLootTable(ItemStack bait) {
		return isMeatBait(bait) ? ModTags.SNARE_MEAT_LOOT : ModTags.SNARE_PLANT_LOOT;
	}

	protected void playCatchSound(ServerLevel level) {
		level.playSound(null, this.worldPosition, SoundEvents.RABBIT_HURT, SoundSource.BLOCKS, 0.35F, 1.2F);
	}

	private void tryCatch(ServerLevel level) {
		ItemStack bait = this.items.get(BAIT_SLOT);
		ResourceKey<LootTable> tableKey = this.getCatchLootTable(bait);

		LootParams params = new LootParams.Builder(level)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition))
				.create(LootContextParamSets.CHEST);
		LootTable table = level.getServer().reloadableRegistries().getLootTable(tableKey);
		List<ItemStack> drops = table.getRandomItems(params);
		if (drops.isEmpty() || !this.canFitAll(drops)) {
			return;
		}

		bait.shrink(1);
		if (bait.isEmpty()) {
			this.items.set(BAIT_SLOT, ItemStack.EMPTY);
		}

		for (ItemStack drop : drops) {
			this.insertInto(this.items, drop.copy());
		}

		this.playCatchSound(level);
		this.syncState();
	}

	private boolean canFitAll(List<ItemStack> drops) {
		NonNullList<ItemStack> copy = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
		for (int slot = 1; slot < SLOT_COUNT; slot++) {
			ItemStack stack = this.items.get(slot);
			if (!stack.isEmpty()) {
				copy.set(slot, stack.copy());
			}
		}
		for (ItemStack drop : drops) {
			if (drop.isEmpty()) {
				continue;
			}
			if (!insertInto(copy, drop.copy())) {
				return false;
			}
		}
		return true;
	}

	private boolean insertInto(NonNullList<ItemStack> target, ItemStack drop) {
		int remaining = drop.getCount();
		for (int slot = 1; slot < SLOT_COUNT && remaining > 0; slot++) {
			ItemStack stack = target.get(slot);
			if (stack.isEmpty()) {
				int moved = Math.min(remaining, drop.getMaxStackSize());
				target.set(slot, drop.copyWithCount(moved));
				remaining -= moved;
				continue;
			}
			if (!ItemStack.isSameItemSameComponents(stack, drop)) {
				continue;
			}
			int space = stack.getMaxStackSize() - stack.getCount();
			if (space <= 0) {
				continue;
			}
			int moved = Math.min(remaining, space);
			stack.grow(moved);
			remaining -= moved;
		}
		return remaining <= 0;
	}

	private void syncState() {
		if (this.level == null || this.level.isClientSide()) {
			return;
		}

		boolean baited = !this.items.get(BAIT_SLOT).isEmpty();
		BlockState state = this.getBlockState();
		if (state.hasProperty(SnareBlock.BAITED) && state.getValue(SnareBlock.BAITED) != baited) {
			this.level.setBlock(this.worldPosition, state.setValue(SnareBlock.BAITED, baited), Block.UPDATE_ALL);
		}
		this.setChanged();
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
		ValueOutput.TypedOutputList<ItemStack> list = output.list("Items", ItemStack.OPTIONAL_CODEC);
		for (ItemStack stack : this.items) {
			list.add(stack);
		}
		output.putInt("CatchTicks", this.catchTicks);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.items.clear();
		int slot = 0;
		for (ItemStack stack : input.listOrEmpty("Items", ItemStack.OPTIONAL_CODEC)) {
			if (slot < SLOT_COUNT) {
				this.items.set(slot++, stack);
			}
		}
		this.catchTicks = Math.max(input.getIntOr("CatchTicks", 0), 0);
	}

	@Override
	public int getContainerSize() {
		return SLOT_COUNT;
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
		if (slot == BAIT_SLOT) {
			return ItemStack.EMPTY;
		}
		ItemStack removed = ContainerHelper.removeItem(this.items, slot, amount);
		if (!removed.isEmpty()) {
			this.syncState();
		}
		return removed;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (slot == BAIT_SLOT) {
			return ItemStack.EMPTY;
		}
		return ContainerHelper.takeItem(this.items, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot == BAIT_SLOT) {
			if (!isBait(stack)) {
				return;
			}
			if (stack.getCount() > BAIT_CAPACITY) {
				stack.setCount(BAIT_CAPACITY);
			}
		}
		this.items.set(slot, stack);
		this.syncState();
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return slot == BAIT_SLOT && isBait(stack);
	}

	@Override
	public boolean canTakeItem(Container hopper, int slot, ItemStack stack) {
		return slot != BAIT_SLOT;
	}

	@Override
	public void clearContent() {
		this.items.clear();
		this.catchTicks = 0;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return NO_SLOTS;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return false;
	}
}
