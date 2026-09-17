package jaymanh.fsts.block;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public final class ModBlockItemIds {
	public static final BlockItemId PEBBLE = create("pebble");
	public static final BlockItemId STICKS = create("sticks");
	public static final BlockItemId TANNING_RACK = create("tanning_rack");
	public static final BlockItemId KINDLING = create("kindling");

	private ModBlockItemIds() {
	}

	public static BlockItemId create(String name) {
		Identifier id = FromStoneToStars.id(name);
		return BlockItemId.create(id, id);
	}
}
