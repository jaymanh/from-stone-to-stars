package jaymanh.fsts.block;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public final class ModBlockItemIds {
	public static final BlockItemId TEST_BLOCK = create("test_block");

	private ModBlockItemIds() {
	}

	public static BlockItemId create(String name) {
		Identifier id = FromStoneToStars.id(name);
		return BlockItemId.create(id, id);
	}
}
