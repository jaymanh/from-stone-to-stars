package jaymanh.fsts.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class MinYPlacement extends PlacementFilter {
	public static final MapCodec<MinYPlacement> CODEC = Codec.INT
			.fieldOf("min_inclusive")
			.xmap(MinYPlacement::new, MinYPlacement::minInclusive);
	public static final PlacementModifierType<MinYPlacement> TYPE = Registry.register(
			BuiltInRegistries.PLACEMENT_MODIFIER_TYPE,
			FromStoneToStars.id("min_y"),
			() -> CODEC
	);

	private final int minInclusive;

	public MinYPlacement(int minInclusive) {
		this.minInclusive = minInclusive;
	}

	public int minInclusive() {
		return minInclusive;
	}

	@Override
	protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
		return pos.getY() >= minInclusive;
	}

	@Override
	public PlacementModifierType<?> type() {
		return TYPE;
	}

	public static void initialize() {
	}
}
