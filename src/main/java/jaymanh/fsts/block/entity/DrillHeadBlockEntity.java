package jaymanh.fsts.block.entity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.mojang.serialization.Codec;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.ModCustomBlockModels.DriveHousingBlock;
import jaymanh.fsts.block.ModCustomBlockModels.DrillStringBlock;
import jaymanh.fsts.item.ModItems;
import jaymanh.fsts.tags.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;

/**
 * Controller of the bore rig. With a rod rack on top, and an ore box and a turning drive housing on
 * opposite sides, it bores straight down, one rod per {@value #BLOCKS_PER_ROD} blocks. Any ore in
 * {@link ModTags#DRILL_ORES} in the ring around the hole is mined as a whole vein before the bore goes on.
 * A recall winds the string back up, seals the hole with stone, and returns most of the rods.
 */
public class DrillHeadBlockEntity extends BlockEntity {
	public static final int BLOCKS_PER_ROD = 8;
	public static final int BORE_TICKS = 60;
	public static final int VEIN_TICKS = 40;
	public static final int RECALL_TICKS = 10;
	public static final float ROD_BREAK_CHANCE = 0.25F;
	public static final int MAX_VEIN = 64;

	public enum Status {
		UNFORMED, NO_POWER, BORING, MINING_VEIN, RECALLING, RECALLED, NO_RODS, BLOCKED, LAVA, BOTTOM, ORE_BOX_FULL
	}

	public static final int MAX_RACKS = 8;

	/** {@code racks} runs upward from the rack sitting on the drill head. */
	private record Parts(List<RodRackBlockEntity> racks, OreBoxBlockEntity box, DriveHousingBlockEntity drive) {
		int rods() {
			int rods = 0;
			for (RodRackBlockEntity rack : this.racks) {
				rods += rack.rods();
			}
			return rods;
		}

		boolean takeRod() {
			for (int i = this.racks.size() - 1; i >= 0; i--) {
				if (this.racks.get(i).takeRod()) {
					return true;
				}
			}
			return false;
		}

		/** @return the rods that did not fit */
		int addRods(int count) {
			for (RodRackBlockEntity rack : this.racks) {
				if (count <= 0) {
					break;
				}
				count = rack.addRods(count);
			}
			return count;
		}
	}

	private final Set<BlockPos> vein = new LinkedHashSet<>();
	private int depth;
	private int rodsDown;
	private int progress;
	private boolean recalling;
	private boolean recalled;
	private int lastReturned;
	private int lastBroken;
	private Status status = Status.UNFORMED;

	public DrillHeadBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.DRILL_HEAD, pos, state);
	}

	public Status status() {
		return this.status;
	}

	public int depth() {
		return this.depth;
	}

	public int rodsDown() {
		return this.rodsDown;
	}

	public int veinLeft() {
		return this.vein.size();
	}

	/** Deepest the rig can reach with the rods it has down plus the rods in the racks. */
	public int maxDepth() {
		Parts parts = this.findParts();
		int rods = this.rodsDown + (parts == null ? 0 : parts.rods());
		return rods * BLOCKS_PER_ROD;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, DrillHeadBlockEntity head) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}
		Parts parts = head.findParts();
		if (parts == null) {
			head.status = Status.UNFORMED;
			return;
		}
		if (!parts.drive().isDriving()) {
			head.status = Status.NO_POWER;
			return;
		}
		if (head.recalled) {
			head.status = Status.RECALLED;
			return;
		}

		head.progress++;
		if (head.recalling) {
			head.status = Status.RECALLING;
			if (head.progress >= RECALL_TICKS) {
				head.progress = 0;
				head.recallStep(serverLevel, parts);
			}
			return;
		}
		if (!head.vein.isEmpty()) {
			head.status = Status.MINING_VEIN;
			if (head.progress >= VEIN_TICKS) {
				head.mineVeinStep(serverLevel, parts);
			}
			return;
		}
		head.status = Status.BORING;
		if (head.progress >= BORE_TICKS) {
			head.boreStep(serverLevel, parts);
		}
	}

	private @Nullable Parts findParts() {
		if (this.level == null) {
			return null;
		}
		List<RodRackBlockEntity> racks = new ArrayList<>();
		for (int up = 1; up <= MAX_RACKS; up++) {
			if (!(this.level.getBlockEntity(this.worldPosition.above(up)) instanceof RodRackBlockEntity rack)) {
				break;
			}
			racks.add(rack);
		}
		if (racks.isEmpty()) {
			return null;
		}
		for (Direction side : Direction.Plane.HORIZONTAL) {
			BlockPos drivePos = this.worldPosition.relative(side.getOpposite());
			BlockState driveState = this.level.getBlockState(drivePos);
			// The drive's front must face this head, so one drive can only ever run one rig.
			if (driveState.is(ModBlocks.DRIVE_HOUSING)
					&& driveState.getValue(DriveHousingBlock.FACING) == side
					&& this.level.getBlockEntity(this.worldPosition.relative(side)) instanceof OreBoxBlockEntity box
					&& this.level.getBlockEntity(drivePos) instanceof DriveHousingBlockEntity drive) {
				return new Parts(racks, box, drive);
			}
		}
		return null;
	}

	private void boreStep(ServerLevel level, Parts parts) {
		BlockPos next = this.worldPosition.below(this.depth + 1);
		if (next.getY() < level.getMinY()) {
			this.stall(Status.BOTTOM);
			return;
		}
		BlockState state = level.getBlockState(next);
		FluidState fluid = state.getFluidState();
		if (fluid.is(FluidTags.LAVA)) {
			this.stall(Status.LAVA);
			return;
		}
		if (isOre(state)) {
			this.queueVein(level, next);
			this.progress = 0;
			this.setChanged();
			return;
		}
		if (!canBore(level, next, state)) {
			this.stall(Status.BLOCKED);
			return;
		}
		int rodsNeeded = (this.depth + BLOCKS_PER_ROD) / BLOCKS_PER_ROD;
		if (rodsNeeded > this.rodsDown) {
			if (!parts.takeRod()) {
				this.stall(Status.NO_RODS);
				return;
			}
			this.rodsDown++;
		}

		boolean water = fluid.is(FluidTags.WATER) && fluid.isSource();
		level.setBlock(next, ModBlocks.DRILL_STRING.defaultBlockState().setValue(DrillStringBlock.WATERLOGGED, water), Block.UPDATE_ALL);
		this.depth++;
		this.progress = 0;
		level.playSound(null, this.worldPosition, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.8F, 0.6F + level.getRandom().nextFloat() * 0.2F);
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx != 0 || dz != 0) {
					BlockPos beside = next.offset(dx, 0, dz);
					if (!this.vein.contains(beside) && isOre(level.getBlockState(beside))) {
						this.queueVein(level, beside);
					}
				}
			}
		}
		this.setChanged();
	}

	/** Holds the step ready, so the rig retries as soon as the cause clears. */
	private void stall(Status reason) {
		this.status = reason;
		this.progress = Math.max(0, this.progress - 1);
	}

	private void mineVeinStep(ServerLevel level, Parts parts) {
		BlockPos target = this.vein.iterator().next();
		BlockState state = level.getBlockState(target);
		if (!isOre(state)) {
			this.vein.remove(target);
			this.setChanged();
			return;
		}
		List<ItemStack> drops = Block.getDrops(state, level, target, null, null, new ItemStack(Items.IRON_PICKAXE));
		if (!parts.box().canFit(drops)) {
			this.stall(Status.ORE_BOX_FULL);
			return;
		}
		for (ItemStack drop : drops) {
			parts.box().store(drop);
		}
		level.setBlock(target, replacementFor(state), Block.UPDATE_ALL);
		level.playSound(null, this.worldPosition, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.8F, 0.8F);
		this.vein.remove(target);
		this.progress = 0;
		this.setChanged();
	}

	private void recallStep(ServerLevel level, Parts parts) {
		if (this.depth > 0) {
			BlockPos cell = this.worldPosition.below(this.depth);
			BlockState fill = cell.getY() < 0 ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState();
			level.setBlock(cell, fill, Block.UPDATE_ALL);
			this.depth--;
			if (this.depth % BLOCKS_PER_ROD == 0) {
				level.playSound(null, this.worldPosition, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 0.8F, 0.8F);
			}
			this.setChanged();
			return;
		}

		int returned = 0;
		for (int rod = 0; rod < this.rodsDown; rod++) {
			if (level.getRandom().nextFloat() >= ROD_BREAK_CHANCE) {
				returned++;
			}
		}
		this.lastReturned = returned;
		this.lastBroken = this.rodsDown - returned;
		this.rodsDown = 0;
		int overflow = parts.addRods(returned);
		if (overflow > 0) {
			Containers.dropItemStack(level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.5, this.worldPosition.getZ() + 0.5,
					new ItemStack(ModItems.STEEL_DRILL_ROD, overflow));
		}
		if (this.lastBroken > 0) {
			level.playSound(null, this.worldPosition, SoundEvents.ITEM_BREAK.value(), SoundSource.BLOCKS, 0.8F, 0.8F);
		}
		this.recalling = false;
		this.recalled = true;
		this.status = Status.RECALLED;
		this.setChanged();
	}

	/**
	 * Sneak-use with an empty hand. Starts a recall, or after a recall starts boring again.
	 *
	 * @return the message for the player
	 */
	public Component toggleRecall() {
		if (this.recalled) {
			this.recalled = false;
			this.progress = 0;
			this.setChanged();
			return Component.translatable("block.from-stone-to-stars.drill_head.resumed");
		}
		if (this.recalling) {
			return Component.translatable("block.from-stone-to-stars.drill_head.status.recalling", this.depth);
		}
		this.recalling = true;
		this.vein.clear();
		this.progress = 0;
		this.setChanged();
		return Component.translatable("block.from-stone-to-stars.drill_head.recall_started", this.depth);
	}

	public Component statusMessage() {
		String key = "block.from-stone-to-stars.drill_head.status." + this.status.name().toLowerCase();
		return switch (this.status) {
			case BORING -> Component.translatable(key, this.depth, this.maxDepth());
			case MINING_VEIN -> Component.translatable(key, this.vein.size());
			case RECALLING -> Component.translatable(key, this.depth);
			case RECALLED -> Component.translatable(key, this.lastReturned, this.lastBroken);
			default -> Component.translatable(key);
		};
	}

	private void queueVein(Level level, BlockPos start) {
		String family = family(level.getBlockState(start));
		Queue<BlockPos> frontier = new ArrayDeque<>();
		frontier.add(start);
		this.vein.add(start);
		int added = 1;
		while (!frontier.isEmpty() && added < MAX_VEIN) {
			BlockPos current = frontier.remove();
			for (BlockPos neighbour : BlockPos.betweenClosed(current.offset(-1, -1, -1), current.offset(1, 1, 1))) {
				if (added >= MAX_VEIN) {
					break;
				}
				if (this.vein.contains(neighbour)) {
					continue;
				}
				BlockState state = level.getBlockState(neighbour);
				if (isOre(state) && family(state).equals(family)) {
					BlockPos found = neighbour.immutable();
					this.vein.add(found);
					frontier.add(found);
					added++;
				}
			}
		}
	}

	private static boolean isOre(BlockState state) {
		return state.is(ModTags.DRILL_ORES);
	}

	/** Deepslate and stone forms of an ore count as one vein. */
	private static String family(BlockState state) {
		var id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
		return id.getNamespace() + ":" + id.getPath().replace("deepslate_", "");
	}

	private static BlockState replacementFor(BlockState ore) {
		return BuiltInRegistries.BLOCK.getKey(ore.getBlock()).getPath().startsWith("deepslate_")
				? Blocks.DEEPSLATE.defaultBlockState()
				: Blocks.STONE.defaultBlockState();
	}

	/** Anything an unenchanted iron pick could take that is not a block entity or the bottom of the world. */
	private static boolean canBore(Level level, BlockPos pos, BlockState state) {
		if (state.isAir() || state.is(ModBlocks.DRILL_STRING) || (state.getFluidState().is(FluidTags.WATER) && state.canBeReplaced())) {
			return true;
		}
		if (state.hasBlockEntity() || state.getDestroySpeed(level, pos) < 0.0F) {
			return false;
		}
		return !state.requiresCorrectToolForDrops() || new ItemStack(Items.IRON_PICKAXE).isCorrectToolForDrops(state);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("Depth", this.depth);
		output.putInt("RodsDown", this.rodsDown);
		output.putInt("Progress", this.progress);
		output.putBoolean("Recalling", this.recalling);
		output.putBoolean("Recalled", this.recalled);
		output.putInt("LastReturned", this.lastReturned);
		output.putInt("LastBroken", this.lastBroken);
		List<Long> packed = new ArrayList<>(this.vein.size());
		for (BlockPos pos : this.vein) {
			packed.add(pos.asLong());
		}
		output.store("Vein", Codec.LONG.listOf(), packed);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.depth = input.getIntOr("Depth", 0);
		this.rodsDown = input.getIntOr("RodsDown", 0);
		this.progress = input.getIntOr("Progress", 0);
		this.recalling = input.getBooleanOr("Recalling", false);
		this.recalled = input.getBooleanOr("Recalled", false);
		this.lastReturned = input.getIntOr("LastReturned", 0);
		this.lastBroken = input.getIntOr("LastBroken", 0);
		this.vein.clear();
		input.read("Vein", Codec.LONG.listOf()).ifPresent(list -> list.forEach(packed -> this.vein.add(BlockPos.of(packed))));
	}
}
