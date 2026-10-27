package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.tags.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Discovers and validates a sawmill around a saw arbor.
 *
 * <pre>
 *   side view (logs travel →)        front view of the blade wall
 *            F  F  F  F                     F F F F F
 *            F  S                           F S S S F
 *   I  R  R  S  R  R  O                     A S S S F
 * </pre>
 *
 * The arbor sits in one side column and faces into a wall of steel saw blades
 * (1–3 wide, 2–3 high) whose sides and top are framed. At ground level, each lane
 * continues both ways along the line through 1–4 rows of wooden rollers, ending in
 * a full row of log intakes on one end and plank outfeeds on the other.
 */
public final class SawmillLayout {
	public static final int MIN_WIDTH = 1;
	public static final int MAX_WIDTH = 3;
	public static final int MIN_BLADE_HEIGHT = 2;
	public static final int MAX_BLADE_HEIGHT = 3;
	public static final int MIN_BED = 1;
	public static final int MAX_BED = 4;

	public static final String RULE_ID = "sawmill_layout";

	private final int width;
	private final int bladeHeight;
	private final List<BlockPos> blades;
	private final List<BlockPos> frame;
	private final List<BlockPos> infeedRollers;
	private final List<BlockPos> outfeedRollers;
	private final List<BlockPos> intakes;
	private final List<BlockPos> outfeeds;
	private final List<BlockPos> lanes;

	private SawmillLayout(
			int width,
			int bladeHeight,
			List<BlockPos> blades,
			List<BlockPos> frame,
			List<BlockPos> infeedRollers,
			List<BlockPos> outfeedRollers,
			List<BlockPos> intakes,
			List<BlockPos> outfeeds,
			List<BlockPos> lanes
	) {
		this.width = width;
		this.bladeHeight = bladeHeight;
		this.blades = List.copyOf(blades);
		this.frame = List.copyOf(frame);
		this.infeedRollers = List.copyOf(infeedRollers);
		this.outfeedRollers = List.copyOf(outfeedRollers);
		this.intakes = List.copyOf(intakes);
		this.outfeeds = List.copyOf(outfeeds);
		this.lanes = List.copyOf(lanes);
	}

	public int width() {
		return this.width;
	}

	public int bladeHeight() {
		return this.bladeHeight;
	}

	public List<BlockPos> infeedRollers() {
		return this.infeedRollers;
	}

	public List<BlockPos> outfeedRollers() {
		return this.outfeedRollers;
	}

	/** Bottom blade of each lane, where logs pass through. */
	public List<BlockPos> lanes() {
		return this.lanes;
	}

	/** Same footprint and sizes, so capacities and speed are unchanged. */
	public boolean sameShape(SawmillLayout other) {
		return other != null
				&& this.width == other.width
				&& this.bladeHeight == other.bladeHeight
				&& this.infeedRollers.equals(other.infeedRollers)
				&& this.outfeedRollers.equals(other.outfeedRollers)
				&& this.intakes.equals(other.intakes)
				&& this.outfeeds.equals(other.outfeeds);
	}

	public List<ComponentRule.ResolvedComponent> toResolvedComponents() {
		List<ComponentRule.ResolvedComponent> list = new ArrayList<>();
		list.add(new ComponentRule.ResolvedComponent(this.blades, MultiblockRole.FRAME, "blades"));
		list.add(new ComponentRule.ResolvedComponent(this.frame, MultiblockRole.FRAME, "frame"));
		list.add(new ComponentRule.ResolvedComponent(this.infeedRollers, MultiblockRole.INPUT, "infeed"));
		list.add(new ComponentRule.ResolvedComponent(this.intakes, MultiblockRole.INPUT, "intakes"));
		list.add(new ComponentRule.ResolvedComponent(this.outfeedRollers, MultiblockRole.OUTPUT, "outfeed"));
		list.add(new ComponentRule.ResolvedComponent(this.outfeeds, MultiblockRole.OUTPUT, "outfeeds"));
		return list;
	}

	public record Attempt(Optional<SawmillLayout> layout, Optional<Component> failure) {
		public static Attempt success(SawmillLayout layout) {
			return new Attempt(Optional.of(layout), Optional.empty());
		}

		public static Attempt fail(Component reason) {
			return new Attempt(Optional.empty(), Optional.of(reason));
		}

		public boolean ok() {
			return this.layout.isPresent();
		}
	}

	private enum BedEnd {
		INTAKE,
		OUTFEED
	}

	private record Bed(Optional<Component> failure, List<BlockPos> rollers, List<BlockPos> ends, BedEnd end) {
		static Bed fail(Component reason) {
			return new Bed(Optional.of(reason), List.of(), List.of(), null);
		}
	}

	public static Optional<SawmillLayout> tryMatch(LevelAccessor level, BlockPos arbor) {
		return attempt(level, arbor).layout();
	}

	public static Attempt attempt(LevelAccessor level, BlockPos arbor) {
		BlockState arborState = level.getBlockState(arbor);
		if (!arborState.is(ModBlocks.SAW_ARBOR)) {
			return fail("not_arbor", posText(arbor));
		}

		Direction across = arborState.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction along = across.getClockWise();
		Direction.Axis lineAxis = along.getAxis();

		BlockPos firstBlade = arbor.relative(across);
		if (!level.getBlockState(firstBlade).is(ModBlocks.STEEL_SAW_BLADES)) {
			return fail("no_blade", blockName(level, firstBlade), posText(firstBlade));
		}

		int bottom = firstBlade.getY();
		while (bottom > firstBlade.getY() - MAX_BLADE_HEIGHT && isBladeBlock(level, firstBlade.atY(bottom - 1))) {
			bottom--;
		}
		int top = firstBlade.getY();
		while (top < firstBlade.getY() + MAX_BLADE_HEIGHT && isBladeBlock(level, firstBlade.atY(top + 1))) {
			top++;
		}
		int height = top - bottom + 1;
		if (height < MIN_BLADE_HEIGHT) {
			return fail("blade_too_short", MIN_BLADE_HEIGHT);
		}
		if (height > MAX_BLADE_HEIGHT) {
			return fail("blade_too_tall", MAX_BLADE_HEIGHT);
		}

		int width = 0;
		while (width <= MAX_WIDTH && isBladeBlock(level, firstBlade.relative(across, width))) {
			width++;
		}
		if (width > MAX_WIDTH) {
			return fail("too_wide", MAX_WIDTH);
		}

		List<BlockPos> blades = new ArrayList<>();
		List<BlockPos> lanes = new ArrayList<>();
		for (int i = 0; i < width; i++) {
			BlockPos column = arbor.relative(across, i + 1);
			lanes.add(column.atY(bottom));
			for (int y = bottom; y <= top; y++) {
				BlockPos blade = column.atY(y);
				BlockState state = level.getBlockState(blade);
				if (!state.is(ModBlocks.STEEL_SAW_BLADES)) {
					return fail("expected_blade", blockName(level, blade), posText(blade));
				}
				if (state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() != lineAxis) {
					return fail("blade_direction", posText(blade));
				}
				blades.add(blade);
			}
		}

		List<BlockPos> frame = new ArrayList<>();
		for (int y = bottom; y <= top; y++) {
			if (y != arbor.getY()) {
				BlockPos near = arbor.atY(y);
				if (!isFrame(level, near)) {
					return fail("missing_side_support", blockName(level, near), posText(near));
				}
				frame.add(near);
			}
			BlockPos far = arbor.relative(across, width + 1).atY(y);
			if (!isFrame(level, far)) {
				return fail("missing_side_support", blockName(level, far), posText(far));
			}
			frame.add(far);
		}
		for (int i = 0; i <= width + 1; i++) {
			BlockPos beam = arbor.relative(across, i).atY(top + 1);
			if (!isFrame(level, beam)) {
				return fail("missing_top_support", blockName(level, beam), posText(beam));
			}
			frame.add(beam);
		}

		Bed forward = scanBed(level, lanes, along, lineAxis);
		if (forward.failure().isPresent()) {
			return Attempt.fail(forward.failure().get());
		}
		Bed backward = scanBed(level, lanes, along.getOpposite(), lineAxis);
		if (backward.failure().isPresent()) {
			return Attempt.fail(backward.failure().get());
		}
		if (forward.end() == backward.end()) {
			return failKey("needs_both_ends");
		}

		Bed infeed = forward.end() == BedEnd.INTAKE ? forward : backward;
		Bed outfeed = infeed == forward ? backward : forward;
		return Attempt.success(new SawmillLayout(
				width,
				height,
				blades,
				frame,
				infeed.rollers(),
				outfeed.rollers(),
				infeed.ends(),
				outfeed.ends(),
				lanes
		));
	}

	private static Bed scanBed(LevelAccessor level, List<BlockPos> lanes, Direction direction, Direction.Axis lineAxis) {
		List<BlockPos> rollers = new ArrayList<>();
		int rows = 0;
		while (true) {
			List<BlockPos> row = rowAt(lanes, direction, rows + 1);
			int rollerCount = 0;
			BlockPos firstMissing = null;
			for (BlockPos pos : row) {
				BlockState state = level.getBlockState(pos);
				if (state.is(ModBlocks.WOODEN_ROLLERS)) {
					if (state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() != lineAxis) {
						return Bed.fail(translatable("roller_direction", posText(pos)));
					}
					rollerCount++;
				} else if (firstMissing == null) {
					firstMissing = pos;
				}
			}
			if (rollerCount == 0) {
				break;
			}
			if (firstMissing != null) {
				return Bed.fail(translatable("partial_roller_row", blockName(level, firstMissing), posText(firstMissing)));
			}
			rows++;
			if (rows > MAX_BED) {
				return Bed.fail(translatable("bed_too_long", MAX_BED));
			}
			rollers.addAll(row);
		}

		List<BlockPos> endRow = rowAt(lanes, direction, rows + 1);
		if (rows < MIN_BED) {
			BlockPos first = endRow.getFirst();
			return Bed.fail(translatable("no_rollers", MIN_BED, posText(first)));
		}

		BedEnd end = null;
		for (BlockPos pos : endRow) {
			BlockState state = level.getBlockState(pos);
			BedEnd here = state.is(ModBlocks.LOG_INTAKE) ? BedEnd.INTAKE
					: state.is(ModBlocks.PLANK_OUTFEED) ? BedEnd.OUTFEED
					: null;
			if (here == null || (end != null && here != end)) {
				return Bed.fail(translatable("bed_end", blockName(level, pos), posText(pos)));
			}
			end = here;
		}
		return new Bed(Optional.empty(), rollers, endRow, end);
	}

	private static List<BlockPos> rowAt(List<BlockPos> lanes, Direction direction, int distance) {
		List<BlockPos> row = new ArrayList<>(lanes.size());
		for (BlockPos lane : lanes) {
			row.add(lane.relative(direction, distance));
		}
		return row;
	}

	private static boolean isBladeBlock(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos).is(ModBlocks.STEEL_SAW_BLADES);
	}

	private static boolean isFrame(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos).is(ModTags.SAWMILL_FRAME);
	}

	private static Attempt failKey(String key) {
		return Attempt.fail(translatable(key));
	}

	private static Attempt fail(String key, Object... args) {
		return Attempt.fail(translatable(key, args));
	}

	private static Component translatable(String key, Object... args) {
		return Component.translatable("multiblock.from-stone-to-stars.fail.sawmill." + key, args);
	}

	private static Component blockName(LevelAccessor level, BlockPos pos) {
		Block block = level.getBlockState(pos).getBlock();
		return block.getName();
	}

	private static String posText(BlockPos pos) {
		return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}

	public static final class Rule implements MultiRoleRule {
		public static final Rule INSTANCE = new Rule();

		private Rule() {
		}

		@Override
		public String id() {
			return RULE_ID;
		}

		@Override
		public MultiblockRole role() {
			return MultiblockRole.CONTROLLER;
		}

		@Override
		public boolean required() {
			return true;
		}

		@Override
		public Component description() {
			return Component.translatable("multiblock.from-stone-to-stars.rule.sawmill_layout");
		}

		@Override
		public Optional<List<ResolvedComponent>> resolveAll(LevelAccessor level, BlockPos controller) {
			return attempt(level, controller).layout().map(SawmillLayout::toResolvedComponents);
		}

		@Override
		public MultiRoleRule.Attempt attemptAll(LevelAccessor level, BlockPos controller) {
			SawmillLayout.Attempt result = attempt(level, controller);
			if (result.ok()) {
				return MultiRoleRule.Attempt.success(result.layout().orElseThrow().toResolvedComponents());
			}
			return MultiRoleRule.Attempt.fail(result.failure().orElse(description()));
		}
	}
}
