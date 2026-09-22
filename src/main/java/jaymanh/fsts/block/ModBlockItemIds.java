package jaymanh.fsts.block;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public final class ModBlockItemIds {
	public static final BlockItemId PEBBLE = create("pebble");
	public static final BlockItemId STICKS = create("sticks");
	public static final BlockItemId TANNING_RACK = create("tanning_rack");
	public static final BlockItemId KINDLING = create("kindling");
	public static final BlockItemId CHARCOAL_BLOCK = create("charcoal_block");
	public static final BlockItemId CLAY_BRICKS = create("clay_bricks");
	public static final BlockItemId FIRED_CLAY_BRICKS = create("fired_clay_bricks");
	public static final BlockItemId ROPE = create("rope");
	public static final BlockItemId BASIC_SMELTER = create("basic_smelter");
	public static final BlockItemId MORTAR_AND_PESTLE = create("mortar_and_pestle");
	public static final BlockItemId SNARE = create("snare");
	public static final	BlockItemId FISH_TRAP = create("fish_trap");
	public static final BlockItemId TIN_ORE = create("tin_ore");
	public static final BlockItemId DEEPSLATE_TIN_ORE = create("deepslate_tin_ore");

	private ModBlockItemIds() {
	}

	public static BlockItemId create(String name) {
		Identifier id = FromStoneToStars.id(name);
		return BlockItemId.create(id, id);
	}
}
