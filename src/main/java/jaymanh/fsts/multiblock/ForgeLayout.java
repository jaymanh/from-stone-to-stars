package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.tags.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Discovers and validates the substantial forge assembly around a clicked hearth.
 *
 * <pre>
 *   chimney air 1x1–3x3 (≥2 tall), framed
 *   hearth layer: 1x1–3x3 hearths on coal centre; sides framed;
 *                 exactly one side is a straight bellows line; optional 1 air access gap
 *   coal pad 3x3–5x5 with frame under and around sides
 * </pre>
 */
public final class ForgeLayout {
	public static final int MIN_COAL = 1;
	public static final int MAX_COAL = 5;
	public static final int MIN_HEARTH = 1;
	public static final int MAX_HEARTH = 5;
	public static final int MIN_CHIMNEY_HEIGHT = 2;
	public static final int MAX_CHIMNEY = 3;

	public static final String RULE_ID = "forge_layout";

	private final List<BlockPos> coal;
	private final List<BlockPos> frame;
	private final List<BlockPos> hearths;
	private final List<BlockPos> bellows;
	private final List<BlockPos> chimneyAir;
	private final List<BlockPos> accessAir;
	private final Direction bellowsSide;

	private ForgeLayout(
			List<BlockPos> coal,
			List<BlockPos> frame,
			List<BlockPos> hearths,
			List<BlockPos> bellows,
			List<BlockPos> chimneyAir,
			List<BlockPos> accessAir,
			Direction bellowsSide
	) {
		this.coal = List.copyOf(coal);
		this.frame = List.copyOf(frame);
		this.hearths = List.copyOf(hearths);
		this.bellows = List.copyOf(bellows);
		this.chimneyAir = List.copyOf(chimneyAir);
		this.accessAir = List.copyOf(accessAir);
		this.bellowsSide = bellowsSide;
	}

	public List<BlockPos> coal() {
		return this.coal;
	}

	public List<BlockPos> hearths() {
		return this.hearths;
	}

	public List<BlockPos> bellows() {
		return this.bellows;
	}

	public List<BlockPos> accessAir() {
		return this.accessAir;
	}

	public Direction bellowsSide() {
		return this.bellowsSide;
	}

	public List<ComponentRule.ResolvedComponent> toResolvedComponents() {
		List<ComponentRule.ResolvedComponent> list = new ArrayList<>();
		list.add(new ComponentRule.ResolvedComponent(this.coal, MultiblockRole.CONSUMED, "fuel_pit"));
		list.add(new ComponentRule.ResolvedComponent(this.frame, MultiblockRole.FRAME, "frame"));
		list.add(new ComponentRule.ResolvedComponent(this.hearths, MultiblockRole.CONTROLLER, "hearths"));
		list.add(new ComponentRule.ResolvedComponent(this.bellows, MultiblockRole.INPUT, "bellows"));
		if (!this.chimneyAir.isEmpty()) {
			list.add(new ComponentRule.ResolvedComponent(this.chimneyAir, MultiblockRole.ORIGIN, "chimney"));
		}
		if (!this.accessAir.isEmpty()) {
			list.add(new ComponentRule.ResolvedComponent(this.accessAir, MultiblockRole.ORIGIN, "access"));
		}
		return list;
	}

	public record Attempt(Optional<ForgeLayout> layout, Optional<Component> failure) {
		public static Attempt success(ForgeLayout layout) {
			return new Attempt(Optional.of(layout), Optional.empty());
		}

		public static Attempt fail(Component reason) {
			return new Attempt(Optional.empty(), Optional.of(reason));
		}

		public boolean ok() {
			return this.layout.isPresent();
		}
	}

	public static Optional<ForgeLayout> tryMatch(LevelAccessor level, BlockPos hearthClick) {
		return attempt(level, hearthClick).layout();
	}

	public static Attempt attempt(LevelAccessor level, BlockPos hearthClick) {
		if (!level.getBlockState(hearthClick).is(ModBlocks.FORGE_HEARTH)) {
			return fail("not_hearth", blockName(level, hearthClick), posText(hearthClick));
		}

		BlockPos coalSeed = hearthClick.below();
		if (!isCoal(level, coalSeed)) {
			return fail("coal_under_hearth", blockName(level, coalSeed), posText(coalSeed));
		}

		Rect coalRect = expandSolidRect(level, coalSeed, ForgeLayout::isCoal, MIN_COAL, MAX_COAL);
		if (coalRect == null) {
			return fail("coal_pad_size", MIN_COAL, MIN_COAL, MAX_COAL, MAX_COAL);
		}

		List<BlockPos> coal = coalRect.cells(coalSeed.getY());
		List<BlockPos> frame = new ArrayList<>();

		for (BlockPos c : coal) {
			BlockPos under = c.below();
			if (!isFrame(level, under)) {
				return fail("missing_frame_under", blockName(level, under), posText(under));
			}
			frame.add(under.immutable());
		}

		for (BlockPos c : coal) {
			for (Direction dir : Direction.Plane.HORIZONTAL) {
				BlockPos side = c.relative(dir);
				if (coalRect.contains(side.getX(), side.getZ())) {
					continue;
				}
				if (!isFrame(level, side)) {
					return fail("missing_frame_beside_coal", blockName(level, side), posText(side));
				}
				frame.add(side.immutable());
			}
		}

		Rect hearthRect = findHearthRect(level, hearthClick, coalRect);
		if (hearthRect == null) {
			return failKey("no_hearth_cluster");
		}
		if (!isCentered(hearthRect, coalRect)) {
			return failKey("hearth_not_centred");
		}

		List<BlockPos> hearths = new ArrayList<>();
		for (int x = hearthRect.minX; x <= hearthRect.maxX; x++) {
			for (int z = hearthRect.minZ; z <= hearthRect.maxZ; z++) {
				BlockPos h = new BlockPos(x, hearthClick.getY(), z);
				if (!level.getBlockState(h).is(ModBlocks.FORGE_HEARTH)) {
					return fail("expected_hearth", blockName(level, h), posText(h));
				}
				if (!isCoal(level, h.below())) {
					return fail("hearth_not_on_coal", posText(h));
				}
				hearths.add(h);
			}
		}

		SidesAttempt sides = validateHearthSides(level, hearthRect, hearthClick.getY());
		if (!sides.ok()) {
			return Attempt.fail(sides.failure().orElseThrow());
		}
		frame.addAll(sides.frame());
		List<BlockPos> bellows = sides.bellows();
		List<BlockPos> accessAir = sides.accessAir();

		for (BlockPos h : hearths) {
			BlockPos above = h.above();
			BlockState state = level.getBlockState(above);
			if (!state.isAir() && !isFrame(level, above)) {
				return fail("blocked_above_hearth", blockName(level, above), posText(above));
			}
			if (isFrame(level, above)) {
				frame.add(above.immutable());
			}
		}

		ChimneyAttempt chimney = findChimney(level, hearthRect, hearthClick.getY(), frame);
		if (!chimney.ok()) {
			return Attempt.fail(chimney.failure().orElseThrow());
		}

		return Attempt.success(new ForgeLayout(
				coal,
				dedupe(frame),
				hearths,
				bellows,
				chimney.air(),
				accessAir,
				sides.bellowsSide()
		));
	}

	private static Attempt failKey(String key) {
		return Attempt.fail(Component.translatable("multiblock.from-stone-to-stars.fail.forge." + key));
	}

	private static Attempt fail(String key, Object... args) {
		return Attempt.fail(Component.translatable("multiblock.from-stone-to-stars.fail.forge." + key, args));
	}

	private static Component blockName(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos).getBlock().getName();
	}

	private static String posText(BlockPos pos) {
		return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}

	private static Rect findHearthRect(LevelAccessor level, BlockPos click, Rect coalRect) {
		int y = click.getY();
		Rect best = null;
		for (int minX = coalRect.minX; minX <= click.getX(); minX++) {
			for (int maxX = click.getX(); maxX <= coalRect.maxX; maxX++) {
				for (int minZ = coalRect.minZ; minZ <= click.getZ(); minZ++) {
					for (int maxZ = click.getZ(); maxZ <= coalRect.maxZ; maxZ++) {
						int w = maxX - minX + 1;
						int d = maxZ - minZ + 1;
						if (w < MIN_HEARTH || w > MAX_HEARTH || d < MIN_HEARTH || d > MAX_HEARTH) {
							continue;
						}
						boolean solid = true;
						for (int x = minX; x <= maxX && solid; x++) {
							for (int z = minZ; z <= maxZ; z++) {
								if (!level.getBlockState(new BlockPos(x, y, z)).is(ModBlocks.FORGE_HEARTH)) {
									solid = false;
									break;
								}
							}
						}
						if (!solid) {
							continue;
						}
						Rect candidate = new Rect(minX, maxX, minZ, maxZ);
						if (best == null || candidate.area() > best.area()) {
							best = candidate;
						}
					}
				}
			}
		}
		return best;
	}

	private static boolean isCentered(Rect hearth, Rect coal) {
		int coalW = coal.width();
		int coalD = coal.depth();
		int centreMinX = coal.minX + (coalW - Math.min(MAX_HEARTH, coalW)) / 2;
		int centreMaxX = centreMinX + Math.min(MAX_HEARTH, coalW) - 1;
		int centreMinZ = coal.minZ + (coalD - Math.min(MAX_HEARTH, coalD)) / 2;
		int centreMaxZ = centreMinZ + Math.min(MAX_HEARTH, coalD) - 1;
		return hearth.minX >= centreMinX && hearth.maxX <= centreMaxX
				&& hearth.minZ >= centreMinZ && hearth.maxZ <= centreMaxZ;
	}

	private record SidesAttempt(
			Optional<Component> failure,
			Direction bellowsSide,
			List<BlockPos> bellows,
			List<BlockPos> frame,
			List<BlockPos> accessAir
	) {
		static SidesAttempt ok(Direction side, List<BlockPos> bellows, List<BlockPos> frame, List<BlockPos> access) {
			return new SidesAttempt(Optional.empty(), side, bellows, frame, access);
		}

		static SidesAttempt fail(Component reason) {
			return new SidesAttempt(Optional.of(reason), Direction.NORTH, List.of(), List.of(), List.of());
		}

		boolean ok() {
			return this.failure.isEmpty();
		}
	}

	private static SidesAttempt validateHearthSides(LevelAccessor level, Rect hearth, int y) {
		record SideCells(Direction side, List<BlockPos> cells) {
		}
		List<SideCells> sides = List.of(
				new SideCells(Direction.NORTH, sideCells(hearth, y, Direction.NORTH)),
				new SideCells(Direction.SOUTH, sideCells(hearth, y, Direction.SOUTH)),
				new SideCells(Direction.WEST, sideCells(hearth, y, Direction.WEST)),
				new SideCells(Direction.EAST, sideCells(hearth, y, Direction.EAST))
		);

		Direction bellowsSide = null;
		for (SideCells side : sides) {
			boolean anyBellows = side.cells().stream().anyMatch(p -> level.getBlockState(p).is(ModBlocks.BELLOWS));
			if (anyBellows) {
				if (bellowsSide != null) {
					return SidesAttempt.fail(Component.translatable("multiblock.from-stone-to-stars.fail.forge.bellows_multiple_sides"));
				}
				bellowsSide = side.side();
			}
		}
		if (bellowsSide == null) {
			return SidesAttempt.fail(Component.translatable("multiblock.from-stone-to-stars.fail.forge.missing_bellows"));
		}

		List<BlockPos> bellows = new ArrayList<>();
		List<BlockPos> frame = new ArrayList<>();
		List<BlockPos> accessAir = new ArrayList<>();
		int airGaps = 0;

		for (SideCells side : sides) {
			if (side.side() == bellowsSide) {
				for (BlockPos cell : side.cells()) {
					if (!level.getBlockState(cell).is(ModBlocks.BELLOWS)) {
						return SidesAttempt.fail(Component.translatable(
								"multiblock.from-stone-to-stars.fail.forge.bellows_incomplete_side",
								blockName(level, cell),
								posText(cell)
						));
					}
					if (!facesTowardHearth(level, cell, bellowsSide.getOpposite())) {
						return SidesAttempt.fail(Component.translatable(
								"multiblock.from-stone-to-stars.fail.forge.bellows_wrong_facing",
								posText(cell)
						));
					}
					bellows.add(cell.immutable());
				}
				List<BlockPos> extended = extendBellowsRect(level, bellows, bellowsSide.getOpposite());
				if (extended == null) {
					return SidesAttempt.fail(Component.translatable("multiblock.from-stone-to-stars.fail.forge.bellows_not_rectangle"));
				}
				bellows.clear();
				bellows.addAll(extended);
			} else {
				for (BlockPos cell : side.cells()) {
					BlockState state = level.getBlockState(cell);
					if (state.isAir()) {
						airGaps++;
						if (airGaps > 1) {
							return SidesAttempt.fail(Component.translatable(
									"multiblock.from-stone-to-stars.fail.forge.too_many_access_gaps",
									posText(cell)
							));
						}
						accessAir.add(cell.immutable());
					} else if (isFrame(level, cell)) {
						frame.add(cell.immutable());
					} else {
						return SidesAttempt.fail(Component.translatable(
								"multiblock.from-stone-to-stars.fail.forge.unexpected_beside_hearth",
								blockName(level, cell),
								posText(cell)
						));
					}
				}
			}
		}

		return SidesAttempt.ok(bellowsSide, bellows, frame, accessAir);
	}

	private static List<BlockPos> sideCells(Rect hearth, int y, Direction side) {
		List<BlockPos> cells = new ArrayList<>();
		switch (side) {
			case NORTH -> {
				for (int x = hearth.minX; x <= hearth.maxX; x++) {
					cells.add(new BlockPos(x, y, hearth.minZ - 1));
				}
			}
			case SOUTH -> {
				for (int x = hearth.minX; x <= hearth.maxX; x++) {
					cells.add(new BlockPos(x, y, hearth.maxZ + 1));
				}
			}
			case WEST -> {
				for (int z = hearth.minZ; z <= hearth.maxZ; z++) {
					cells.add(new BlockPos(hearth.minX - 1, y, z));
				}
			}
			case EAST -> {
				for (int z = hearth.minZ; z <= hearth.maxZ; z++) {
					cells.add(new BlockPos(hearth.maxX + 1, y, z));
				}
			}
			default -> {
			}
		}
		return cells;
	}

	private static List<BlockPos> extendBellowsRect(LevelAccessor level, List<BlockPos> sideBellows, Direction faceToward) {
		Set<BlockPos> found = new HashSet<>();
		for (BlockPos p : sideBellows) {
			found.add(p.immutable());
		}

		boolean changed = true;
		while (changed) {
			changed = false;
			Set<BlockPos> add = new HashSet<>();
			for (BlockPos p : found) {
				for (Direction dir : Direction.values()) {
					BlockPos n = p.relative(dir);
					if (found.contains(n) || !level.getBlockState(n).is(ModBlocks.BELLOWS)) {
						continue;
					}
					if (!facesTowardHearth(level, n, faceToward)) {
						continue;
					}
					add.add(n.immutable());
				}
			}
			if (!add.isEmpty()) {
				found.addAll(add);
				changed = true;
			}
		}

		if (!isSolidRectangle(found)) {
			return null;
		}
		return new ArrayList<>(found);
	}

	/** Axis-aligned solid rectangle (line, flat bank, or vertical slab of bellows). */
	private static boolean isSolidRectangle(Set<BlockPos> positions) {
		if (positions.isEmpty()) {
			return false;
		}
		if (positions.size() == 1) {
			return true;
		}
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxZ = Integer.MIN_VALUE;
		for (BlockPos p : positions) {
			minX = Math.min(minX, p.getX());
			maxX = Math.max(maxX, p.getX());
			minY = Math.min(minY, p.getY());
			maxY = Math.max(maxY, p.getY());
			minZ = Math.min(minZ, p.getZ());
			maxZ = Math.max(maxZ, p.getZ());
		}
		int expected = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
		if (expected != positions.size()) {
			return false;
		}
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					if (!positions.contains(new BlockPos(x, y, z))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static boolean facesTowardHearth(LevelAccessor level, BlockPos bellowsPos, Direction requiredFacing) {
		BlockState state = level.getBlockState(bellowsPos);
		if (!state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
			return false;
		}
		return state.getValue(BlockStateProperties.HORIZONTAL_FACING) == requiredFacing;
	}

	private record ChimneyAttempt(Optional<Component> failure, List<BlockPos> air, List<BlockPos> frames) {
		static ChimneyAttempt ok(List<BlockPos> air) {
			return ok(air, List.of());
		}

		static ChimneyAttempt ok(List<BlockPos> air, List<BlockPos> frames) {
			return new ChimneyAttempt(Optional.empty(), air, frames);
		}

		static ChimneyAttempt fail(Component reason) {
			return new ChimneyAttempt(Optional.of(reason), List.of(), List.of());
		}

		boolean ok() {
			return this.failure.isEmpty();
		}
	}

	private static ChimneyAttempt findChimney(LevelAccessor level, Rect hearth, int hearthY, List<BlockPos> frameOut) {
		Component lastHint = Component.translatable("multiblock.from-stone-to-stars.fail.forge.chimney");

		// Any axis-aligned shaft that fits inside the hearth footprint (centred or offset).
		for (int w = Math.min(MAX_CHIMNEY, hearth.width()); w >= 1; w--) {
			for (int d = Math.min(MAX_CHIMNEY, hearth.depth()); d >= 1; d--) {
				for (int minX = hearth.minX; minX + w - 1 <= hearth.maxX; minX++) {
					for (int minZ = hearth.minZ; minZ + d - 1 <= hearth.maxZ; minZ++) {
						Rect shaft = new Rect(minX, minX + w - 1, minZ, minZ + d - 1);
						ChimneyAttempt result = tryChimneyShaft(level, shaft, hearthY);
						if (result.ok()) {
							frameOut.addAll(result.frames());
							return ChimneyAttempt.ok(result.air());
						}
						if (result.failure().isPresent()) {
							lastHint = result.failure().get();
						}
					}
				}
			}
		}
		return ChimneyAttempt.fail(lastHint);
	}

	private static ChimneyAttempt tryChimneyShaft(LevelAccessor level, Rect shaft, int hearthY) {
		Component lastHint = Component.translatable("multiblock.from-stone-to-stars.fail.forge.chimney");
		List<BlockPos> lidFrames = new ArrayList<>();

		int startY = hearthY + 1;
		while (startY <= hearthY + 8) {
			boolean allAir = true;
			boolean allFrame = true;
			BlockPos problem = null;
			for (int x = shaft.minX; x <= shaft.maxX; x++) {
				for (int z = shaft.minZ; z <= shaft.maxZ; z++) {
					BlockPos p = new BlockPos(x, startY, z);
					if (!level.getBlockState(p).isAir()) {
						allAir = false;
					}
					if (!isFrame(level, p)) {
						allFrame = false;
						if (!level.getBlockState(p).isAir()) {
							problem = p;
						}
					}
				}
			}
			if (allAir) {
				break;
			}
			if (allFrame) {
				for (int x = shaft.minX; x <= shaft.maxX; x++) {
					for (int z = shaft.minZ; z <= shaft.maxZ; z++) {
						lidFrames.add(new BlockPos(x, startY, z));
					}
				}
				startY++;
				continue;
			}
			if (problem != null) {
				return ChimneyAttempt.fail(Component.translatable(
						"multiblock.from-stone-to-stars.fail.forge.chimney_blocked",
						blockName(level, problem),
						posText(problem)
				));
			}
			return ChimneyAttempt.fail(lastHint);
		}

		List<BlockPos> air = new ArrayList<>();
		List<BlockPos> sideFrames = new ArrayList<>();
		int y = startY;
		BlockPos missingFrame = null;
		int shaftArea = shaft.area();
		while (y < startY + 16) {
			boolean layerAir = true;
			for (int x = shaft.minX; x <= shaft.maxX && layerAir; x++) {
				for (int z = shaft.minZ; z <= shaft.maxZ; z++) {
					if (!level.getBlockState(new BlockPos(x, y, z)).isAir()) {
						layerAir = false;
					}
				}
			}
			if (!layerAir) {
				break;
			}

			List<BlockPos> layerFrames = new ArrayList<>();
			boolean framed = true;
			for (int x = shaft.minX; x <= shaft.maxX && framed; x++) {
				for (int z = shaft.minZ; z <= shaft.maxZ; z++) {
					BlockPos cell = new BlockPos(x, y, z);
					for (Direction dir : Direction.Plane.HORIZONTAL) {
						BlockPos side = cell.relative(dir);
						if (shaft.contains(side.getX(), side.getZ())) {
							continue;
						}
						if (!isFrame(level, side)) {
							framed = false;
							missingFrame = side;
							break;
						}
						layerFrames.add(side.immutable());
					}
					if (!framed) {
						break;
					}
				}
			}
			if (!framed) {
				break;
			}

			for (int x = shaft.minX; x <= shaft.maxX; x++) {
				for (int z = shaft.minZ; z <= shaft.maxZ; z++) {
					air.add(new BlockPos(x, y, z));
				}
			}
			sideFrames.addAll(layerFrames);
			y++;
		}
		int height = shaftArea == 0 ? 0 : air.size() / shaftArea;
		if (height >= MIN_CHIMNEY_HEIGHT) {
			List<BlockPos> frames = new ArrayList<>(lidFrames);
			frames.addAll(sideFrames);
			return ChimneyAttempt.ok(air, frames);
		}
		if (height > 0) {
			return ChimneyAttempt.fail(Component.translatable(
					"multiblock.from-stone-to-stars.fail.forge.chimney_too_short",
					MIN_CHIMNEY_HEIGHT,
					height
			));
		}
		if (missingFrame != null) {
			return ChimneyAttempt.fail(Component.translatable(
					"multiblock.from-stone-to-stars.fail.forge.chimney_missing_frame",
					blockName(level, missingFrame),
					posText(missingFrame)
			));
		}
		return ChimneyAttempt.fail(lastHint);
	}

	private static Rect expandSolidRect(LevelAccessor level, BlockPos seed, CellTest test, int min, int max) {
		int minX = seed.getX();
		int maxX = seed.getX();
		int minZ = seed.getZ();
		int maxZ = seed.getZ();
		int y = seed.getY();

		boolean expanded;
		do {
			expanded = false;
			if (maxX - minX + 1 < max && edgeAll(level, minX, maxX, minZ, maxZ, y, Direction.EAST, test)) {
				maxX++;
				expanded = true;
			}
			if (maxX - minX + 1 < max && edgeAll(level, minX, maxX, minZ, maxZ, y, Direction.WEST, test)) {
				minX--;
				expanded = true;
			}
			if (maxZ - minZ + 1 < max && edgeAll(level, minX, maxX, minZ, maxZ, y, Direction.SOUTH, test)) {
				maxZ++;
				expanded = true;
			}
			if (maxZ - minZ + 1 < max && edgeAll(level, minX, maxX, minZ, maxZ, y, Direction.NORTH, test)) {
				minZ--;
				expanded = true;
			}
		} while (expanded);

		if (maxX - minX + 1 < min || maxZ - minZ + 1 < min) {
			return null;
		}
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				if (!test.test(level, new BlockPos(x, y, z))) {
					return null;
				}
			}
		}
		return new Rect(minX, maxX, minZ, maxZ);
	}

	private static boolean edgeAll(
			LevelAccessor level,
			int minX,
			int maxX,
			int minZ,
			int maxZ,
			int y,
			Direction dir,
			CellTest test
	) {
		return switch (dir) {
			case EAST -> {
				int x = maxX + 1;
				for (int z = minZ; z <= maxZ; z++) {
					if (!test.test(level, new BlockPos(x, y, z))) {
						yield false;
					}
				}
				yield true;
			}
			case WEST -> {
				int x = minX - 1;
				for (int z = minZ; z <= maxZ; z++) {
					if (!test.test(level, new BlockPos(x, y, z))) {
						yield false;
					}
				}
				yield true;
			}
			case SOUTH -> {
				int z = maxZ + 1;
				for (int x = minX; x <= maxX; x++) {
					if (!test.test(level, new BlockPos(x, y, z))) {
						yield false;
					}
				}
				yield true;
			}
			case NORTH -> {
				int z = minZ - 1;
				for (int x = minX; x <= maxX; x++) {
					if (!test.test(level, new BlockPos(x, y, z))) {
						yield false;
					}
				}
				yield true;
			}
			default -> false;
		};
	}

	private static boolean isCoal(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos).is(ModTags.COAL_BLOCKS);
	}

	private static boolean isFrame(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos).is(ModTags.FORGE_FRAME);
	}

	private static List<BlockPos> dedupe(List<BlockPos> positions) {
		Set<BlockPos> set = new HashSet<>();
		List<BlockPos> out = new ArrayList<>();
		for (BlockPos pos : positions) {
			BlockPos immutable = pos.immutable();
			if (set.add(immutable)) {
				out.add(immutable);
			}
		}
		return out;
	}

	@FunctionalInterface
	private interface CellTest {
		boolean test(LevelAccessor level, BlockPos pos);
	}

	private record Rect(int minX, int maxX, int minZ, int maxZ) {
		int width() {
			return this.maxX - this.minX + 1;
		}

		int depth() {
			return this.maxZ - this.minZ + 1;
		}

		int area() {
			return width() * depth();
		}

		boolean contains(int x, int z) {
			return x >= this.minX && x <= this.maxX && z >= this.minZ && z <= this.maxZ;
		}

		List<BlockPos> cells(int y) {
			List<BlockPos> list = new ArrayList<>(area());
			for (int x = this.minX; x <= this.maxX; x++) {
				for (int z = this.minZ; z <= this.maxZ; z++) {
					list.add(new BlockPos(x, y, z));
				}
			}
			return list;
		}
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
			return Component.translatable("multiblock.from-stone-to-stars.rule.forge_layout");
		}

		@Override
		public Optional<List<ResolvedComponent>> resolveAll(LevelAccessor level, BlockPos controller) {
			return attempt(level, controller).layout().map(ForgeLayout::toResolvedComponents);
		}

		@Override
		public MultiRoleRule.Attempt attemptAll(LevelAccessor level, BlockPos controller) {
			ForgeLayout.Attempt result = attempt(level, controller);
			if (result.ok()) {
				return MultiRoleRule.Attempt.success(result.layout().orElseThrow().toResolvedComponents());
			}
			return MultiRoleRule.Attempt.fail(result.failure().orElse(description()));
		}
	}
}
