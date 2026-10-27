package jaymanh.fsts.block.entity;

import java.util.List;
import java.util.Optional;

import jaymanh.fsts.multiblock.ActiveMultiblocks;
import jaymanh.fsts.multiblock.AssemblyMatch;
import jaymanh.fsts.multiblock.AssemblyMatcher;
import jaymanh.fsts.multiblock.AssemblyRegistry;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.ModMultiblocks;
import jaymanh.fsts.multiblock.SawmillLayout;
import jaymanh.fsts.rotation.RotationConstants;
import jaymanh.fsts.rotation.RotationNetwork;
import jaymanh.fsts.rotation.RotationStatus;
import jaymanh.fsts.util.LogPlanks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

/**
 * Brain of a sawmill. Tries to assemble whenever it is turning and unformed, and
 * holds the log and plank buffers that every intake and outfeed share.
 */
public class SawArborBlockEntity extends AbstractRotationBlockEntity {
	public static final int PLANKS_PER_LOG = 6;
	public static final int LOGS_PER_INFEED_ROLLER = 16;
	public static final int MAX_SLOTS = SawmillLayout.MAX_WIDTH * SawmillLayout.MAX_BED;
	private static final int ASSEMBLY_RETRY_TICKS = 40;

	private final NonNullList<ItemStack> input = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);
	private final NonNullList<ItemStack> output = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);

	private FormedMultiblock formed;
	private boolean participantsLinked;
	private SawmillLayout layout;
	private Component lastFailure;
	private boolean wasSpinning;
	private int retryTimer;

	public SawArborBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.SAW_ARBOR, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, SawArborBlockEntity arbor) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		if (arbor.formed != null) {
			arbor.tickFormed(serverLevel);
		}

		boolean spinning = arbor.isSpinning();
		if (arbor.formed == null && spinning) {
			arbor.retryTimer--;
			if (!arbor.wasSpinning || arbor.retryTimer <= 0) {
				arbor.retryTimer = ASSEMBLY_RETRY_TICKS;
				arbor.tryAssemble(serverLevel);
			}
		}
		arbor.wasSpinning = spinning;
	}

	private void tickFormed(ServerLevel level) {
		FormedMultiblock current = this.formed;
		if (!this.participantsLinked) {
			ActiveMultiblocks.register(level, current);
			this.participantsLinked = true;
		}

		if (this.layout == null || level.getGameTime() % ASSEMBLY_RETRY_TICKS == 0) {
			SawmillLayout fresh = SawmillLayout.tryMatch(level, this.worldPosition).orElse(null);
			if (fresh == null || (this.layout != null && !this.layout.sameShape(fresh))) {
				current.cancel(level);
			} else {
				this.layout = fresh;
			}
		}

		if (current.isActive()) {
			current.tick(level);
		}

		if (!current.isActive()) {
			ActiveMultiblocks.unregister(level, current);
			this.formed = null;
			this.layout = null;
			this.participantsLinked = false;
			this.retryTimer = 0;
			this.setChanged();
			RotationNetwork.update(level, this.worldPosition);
		}
	}

	private void tryAssemble(ServerLevel level) {
		Optional<AssemblyMatch.Attempt> attempt = AssemblyRegistry.get(ModMultiblocks.SAWMILL_ID)
				.map(definition -> AssemblyMatcher.attempt(level, this.worldPosition, definition));
		if (attempt.isEmpty()) {
			return;
		}
		AssemblyMatch.Attempt result = attempt.get();
		if (!result.ok()) {
			this.lastFailure = result.failure().orElse(null);
			return;
		}
		SawmillLayout matched = SawmillLayout.tryMatch(level, this.worldPosition).orElse(null);
		if (matched == null) {
			return;
		}

		AssemblyMatch match = result.match().orElseThrow();
		FormedMultiblock created = new FormedMultiblock(match, match.processFactory().create(match));
		this.formed = created;
		this.layout = matched;
		this.lastFailure = null;
		this.participantsLinked = true;
		ActiveMultiblocks.register(level, created);
		created.onFormed(level);
		this.dropOverflow(level);
		this.setChanged();
		level.playSound(null, this.worldPosition, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
		RotationNetwork.update(level, this.worldPosition);
	}

	public boolean isFormed() {
		return this.formed != null && this.formed.isActive() && this.layout != null;
	}

	public SawmillLayout layout() {
		return this.isFormed() ? this.layout : null;
	}

	/** Network load: one per lane once formed. */
	public int powerCost() {
		return this.isFormed() ? this.layout.width() * RotationConstants.SAW_ARBOR_COST_PER_LANE : RotationConstants.MACHINE_COST;
	}

	public int inputSlots() {
		return this.isFormed() ? Math.min(MAX_SLOTS, this.layout.infeedRollers().size()) : 0;
	}

	public int outputSlots() {
		return this.isFormed() ? Math.min(MAX_SLOTS, this.layout.outfeedRollers().size()) : 0;
	}

	public ItemStack getInput(int slot) {
		return this.input.get(slot);
	}

	public ItemStack getOutput(int slot) {
		return this.output.get(slot);
	}

	public void setInput(int slot, ItemStack stack) {
		this.input.set(slot, stack);
		this.setChanged();
	}

	public void setOutput(int slot, ItemStack stack) {
		this.output.set(slot, stack);
		this.setChanged();
	}

	public static boolean isSawable(ItemStack stack) {
		return planksFor(stack) != Items.AIR;
	}

	private static Item planksFor(ItemStack stack) {
		if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
			return Items.AIR;
		}
		return LogPlanks.planksItemForLog(blockItem.getBlock());
	}

	public boolean canInsertInput(int slot, ItemStack stack) {
		if (slot < 0 || slot >= this.inputSlots() || !isSawable(stack)) {
			return false;
		}
		ItemStack current = this.input.get(slot);
		if (current.isEmpty()) {
			return stack.getCount() <= LOGS_PER_INFEED_ROLLER;
		}
		return ItemStack.isSameItemSameComponents(current, stack)
				&& current.getCount() + stack.getCount() <= LOGS_PER_INFEED_ROLLER;
	}

	/**
	 * Moves as much of {@code stack} as fits into the shared input.
	 *
	 * @return number of logs moved
	 */
	public int insertInput(ItemStack stack, boolean creative) {
		if (!isSawable(stack)) {
			return 0;
		}
		int moved = 0;
		int slots = this.inputSlots();
		for (int pass = 0; pass < 2 && moved < stack.getCount(); pass++) {
			for (int slot = 0; slot < slots && moved < stack.getCount(); slot++) {
				ItemStack current = this.input.get(slot);
				boolean merge = pass == 0;
				if (merge ? current.isEmpty() || !ItemStack.isSameItemSameComponents(current, stack) : !current.isEmpty()) {
					continue;
				}
				int space = LOGS_PER_INFEED_ROLLER - current.getCount();
				int amount = Math.min(space, stack.getCount() - moved);
				if (amount <= 0) {
					continue;
				}
				if (current.isEmpty()) {
					this.input.set(slot, stack.copyWithCount(amount));
				} else {
					current.grow(amount);
				}
				moved += amount;
			}
		}
		if (moved > 0) {
			if (!creative) {
				stack.shrink(moved);
			}
			this.setChanged();
		}
		return moved;
	}

	public boolean takeAllOutput(Player player) {
		boolean took = false;
		for (int slot = 0; slot < MAX_SLOTS; slot++) {
			ItemStack stack = this.output.get(slot);
			if (!stack.isEmpty()) {
				this.output.set(slot, ItemStack.EMPTY);
				player.getInventory().placeItemBackInInventory(stack);
				took = true;
			}
		}
		if (took) {
			this.setChanged();
		}
		return took;
	}

	/** True when at least one log can be cut and its planks fit. */
	public boolean canCut() {
		int slots = this.inputSlots();
		for (int slot = 0; slot < slots; slot++) {
			ItemStack log = this.input.get(slot);
			if (!log.isEmpty() && this.outputRoomFor(planksFor(log)) >= PLANKS_PER_LOG) {
				return true;
			}
		}
		return false;
	}

	/** Cuts up to one log per lane. */
	public void cut(ServerLevel level, SawmillLayout layout) {
		int cut = 0;
		BlockState lastLog = null;
		for (int lane = 0; lane < layout.width(); lane++) {
			int slot = this.nextCuttableSlot();
			if (slot < 0) {
				break;
			}
			ItemStack log = this.input.get(slot);
			Item planks = planksFor(log);
			if (log.getItem() instanceof BlockItem blockItem) {
				lastLog = blockItem.getBlock().defaultBlockState();
			}
			log.shrink(1);
			if (log.isEmpty()) {
				this.input.set(slot, ItemStack.EMPTY);
			}
			this.addOutput(new ItemStack(planks, PLANKS_PER_LOG));
			cut++;
		}
		if (cut == 0) {
			return;
		}
		this.setChanged();

		List<BlockPos> lanes = layout.lanes();
		for (int lane = 0; lane < Math.min(cut, lanes.size()); lane++) {
			BlockPos at = lanes.get(lane);
			if (lastLog != null) {
				level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, lastLog),
						at.getX() + 0.5, at.getY() + 0.4, at.getZ() + 0.5, 8, 0.3, 0.2, 0.3, 0.05);
			}
		}
		level.playSound(null, lanes.getFirst(), SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 0.8F, 0.7F + level.getRandom().nextFloat() * 0.2F);
		RotationNetwork.grantUnderLoadAdvancement(level, this.worldPosition);
	}

	private int nextCuttableSlot() {
		int slots = this.inputSlots();
		for (int slot = 0; slot < slots; slot++) {
			ItemStack log = this.input.get(slot);
			if (!log.isEmpty() && this.outputRoomFor(planksFor(log)) >= PLANKS_PER_LOG) {
				return slot;
			}
		}
		return -1;
	}

	private int outputRoomFor(Item planks) {
		if (planks == Items.AIR) {
			return 0;
		}
		ItemStack probe = new ItemStack(planks);
		int room = 0;
		int slots = this.outputSlots();
		for (int slot = 0; slot < slots; slot++) {
			ItemStack current = this.output.get(slot);
			if (current.isEmpty()) {
				room += probe.getMaxStackSize();
			} else if (ItemStack.isSameItemSameComponents(current, probe)) {
				room += current.getMaxStackSize() - current.getCount();
			}
		}
		return room;
	}

	private void addOutput(ItemStack planks) {
		int slots = this.outputSlots();
		for (int slot = 0; slot < slots && !planks.isEmpty(); slot++) {
			ItemStack current = this.output.get(slot);
			if (!current.isEmpty() && ItemStack.isSameItemSameComponents(current, planks)) {
				int amount = Math.min(planks.getCount(), current.getMaxStackSize() - current.getCount());
				current.grow(amount);
				planks.shrink(amount);
			}
		}
		for (int slot = 0; slot < slots && !planks.isEmpty(); slot++) {
			if (this.output.get(slot).isEmpty()) {
				this.output.set(slot, planks.split(planks.getMaxStackSize()));
			}
		}
	}

	public void pullInDroppedLogs(SawmillLayout layout) {
		if (this.level == null || layout.infeedRollers().isEmpty()) {
			return;
		}
		AABB area = null;
		for (BlockPos roller : layout.infeedRollers()) {
			AABB cell = new AABB(roller).expandTowards(0.0, 0.5, 0.0);
			area = area == null ? cell : area.minmax(cell);
		}
		for (ItemEntity item : this.level.getEntitiesOfClass(ItemEntity.class, area, entity -> isSawable(entity.getItem()))) {
			ItemStack stack = item.getItem();
			if (this.insertInput(stack, false) > 0) {
				if (stack.isEmpty()) {
					item.discard();
				} else {
					item.setItem(stack);
				}
			}
		}
	}

	/** Slots beyond the current capacity spill at the arbor. */
	private void dropOverflow(ServerLevel level) {
		dropFrom(level, this.input, this.inputSlots());
		dropFrom(level, this.output, this.outputSlots());
	}

	private void dropFrom(ServerLevel level, NonNullList<ItemStack> buffer, int keep) {
		for (int slot = keep; slot < buffer.size(); slot++) {
			ItemStack stack = buffer.get(slot);
			if (!stack.isEmpty()) {
				buffer.set(slot, ItemStack.EMPTY);
				Containers.dropItemStack(level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0, this.worldPosition.getZ() + 0.5, stack);
			}
		}
	}

	private static int count(NonNullList<ItemStack> buffer, int slots) {
		int total = 0;
		for (int slot = 0; slot < slots; slot++) {
			total += buffer.get(slot).getCount();
		}
		return total;
	}

	public Component statusMessage() {
		if (this.isFormed()) {
			int logs = count(this.input, this.inputSlots());
			int planks = count(this.output, this.outputSlots());
			int logCap = this.inputSlots() * LOGS_PER_INFEED_ROLLER;
			int plankCap = this.outputSlots() * Items.OAK_PLANKS.getDefaultMaxStackSize();
			String key = this.isSpinning() ? "running" : "stalled";
			return Component.translatable(
					"multiblock.from-stone-to-stars.sawmill.status." + key,
					this.layout.width(), this.layout.bladeHeight(), logs, logCap, planks, plankCap
			);
		}
		if (!this.isSpinning()) {
			if (this.getRotationStatus() == RotationStatus.OVERLOADED) {
				return Component.translatable("multiblock.from-stone-to-stars.sawmill.status.overloaded");
			}
			return Component.translatable("multiblock.from-stone-to-stars.sawmill.status.not_turning");
		}
		if (this.lastFailure != null) {
			return this.lastFailure;
		}
		return Component.translatable("multiblock.from-stone-to-stars.sawmill.status.checking");
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		ValueOutput.TypedOutputList<ItemStack> inputList = output.list("Input", ItemStack.OPTIONAL_CODEC);
		for (ItemStack stack : this.input) {
			inputList.add(stack);
		}
		ValueOutput.TypedOutputList<ItemStack> outputList = output.list("Output", ItemStack.OPTIONAL_CODEC);
		for (ItemStack stack : this.output) {
			outputList.add(stack);
		}
		if (this.formed != null && this.formed.isActive()) {
			this.formed.saveAdditional(output.child("Formed"));
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		loadList(input, "Input", this.input);
		loadList(input, "Output", this.output);
		input.child("Formed").flatMap(FormedMultiblock::load).ifPresent(loaded -> {
			this.formed = loaded;
			this.layout = null;
			this.participantsLinked = false;
		});
	}

	private static void loadList(ValueInput input, String key, NonNullList<ItemStack> buffer) {
		buffer.clear();
		int slot = 0;
		for (ItemStack stack : input.listOrEmpty(key, ItemStack.OPTIONAL_CODEC)) {
			if (slot < buffer.size()) {
				buffer.set(slot++, stack);
			}
		}
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level instanceof ServerLevel serverLevel) {
			FormedMultiblock current = this.formed;
			if (current == null) {
				current = ActiveMultiblocks.get(serverLevel, pos).orElse(null);
			}
			if (current != null) {
				if (current.isActive()) {
					current.cancel(serverLevel);
				}
				ActiveMultiblocks.unregister(serverLevel, current);
			}
			this.formed = null;
			this.layout = null;
			Containers.dropContents(serverLevel, pos, this.input);
			Containers.dropContents(serverLevel, pos, this.output);
		}
		super.preRemoveSideEffects(pos, state);
	}
}
