package jaymanh.fsts.multiblock;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;

/**
 * Semantic role of a matched part. Used by processes and (later) machines
 * to find igniters, inputs, frames, and controllers without hardcoding offsets.
 */
public enum MultiblockRole implements StringRepresentable {
	/** Pattern origin / ignition point (e.g. kindling). */
	ORIGIN,
	/** Material converted by the process (e.g. logs). */
	INPUT,
	/** Material consumed by the process (e.g. planks). */
	CONSUMED,
	/** Structural blocks required to form but not transformed. */
	FRAME,
	/** Dedicated machine brain for larger factories. */
	CONTROLLER,
	/** Where finished products leave the structure (e.g. sawmill outfeed). */
	OUTPUT;

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
