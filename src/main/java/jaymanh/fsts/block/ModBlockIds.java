package jaymanh.fsts.block;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public final class ModBlockIds {
	public static final ResourceKey<Block> PLACED_BONE_SAW = create("placed_bone_saw");

	private ModBlockIds() {
	}

	public static ResourceKey<Block> create(String name) {
		return ResourceKey.create(Registries.BLOCK, FromStoneToStars.id(name));
	}
}
