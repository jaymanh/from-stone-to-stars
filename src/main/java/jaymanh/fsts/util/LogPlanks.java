package jaymanh.fsts.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class LogPlanks {
	private LogPlanks() {
	}

	public static ItemStack planksForLog(BlockState logState, int count) {
		Item planks = planksItemForLog(logState.getBlock());
		if (planks == Items.AIR) {
			return ItemStack.EMPTY;
		}
		return new ItemStack(planks, count);
	}

	public static Item planksItemForLog(Block log) {
		Identifier id = BuiltInRegistries.BLOCK.getKey(log);
		if (id == null) {
			return Items.AIR;
		}

		String path = id.getPath();
		if (path.startsWith("stripped_")) {
			path = path.substring("stripped_".length());
		}

		String plankPath;
		if (path.endsWith("_log")) {
			plankPath = path.substring(0, path.length() - "_log".length()) + "_planks";
		} else if (path.endsWith("_wood")) {
			plankPath = path.substring(0, path.length() - "_wood".length()) + "_planks";
		} else if (path.endsWith("_stem")) {
			plankPath = path.substring(0, path.length() - "_stem".length()) + "_planks";
		} else if (path.endsWith("_hyphae")) {
			plankPath = path.substring(0, path.length() - "_hyphae".length()) + "_planks";
		} else if (path.equals("bamboo_block")) {
			plankPath = "bamboo_planks";
		} else {
			return Items.AIR;
		}

		Identifier plankId = Identifier.fromNamespaceAndPath(id.getNamespace(), plankPath);
		return BuiltInRegistries.ITEM.getOptional(plankId).orElse(Items.AIR);
	}
}
