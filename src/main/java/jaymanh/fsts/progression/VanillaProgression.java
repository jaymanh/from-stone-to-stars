package jaymanh.fsts.progression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class VanillaProgression {
	private static final List<String> WOODS = List.of(
			"oak", "spruce", "birch", "jungle", "acacia", "dark_oak",
			"mangrove", "cherry", "pale_oak"
	);
	private static final List<String> BED_COLORS = List.of(
			"white", "light_gray", "gray", "black", "brown", "red", "orange", "yellow",
			"lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink"
	);

	public static final Set<Identifier> REMOVED_RECIPES = buildRemovedRecipes();
	public static final Set<Item> REMOVED_TOOL_DROPS = Set.of(
			Items.WOODEN_SWORD, Items.WOODEN_SHOVEL, Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SPEAR,
			Items.STONE_SWORD, Items.STONE_SHOVEL, Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SPEAR
	);

	private VanillaProgression() {
	}

	public static void initialize() {
		FromStoneToStars.LOGGER.info("Age 0 vanilla gate: {} recipes removed", REMOVED_RECIPES.size());
	}

	public static boolean isRemovedRecipe(Identifier id) {
		return REMOVED_RECIPES.contains(id);
	}

	public static boolean isRemovedToolDrop(Item item) {
		return REMOVED_TOOL_DROPS.contains(item);
	}

	private static Set<Identifier> buildRemovedRecipes() {
		Set<Identifier> ids = new HashSet<>();
		add(ids,
				"wooden_sword", "wooden_shovel", "wooden_pickaxe", "wooden_axe", "wooden_hoe",
				"stone_sword", "stone_shovel", "stone_pickaxe", "stone_axe", "stone_hoe", "stone_spear",
				"crafting_table", "furnace", "bucket", "flint_and_steel", "chest",
				"bamboo_planks", "bamboo_raft", "bamboo_chest_raft",
				"crimson_planks", "warped_planks", "bread", "bone_meal", "leather", "barrel",
				"copper_sword", "copper_shovel", "copper_pickaxe", "copper_axe", "copper_hoe", "copper_spear"
		);
		for (String wood : WOODS) {
			add(ids, wood + "_planks", wood + "_boat", wood + "_chest_boat");
		}
		for (String color : BED_COLORS) {
			add(ids, color + "_bed", "dye_" + color + "_bed");
		}
		return Set.copyOf(ids);
	}

	private static void add(Set<Identifier> ids, String... paths) {
		for (String path : paths) {
			ids.add(Identifier.withDefaultNamespace(path));
		}
	}
}
