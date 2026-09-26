package jaymanh.fsts.component;

import com.mojang.serialization.Codec;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;

public final class ModDataComponents {
	public static final DataComponentType<CrucibleCharge> CRUCIBLE_CHARGE = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			FromStoneToStars.id("crucible_charge"),
			DataComponentType.<CrucibleCharge>builder()
					.persistent(CrucibleCharge.CODEC)
					.networkSynchronized(CrucibleCharge.STREAM_CODEC)
					.build()
	);

	/** Game time when a heated metal finishes cooling and reverts to its cold ingot. */
	public static final DataComponentType<Long> HEAT_UNTIL = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			FromStoneToStars.id("heat_until"),
			DataComponentType.<Long>builder()
					.persistent(Codec.LONG)
					.networkSynchronized(ByteBufCodecs.VAR_LONG)
					.build()
	);

	private ModDataComponents() {
	}

	public static void initialize() {
	}
}
