package jaymanh.fsts.block.entity;

import jaymanh.fsts.multiblock.ActiveMultiblocks;
import jaymanh.fsts.multiblock.FormedMultiblock;
import jaymanh.fsts.multiblock.StructureMatch;
import jaymanh.fsts.multiblock.process.ForgeProcess;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Blast-furnace smelting while the fuzzy forge assembly is lit.
 * All hearths in a match share lit state; inventory/smelting live on the master (origin) hearth.
 * Base cook is slower than a blast furnace; extra hearths speed it back up. Item fuel is unused.
 */
public class ForgeHearthBlockEntity extends AbstractFurnaceBlockEntity {
	private static final int ASSEMBLY_LIT_BUFFER = 200;
	/** Game ticks between cook steps (higher = slower base speed). Extra hearths add steps per interval. */
	private static final int COOK_INTERVAL = 5;
	private static final int[] SLOTS_UP = {SLOT_INPUT};
	private static final int[] SLOTS_DOWN = {SLOT_RESULT};
	private static final int[] SLOTS_SIDE = {};

	private FormedMultiblock formed;
	private boolean participantsLinked;

	public ForgeHearthBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FORGE_HEARTH, pos, state, RecipeType.BLASTING);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.from-stone-to-stars.forge_hearth");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new BlastFurnaceMenu(containerId, inventory, this, this.dataAccess);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == SLOT_FUEL) {
			return false;
		}
		return super.canPlaceItem(slot, stack);
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_DOWN;
		}
		if (direction == Direction.UP) {
			return SLOTS_UP;
		}
		return SLOTS_SIDE;
	}

	@Override
	protected int getBurnDuration(FuelValues fuelValues, ItemStack stack) {
		return 0;
	}

	public FormedMultiblock getFormed() {
		if (this.formed != null && this.formed.isActive()) {
			return this.formed;
		}
		if (this.level != null) {
			return ActiveMultiblocks.get(this.level, this.worldPosition).orElse(null);
		}
		return null;
	}

	/**
	 * Hearth that owns inventory and the burn process (assembly origin).
	 */
	public ForgeHearthBlockEntity getMaster() {
		FormedMultiblock formed = this.getFormed();
		if (formed == null) {
			return this;
		}
		BlockPos origin = formed.match().origin();
		if (origin.equals(this.worldPosition)) {
			return this;
		}
		if (this.level != null && this.level.getBlockEntity(origin) instanceof ForgeHearthBlockEntity master) {
			return master;
		}
		return this;
	}

	public boolean isBurning() {
		FormedMultiblock formed = this.getFormed();
		return formed != null && formed.isActive();
	}

	public boolean isMaster() {
		FormedMultiblock formed = this.getFormed();
		return formed == null || formed.match().origin().equals(this.worldPosition);
	}

	public int getProcessProgress() {
		FormedMultiblock formed = this.getFormed();
		return formed != null && formed.isActive() ? formed.getProcessProgress() : 0;
	}

	public int getProcessDuration() {
		FormedMultiblock formed = this.getFormed();
		return formed != null ? formed.getProcessDuration() : 0;
	}

	public boolean start(StructureMatch match) {
		if (!(this.level instanceof ServerLevel serverLevel) || this.isBurning()) {
			return false;
		}
		FormedMultiblock formed = new FormedMultiblock(match, match.processFactory().create(match));
		this.formed = formed;
		this.participantsLinked = true;
		ActiveMultiblocks.register(serverLevel, formed);
		formed.onFormed(serverLevel);
		return true;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ForgeHearthBlockEntity hearth) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		tickAssembly(serverLevel, pos, state, hearth);
		if (hearth.isRemoved()) {
			return;
		}

		state = level.getBlockState(pos);
		if (!state.hasProperty(AbstractFurnaceBlock.LIT)) {
			return;
		}

		// Only the master hearth smelts; slaves share its lit visual / inventory via openMenu.
		if (!hearth.isMaster()) {
			return;
		}

		if (hearth.isBurning()) {
			hearth.dataAccess.set(DATA_LIT_TIME, Math.max(hearth.dataAccess.get(DATA_LIT_TIME), ASSEMBLY_LIT_BUFFER));
			hearth.dataAccess.set(DATA_LIT_DURATION, ASSEMBLY_LIT_BUFFER);
			if (serverLevel.getGameTime() % COOK_INTERVAL != 0L) {
				return;
			}
			int speed = 1;
			FormedMultiblock formed = hearth.getFormed();
			if (formed != null && formed.process() instanceof ForgeProcess forge) {
				speed = Math.max(1, forge.hearthCount());
			}
			for (int i = 0; i < speed; i++) {
				AbstractFurnaceBlockEntity.serverTick(serverLevel, pos, state, hearth);
				state = level.getBlockState(pos);
				if (!state.hasProperty(AbstractFurnaceBlock.LIT)) {
					return;
				}
			}

			BlockState after = level.getBlockState(pos);
			if (hearth.isBurning() && after.hasProperty(AbstractFurnaceBlock.LIT) && !after.getValue(AbstractFurnaceBlock.LIT)) {
				level.setBlock(pos, after.setValue(AbstractFurnaceBlock.LIT, true), Block.UPDATE_CLIENTS);
			}
		} else {
			if (hearth.dataAccess.get(DATA_LIT_TIME) > 0) {
				hearth.dataAccess.set(DATA_LIT_TIME, 0);
			}
			if (serverLevel.getGameTime() % COOK_INTERVAL != 0L) {
				return;
			}
			AbstractFurnaceBlockEntity.serverTick(serverLevel, pos, state, hearth);
		}
	}

	private static void tickAssembly(ServerLevel level, BlockPos pos, BlockState state, ForgeHearthBlockEntity hearth) {
		if (hearth.formed == null) {
			return;
		}

		FormedMultiblock formed = hearth.formed;
		if (!pos.equals(formed.match().origin())) {
			return;
		}

		if (!hearth.participantsLinked) {
			ActiveMultiblocks.register(level, formed);
			hearth.participantsLinked = true;
		}

		formed.tick(level);

		if (!formed.isActive()) {
			ActiveMultiblocks.unregister(level, formed);
			hearth.formed = null;
			hearth.participantsLinked = false;
			hearth.dataAccess.set(DATA_LIT_TIME, 0);
			ForgeProcess.setAllHearthsLit(level, formed, false);
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (this.formed != null && this.formed.isActive()) {
			this.formed.saveAdditional(output.child("Formed"));
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		input.child("Formed").flatMap(FormedMultiblock::load).ifPresent(loaded -> {
			this.formed = loaded;
			this.participantsLinked = false;
		});
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level instanceof ServerLevel serverLevel) {
			FormedMultiblock formed = this.formed;
			if (formed == null) {
				formed = ActiveMultiblocks.get(serverLevel, pos).orElse(null);
			}
			if (formed != null) {
				if (formed.isActive()) {
					formed.cancel(serverLevel);
				}
				ActiveMultiblocks.unregister(serverLevel, formed);
				if (this.formed == formed) {
					this.formed = null;
					this.participantsLinked = false;
				}
			}
		}
		super.preRemoveSideEffects(pos, state);
	}
}
