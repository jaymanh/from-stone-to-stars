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
	public static final BlockItemId ROPE = create("rope");
	public static final BlockItemId BASIC_SMELTER = create("basic_smelter");
	public static final BlockItemId MORTAR_AND_PESTLE = create("mortar_and_pestle");
	public static final BlockItemId SNARE = create("snare");
	public static final	BlockItemId FISH_TRAP = create("fish_trap");
	public static final BlockItemId ZINC_ORE = create("zinc_ore");
	public static final BlockItemId DEEPSLATE_ZINC_ORE = create("deepslate_zinc_ore");
	public static final BlockItemId HIDE_BED = create("hide_bed");
	public static final BlockItemId THATCH = create("thatch");
	public static final BlockItemId WATTLE_AND_DAUB = create("wattle_and_daub");
	public static final BlockItemId WORK_STUMP = create("work_stump");
	public static final BlockItemId UNFINISHED_WORK_STUMP = create("unfinished_work_stump");
	public static final BlockItemId UNFIRED_CLAY_JUG = create("unfired_clay_jug");
	public static final BlockItemId UNFIRED_STORAGE_POT = create("unfired_storage_pot");
	public static final BlockItemId STORAGE_POT = create("storage_pot");
	public static final BlockItemId FORGE_HEARTH = create("forge_hearth");
	public static final BlockItemId BELLOWS = create("bellows");
	public static final BlockItemId FORGING_STUMP = create("forging_stump");
	public static final BlockItemId WATER_WHEEL = create("water_wheel");
	public static final BlockItemId WOODEN_SHAFT = create("wooden_shaft");
	public static final BlockItemId ROTATION_GEAR = create("rotation_gear");
	public static final BlockItemId POWERED_QUERN = create("powered_quern");

	private ModBlockItemIds() {
	}

	public static BlockItemId create(String name) {
		Identifier id = FromStoneToStars.id(name);
		return BlockItemId.create(id, id);
	}
}
