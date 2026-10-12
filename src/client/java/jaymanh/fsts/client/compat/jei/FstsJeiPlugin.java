package jaymanh.fsts.client.compat.jei;

import java.util.List;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.entity.TanningRackBlockEntity;
import jaymanh.fsts.item.ModItems;
import jaymanh.fsts.multiblock.process.BrickDryingProcess;
import jaymanh.fsts.multiblock.process.CharcoalPileProcess;
import jaymanh.fsts.multiblock.process.ForgeProcess;
import jaymanh.fsts.tools.ModTools;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@JeiPlugin
public final class FstsJeiPlugin implements IModPlugin {
	public static final IRecipeType<SimpleProcessRecipe> TANNING = IRecipeType.create(FromStoneToStars.MOD_ID, "tanning", SimpleProcessRecipe.class);
	public static final IRecipeType<SimpleProcessRecipe> CHARCOAL_PILE = IRecipeType.create(FromStoneToStars.MOD_ID, "charcoal_pile", SimpleProcessRecipe.class);
	public static final IRecipeType<SimpleProcessRecipe> BRICK_DRYING = IRecipeType.create(FromStoneToStars.MOD_ID, "brick_drying", SimpleProcessRecipe.class);
	public static final IRecipeType<SimpleProcessRecipe> FORGE = IRecipeType.create(FromStoneToStars.MOD_ID, "forge", SimpleProcessRecipe.class);
	public static final IRecipeType<SimpleProcessRecipe> BONE_SAW = IRecipeType.create(FromStoneToStars.MOD_ID, "bone_saw", SimpleProcessRecipe.class);
	public static final IRecipeType<SimpleProcessRecipe> GRINDING = IRecipeType.create(FromStoneToStars.MOD_ID, "grinding", SimpleProcessRecipe.class);
	public static final IRecipeType<SimpleProcessRecipe> CRUCIBLE_ALLOYING = IRecipeType.create(FromStoneToStars.MOD_ID, "crucible_alloying", SimpleProcessRecipe.class);
	public static final IRecipeType<ForgingJeiRecipe> FORGING = IRecipeType.create(FromStoneToStars.MOD_ID, "forging", ForgingJeiRecipe.class);
	public static final IRecipeType<WorldInteractionRecipe> WORLD_INTERACTION = IRecipeType.create(FromStoneToStars.MOD_ID, "world_interaction", WorldInteractionRecipe.class);

	@Override
	public Identifier getPluginUid() {
		return FromStoneToStars.id("jei");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(
				new ProcessRecipeCategory(guiHelper, TANNING, Component.translatable("jei.from-stone-to-stars.tanning"), ModBlocks.TANNING_RACK),
				new ProcessRecipeCategory(guiHelper, CHARCOAL_PILE, Component.translatable("jei.from-stone-to-stars.charcoal_pile"), ModBlocks.KINDLING),
				new ProcessRecipeCategory(guiHelper, BRICK_DRYING, Component.translatable("jei.from-stone-to-stars.brick_drying"), ModBlocks.CLAY_BRICKS),
				new ProcessRecipeCategory(guiHelper, FORGE, Component.translatable("jei.from-stone-to-stars.forge"), ModBlocks.FORGE_HEARTH),
				new ProcessRecipeCategory(guiHelper, BONE_SAW, Component.translatable("jei.from-stone-to-stars.bone_saw"), ModTools.BONE_SAW),
				new ProcessRecipeCategory(guiHelper, GRINDING, Component.translatable("jei.from-stone-to-stars.grinding"), ModBlocks.MORTAR_AND_PESTLE),
				new ProcessRecipeCategory(guiHelper, CRUCIBLE_ALLOYING, Component.translatable("jei.from-stone-to-stars.crucible_alloying"), ModItems.CRUCIBLE),
				new ForgingRecipeCategory(guiHelper, FORGING, Component.translatable("jei.from-stone-to-stars.forging"), ModBlocks.FORGING_STUMP),
				new WorldInteractionCategory(guiHelper, WORLD_INTERACTION, Component.translatable("jei.from-stone-to-stars.world_interaction"), ModTools.FLINT_AXE)
		);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(TANNING, List.of(new SimpleProcessRecipe(
				FromStoneToStars.id("tanning/leather"),
				List.of(SimpleProcessRecipe.item(ModItems.RAW_LEATHER)),
				List.of(),
				SimpleProcessRecipe.item(Items.LEATHER),
				TanningRackBlockEntity.TANNING_TICKS
		)));

		registration.addRecipes(CHARCOAL_PILE, List.of(new SimpleProcessRecipe(
				FromStoneToStars.id("charcoal_pile/logs"),
				List.of(
						SimpleProcessRecipe.tag(ItemTags.LOGS),
						SimpleProcessRecipe.stack(new ItemStack(ModBlocks.KINDLING, 4)),
						SimpleProcessRecipe.any(ModTools.BOW_AND_DRILL, Items.FLINT_AND_STEEL, Items.FIRE_CHARGE)
				),
				List.of(),
				SimpleProcessRecipe.item(ModBlocks.CHARCOAL_BLOCK),
				CharcoalPileProcess.DURATION_TICKS
		)));

		registration.addRecipes(BRICK_DRYING, List.of(
				new SimpleProcessRecipe(
						FromStoneToStars.id("brick_drying/clay_bricks"),
						List.of(
								SimpleProcessRecipe.item(ModBlocks.CLAY_BRICKS),
								SimpleProcessRecipe.stack(new ItemStack(ModBlocks.KINDLING, 5)),
								SimpleProcessRecipe.any(ModBlocks.CHARCOAL_BLOCK, Items.COAL_BLOCK),
								SimpleProcessRecipe.any(ModTools.BOW_AND_DRILL, Items.FLINT_AND_STEEL, Items.FIRE_CHARGE)
						),
						List.of(),
						SimpleProcessRecipe.item(Items.BRICK),
						BrickDryingProcess.DURATION_TICKS
				),
				new SimpleProcessRecipe(
						FromStoneToStars.id("brick_drying/clay_jug"),
						List.of(
								SimpleProcessRecipe.item(ModBlocks.UNFIRED_CLAY_JUG),
								SimpleProcessRecipe.stack(new ItemStack(ModBlocks.KINDLING, 5)),
								SimpleProcessRecipe.any(ModBlocks.CHARCOAL_BLOCK, Items.COAL_BLOCK),
								SimpleProcessRecipe.any(ModTools.BOW_AND_DRILL, Items.FLINT_AND_STEEL, Items.FIRE_CHARGE)
						),
						List.of(),
						SimpleProcessRecipe.item(ModItems.CLAY_JUG),
						BrickDryingProcess.DURATION_TICKS
				),
				new SimpleProcessRecipe(
						FromStoneToStars.id("brick_drying/storage_pot"),
						List.of(
								SimpleProcessRecipe.item(ModBlocks.UNFIRED_STORAGE_POT),
								SimpleProcessRecipe.stack(new ItemStack(ModBlocks.KINDLING, 5)),
								SimpleProcessRecipe.any(ModBlocks.CHARCOAL_BLOCK, Items.COAL_BLOCK),
								SimpleProcessRecipe.any(ModTools.BOW_AND_DRILL, Items.FLINT_AND_STEEL, Items.FIRE_CHARGE)
						),
						List.of(),
						SimpleProcessRecipe.item(ModBlocks.STORAGE_POT),
						BrickDryingProcess.DURATION_TICKS
				)
		));

		registration.addRecipes(FORGE, List.of(new SimpleProcessRecipe(
				FromStoneToStars.id("forge/heat"),
				List.of(
						SimpleProcessRecipe.item(ModBlocks.FORGE_HEARTH),
						SimpleProcessRecipe.any(ModBlocks.CHARCOAL_BLOCK, Items.COAL_BLOCK),
						SimpleProcessRecipe.any(Items.BRICKS, Items.COBBLESTONE),
						SimpleProcessRecipe.item(ModBlocks.BELLOWS),
						SimpleProcessRecipe.any(ModTools.BOW_AND_DRILL, Items.FLINT_AND_STEEL, Items.FIRE_CHARGE)
				),
				List.of(),
				SimpleProcessRecipe.item(ModBlocks.FORGE_HEARTH),
				ForgeProcess.BURN_TICKS_FOR_MIN_COAL
		)));

		registration.addRecipes(GRINDING, List.of(
				new SimpleProcessRecipe(
						FromStoneToStars.id("grinding/flour"),
						List.of(SimpleProcessRecipe.item(Items.WHEAT)),
						List.of(),
						SimpleProcessRecipe.item(ModItems.FLOUR),
						0
				),
				new SimpleProcessRecipe(
						FromStoneToStars.id("grinding/bone_meal"),
						List.of(SimpleProcessRecipe.item(Items.BONE)),
						List.of(),
						SimpleProcessRecipe.stack(new ItemStack(Items.BONE_MEAL, 3)),
						0
				)
		));

		registration.addRecipes(CRUCIBLE_ALLOYING, List.of(
				new SimpleProcessRecipe(
						FromStoneToStars.id("alloying/bronze"),
						List.of(
								SimpleProcessRecipe.any(ModItems.CRUCIBLE, ModItems.BRONZE_REINFORCED_CRUCIBLE),
								SimpleProcessRecipe.stack(new ItemStack(Items.COPPER_INGOT, 4)),
								SimpleProcessRecipe.item(ModItems.ZINC_INGOT)
						),
						List.of(SimpleProcessRecipe.any(ModBlocks.BASIC_SMELTER, Items.BLAST_FURNACE, ModBlocks.FORGE_HEARTH)),
						SimpleProcessRecipe.stack(new ItemStack(ModItems.BRONZE_INGOT, 5)),
						200
				),
				new SimpleProcessRecipe(
						FromStoneToStars.id("alloying/iron_from_specks"),
						List.of(
								SimpleProcessRecipe.any(ModItems.CRUCIBLE, ModItems.BRONZE_REINFORCED_CRUCIBLE),
								SimpleProcessRecipe.stack(new ItemStack(ModItems.IRON_SPECKS, 6))
						),
						List.of(SimpleProcessRecipe.any(ModBlocks.BASIC_SMELTER, Items.BLAST_FURNACE, ModBlocks.FORGE_HEARTH)),
						SimpleProcessRecipe.stack(new ItemStack(Items.IRON_INGOT, 2)),
						200
				),
				new SimpleProcessRecipe(
						FromStoneToStars.id("alloying/steel"),
						List.of(
								SimpleProcessRecipe.any(ModItems.CRUCIBLE, ModItems.BRONZE_REINFORCED_CRUCIBLE),
								SimpleProcessRecipe.stack(new ItemStack(Items.IRON_INGOT, 6)),
								SimpleProcessRecipe.tag(ItemTags.COALS)
						),
						List.of(SimpleProcessRecipe.any(ModBlocks.BASIC_SMELTER, Items.BLAST_FURNACE, ModBlocks.FORGE_HEARTH)),
						SimpleProcessRecipe.stack(new ItemStack(ModItems.STEEL_INGOT, 2)),
						200
				)
		));

		registration.addRecipes(FORGING, List.of(
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/crushed_iron"), Items.RAW_IRON, 1, ModItems.CRUSHED_IRON, 2, 16),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/crushed_copper"), Items.RAW_COPPER, 1, ModItems.CRUSHED_COPPER, 2, 16),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/crushed_gold"), Items.RAW_GOLD, 1, ModItems.CRUSHED_GOLD, 2, 16),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/crushed_zinc"), ModItems.RAW_ZINC, 1, ModItems.CRUSHED_ZINC, 2, 16),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/bronze_nails"), ModItems.HEATED_BRONZE_INGOT, 1, ModItems.BRONZE_NAIL, 4, 4),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/bronze_shafts"), ModItems.HEATED_BRONZE_INGOT, 2, ModItems.BRONZE_SHAFT, 4, 4),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/bronze_tool_blades"), ModItems.HEATED_BRONZE_INGOT, 3, ModItems.BRONZE_TOOL_BLADE, 2, 4),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/bronze_plates"), ModItems.HEATED_BRONZE_INGOT, 4, ModItems.BRONZE_PLATE, 2, 4),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/copper_nails"), ModItems.HEATED_COPPER_INGOT, 1, ModItems.COPPER_NAIL, 4, 4),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/copper_shafts"), ModItems.HEATED_COPPER_INGOT, 2, ModItems.COPPER_SHAFT, 4, 4),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/iron_tool_heads"), ModItems.HEATED_IRON_INGOT, 3, ModItems.IRON_TOOL_HEAD, 2, 4),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/iron_plates"), ModItems.HEATED_IRON_INGOT, 4, ModItems.IRON_PLATE, 2, 4),
				ForgingJeiRecipe.of(FromStoneToStars.id("forging/iron_specks"), ModBlocks.DRIED_RUSTY_MUD, 1, ModItems.IRON_SPECKS, 1, 8)
		));

		registration.addRecipes(BONE_SAW, List.of(new SimpleProcessRecipe(
				FromStoneToStars.id("bone_saw/planks"),
				List.of(SimpleProcessRecipe.tag(ItemTags.LOGS)),
				List.of(SimpleProcessRecipe.item(ModTools.BONE_SAW)),
				SimpleProcessRecipe.tag(ItemTags.PLANKS),
				0 // Sawing is player-driven: one interaction per stroke, not a timed process.
		)));

		registration.addRecipes(WORLD_INTERACTION, List.of(
				new WorldInteractionRecipe(
						FromStoneToStars.id("world/bark"),
						SimpleProcessRecipe.tag(ItemTags.AXES),
						SimpleProcessRecipe.tag(ItemTags.LOGS),
						SimpleProcessRecipe.item(ModItems.BARK)
				),
				new WorldInteractionRecipe(
						FromStoneToStars.id("world/unfinished_work_stump"),
						SimpleProcessRecipe.tag(ItemTags.AXES),
						SimpleProcessRecipe.tag(ItemTags.LOGS),
						SimpleProcessRecipe.item(ModBlocks.UNFINISHED_WORK_STUMP)
				),
				new WorldInteractionRecipe(
						FromStoneToStars.id("world/work_stump"),
						SimpleProcessRecipe.item(ModItems.LEATHER_STRIPS),
						SimpleProcessRecipe.item(ModBlocks.UNFINISHED_WORK_STUMP),
						SimpleProcessRecipe.item(ModBlocks.WORK_STUMP)
				)
		));

		registration.addIngredientInfo(ModBlocks.TANNING_RACK, Component.translatable("jei.from-stone-to-stars.info.tanning_rack"));
		registration.addIngredientInfo(ModBlocks.KINDLING, Component.translatable("jei.from-stone-to-stars.info.kindling"));
		registration.addIngredientInfo(ModBlocks.CHARCOAL_BLOCK, Component.translatable("jei.from-stone-to-stars.info.charcoal_block"));
		registration.addIngredientInfo(ModTools.BONE_SAW, Component.translatable("jei.from-stone-to-stars.info.bone_saw"));
		registration.addIngredientInfo(ModItems.RAW_LEATHER, Component.translatable("jei.from-stone-to-stars.info.raw_leather"));
		registration.addIngredientInfo(ModBlocks.PEBBLE, Component.translatable("jei.from-stone-to-stars.info.pebble"));
		registration.addIngredientInfo(ModItems.BARK, Component.translatable("jei.from-stone-to-stars.info.bark"));
		registration.addIngredientInfo(ModTools.HAMMERSTONE, Component.translatable("jei.from-stone-to-stars.info.hammerstone"));
		registration.addIngredientInfo(ModTools.BRONZE_HAMMER, Component.translatable("jei.from-stone-to-stars.info.bronze_hammer"));
		registration.addIngredientInfo(ModItems.BRONZE_BUCKET, Component.translatable("jei.from-stone-to-stars.info.bronze_bucket"));
		registration.addIngredientInfo(ModTools.IRON_HAMMER, Component.translatable("jei.from-stone-to-stars.info.iron_hammer"));
		registration.addIngredientInfo(ModTools.IRON_KNIFE, Component.translatable("jei.from-stone-to-stars.info.iron_knife"));
		registration.addIngredientInfo(ModTools.BOW_AND_DRILL, Component.translatable("jei.from-stone-to-stars.info.bow_and_drill"));
		registration.addIngredientInfo(ModBlocks.CLAY_BRICKS, Component.translatable("jei.from-stone-to-stars.info.clay_bricks"));
		registration.addIngredientInfo(ModBlocks.UNFIRED_CLAY_JUG, Component.translatable("jei.from-stone-to-stars.info.unfired_clay_jug"));
		registration.addIngredientInfo(ModItems.CLAY_JUG, Component.translatable("jei.from-stone-to-stars.info.clay_jug"));
		registration.addIngredientInfo(ModBlocks.UNFIRED_STORAGE_POT, Component.translatable("jei.from-stone-to-stars.info.unfired_storage_pot"));
		registration.addIngredientInfo(ModBlocks.STORAGE_POT, Component.translatable("jei.from-stone-to-stars.info.storage_pot"));
		registration.addIngredientInfo(ModBlocks.BRONZE_CHEST, Component.translatable("jei.from-stone-to-stars.info.bronze_chest"));
		registration.addIngredientInfo(ModBlocks.FORGE_HEARTH, Component.translatable("jei.from-stone-to-stars.info.forge_hearth"));
		registration.addIngredientInfo(ModBlocks.BELLOWS, Component.translatable("jei.from-stone-to-stars.info.bellows"));
		registration.addIngredientInfo(Items.BRICK, Component.translatable("jei.from-stone-to-stars.info.brick"));
		registration.addIngredientInfo(ModBlocks.BASIC_SMELTER, Component.translatable("jei.from-stone-to-stars.info.basic_smelter"));
		registration.addIngredientInfo(ModItems.CRUCIBLE, Component.translatable("jei.from-stone-to-stars.info.crucible"));
		registration.addIngredientInfo(ModItems.BRONZE_REINFORCED_CRUCIBLE, Component.translatable("jei.from-stone-to-stars.info.bronze_reinforced_crucible"));
		registration.addIngredientInfo(ModBlocks.MORTAR_AND_PESTLE, Component.translatable("jei.from-stone-to-stars.info.mortar_and_pestle"));
		registration.addIngredientInfo(ModItems.FLOUR, Component.translatable("jei.from-stone-to-stars.info.flour"));
		registration.addIngredientInfo(ModBlocks.SNARE, Component.translatable("jei.from-stone-to-stars.info.snare"));
		registration.addIngredientInfo(ModBlocks.FISH_TRAP, Component.translatable("jei.from-stone-to-stars.info.fish_trap"));
		registration.addIngredientInfo(ModBlocks.HIDE_BED, Component.translatable("jei.from-stone-to-stars.info.hide_bed"));
		registration.addIngredientInfo(ModBlocks.UNFINISHED_WORK_STUMP, Component.translatable("jei.from-stone-to-stars.info.unfinished_work_stump"));
		registration.addIngredientInfo(ModBlocks.WORK_STUMP, Component.translatable("jei.from-stone-to-stars.info.work_stump"));
		registration.addIngredientInfo(ModBlocks.FORGING_STUMP, Component.translatable("jei.from-stone-to-stars.info.forging_stump"));
		registration.addIngredientInfo(ModItems.CRUSHED_IRON, Component.translatable("jei.from-stone-to-stars.info.crushed_iron"));
		registration.addIngredientInfo(ModItems.CRUSHED_COPPER, Component.translatable("jei.from-stone-to-stars.info.crushed_copper"));
		registration.addIngredientInfo(ModItems.CRUSHED_GOLD, Component.translatable("jei.from-stone-to-stars.info.crushed_gold"));
		registration.addIngredientInfo(ModItems.CRUSHED_ZINC, Component.translatable("jei.from-stone-to-stars.info.crushed_zinc"));
		registration.addIngredientInfo(ModBlocks.TRIP_HAMMER, Component.translatable("jei.from-stone-to-stars.info.trip_hammer"));
		registration.addIngredientInfo(ModBlocks.POWERED_QUERN, Component.translatable("jei.from-stone-to-stars.info.powered_quern"));
		registration.addIngredientInfo(ModBlocks.BRONZE_HOPPER, Component.translatable("jei.from-stone-to-stars.info.bronze_hopper"));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(TANNING, ModBlocks.TANNING_RACK);
		registration.addCraftingStation(CHARCOAL_PILE, ModBlocks.KINDLING);
		registration.addCraftingStation(CHARCOAL_PILE, ModTools.BOW_AND_DRILL);
		registration.addCraftingStation(BRICK_DRYING, ModBlocks.KINDLING);
		registration.addCraftingStation(BRICK_DRYING, ModBlocks.CLAY_BRICKS);
		registration.addCraftingStation(BRICK_DRYING, ModBlocks.UNFIRED_CLAY_JUG);
		registration.addCraftingStation(BRICK_DRYING, ModBlocks.UNFIRED_STORAGE_POT);
		registration.addCraftingStation(BRICK_DRYING, ModBlocks.CHARCOAL_BLOCK);
		registration.addCraftingStation(BRICK_DRYING, Items.COAL_BLOCK);
		registration.addCraftingStation(BRICK_DRYING, ModTools.BOW_AND_DRILL);
		registration.addCraftingStation(FORGE, ModBlocks.FORGE_HEARTH);
		registration.addCraftingStation(FORGE, ModBlocks.BELLOWS);
		registration.addCraftingStation(FORGE, ModTools.BOW_AND_DRILL);
		registration.addCraftingStation(BONE_SAW, ModTools.BONE_SAW);
		registration.addCraftingStation(GRINDING, ModBlocks.MORTAR_AND_PESTLE);
		registration.addCraftingStation(CRUCIBLE_ALLOYING, ModItems.CRUCIBLE);
		registration.addCraftingStation(CRUCIBLE_ALLOYING, ModItems.BRONZE_REINFORCED_CRUCIBLE);
		registration.addCraftingStation(CRUCIBLE_ALLOYING, ModBlocks.BASIC_SMELTER);
		registration.addCraftingStation(CRUCIBLE_ALLOYING, Items.BLAST_FURNACE);
		registration.addCraftingStation(CRUCIBLE_ALLOYING, ModBlocks.FORGE_HEARTH);
		registration.addCraftingStation(FORGING, ModBlocks.FORGING_STUMP);
		registration.addCraftingStation(FORGING, ModBlocks.TRIP_HAMMER);
		registration.addCraftingStation(FORGING, ModTools.BRONZE_HAMMER);
		registration.addCraftingStation(FORGING, ModTools.IRON_HAMMER);
		registration.addCraftingStation(WORLD_INTERACTION, ModTools.FLINT_AXE);
		registration.addCraftingStation(WORLD_INTERACTION, ModTools.BONE_AXE);
	}
}
