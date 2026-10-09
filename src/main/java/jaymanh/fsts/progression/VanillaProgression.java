package jaymanh.fsts.progression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jaymanh.fsts.FromStoneToStars;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
	/**
	 * Iron metal, plus gear that smelts into iron nuggets. Found without mining, these skip the rusty-mud path.
	 */
	public static final Set<Item> FREE_IRON = Set.of(
			Items.IRON_INGOT, Items.IRON_NUGGET, Items.IRON_BLOCK, Items.RAW_IRON, Items.RAW_IRON_BLOCK,
			Items.IRON_SWORD, Items.IRON_SPEAR, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_HOE,
			Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
			Items.IRON_HORSE_ARMOR, Items.IRON_NAUTILUS_ARMOR,
			Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS
	);
	public static final Set<Item> REMOVED_TOOL_DROPS = Set.of(
			Items.WOODEN_SWORD, Items.WOODEN_SHOVEL, Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SPEAR,
			Items.STONE_SWORD, Items.STONE_SHOVEL, Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SPEAR
	);

	private VanillaProgression() {
	}

	public static void initialize() {
		LootTableEvents.MODIFY_DROPS.register((holder, context, drops) -> {
			holder.unwrapKey().map(key -> key.identifier()).ifPresent(id -> {
				if (keepsIronLoot(id)) {
					return;
				}
				drops.removeIf(VanillaProgression::isFreeIron);
			});
		});
		FromStoneToStars.LOGGER.info("Vanilla gate: {} recipes removed", REMOVED_RECIPES.size());
	}

	/**
	 * Mined blocks still drop. End-city chests are past the early game and stay as they are.
	 */
	public static boolean keepsIronLoot(Identifier id) {
		if (!id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
			return true;
		}
		String path = id.getPath();
		return path.startsWith("blocks/") || path.equals("chests/end_city_treasure");
	}

	public static boolean isFreeIron(ItemStack stack) {
		return !stack.isEmpty() && FREE_IRON.contains(stack.getItem());
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
				"wooden_sword", "wooden_shovel", "wooden_pickaxe", "wooden_axe", "wooden_hoe", "wooden_spear",
				"stone_sword", "stone_shovel", "stone_pickaxe", "stone_axe", "stone_hoe", "stone_spear",
				"crafting_table", "furnace", "bucket", "flint_and_steel", "chest",
				"bamboo_planks", "bamboo_raft", "bamboo_chest_raft",
				"crimson_planks", "warped_planks", "bread", "bone_meal", "leather", "barrel",
				"copper_sword", "copper_shovel", "copper_pickaxe", "copper_axe", "copper_hoe", "copper_spear",
				"brick", "copper_chestplate", "copper_helmet", "copper_leggings", "copper_boots", "shield"
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
