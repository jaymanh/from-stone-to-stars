package jaymanh.fsts.component;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModDataComponents {
	public static final DataComponentType<CrucibleCharge> CRUCIBLE_CHARGE = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			FromStoneToStars.id("crucible_charge"),
			DataComponentType.<CrucibleCharge>builder()
					.persistent(CrucibleCharge.CODEC)
					.networkSynchronized(CrucibleCharge.STREAM_CODEC)
					.build()
	);

	private ModDataComponents() {
	}

	public static void initialize() {
	}
}
