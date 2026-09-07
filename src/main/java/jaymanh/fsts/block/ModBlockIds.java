package jaymanh.fsts.block;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public final class ModBlockIds {
	private ModBlockIds() {
	}

	public static ResourceKey<Block> create(String name) {
		return ResourceKey.create(Registries.BLOCK, FromStoneToStars.id(name));
	}
}
