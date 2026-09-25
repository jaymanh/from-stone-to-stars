package jaymanh.fsts.client.compat.jei;

import java.util.List;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.entity.TanningRackBlockEntity;
import jaymanh.fsts.item.ModItems;
import jaymanh.fsts.multiblock.process.BrickDryingProcess;
import jaymanh.fsts.multiblock.process.CharcoalPileProcess;
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
	public static final IRecipeType<SimpleProcessRecipe> BONE_SAW = IRecipeType.create(FromStoneToStars.MOD_ID, "bone_saw", SimpleProcessRecipe.class);
	public static final IRecipeType<SimpleProcessRecipe> GRINDING = IRecipeType.create(FromStoneToStars.MOD_ID, "grinding", SimpleProcessRecipe.class);
	public static final IRecipeType<SimpleProcessRecipe> CRUCIBLE_ALLOYING = IRecipeType.create(FromStoneToStars.MOD_ID, "crucible_alloying", SimpleProcessRecipe.class);
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
				new ProcessRecipeCategory(guiHelper, BONE_SAW, Component.translatable("jei.from-stone-to-stars.bone_saw"), ModTools.BONE_SAW),
				new ProcessRecipeCategory(guiHelper, GRINDING, Component.translatable("jei.from-stone-to-stars.grinding"), ModBlocks.MORTAR_AND_PESTLE),
				new ProcessRecipeCategory(guiHelper, CRUCIBLE_ALLOYING, Component.translatable("jei.from-stone-to-stars.crucible_alloying"), ModItems.CRUCIBLE),
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
								SimpleProcessRecipe.item(ModItems.CRUCIBLE),
								SimpleProcessRecipe.stack(new ItemStack(Items.COPPER_INGOT, 4)),
								SimpleProcessRecipe.item(ModItems.ZINC_INGOT)
						),
						List.of(SimpleProcessRecipe.item(ModBlocks.BASIC_SMELTER)),
						SimpleProcessRecipe.stack(new ItemStack(ModItems.BRONZE_INGOT, 5)),
						200
				)
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
		registration.addIngredientInfo(ModTools.BOW_AND_DRILL, Component.translatable("jei.from-stone-to-stars.info.bow_and_drill"));
		registration.addIngredientInfo(ModBlocks.CLAY_BRICKS, Component.translatable("jei.from-stone-to-stars.info.clay_bricks"));
		registration.addIngredientInfo(ModBlocks.UNFIRED_CLAY_JUG, Component.translatable("jei.from-stone-to-stars.info.unfired_clay_jug"));
		registration.addIngredientInfo(ModItems.CLAY_JUG, Component.translatable("jei.from-stone-to-stars.info.clay_jug"));
		registration.addIngredientInfo(ModBlocks.UNFIRED_STORAGE_POT, Component.translatable("jei.from-stone-to-stars.info.unfired_storage_pot"));
		registration.addIngredientInfo(ModBlocks.STORAGE_POT, Component.translatable("jei.from-stone-to-stars.info.storage_pot"));
		registration.addIngredientInfo(Items.BRICK, Component.translatable("jei.from-stone-to-stars.info.brick"));
		registration.addIngredientInfo(ModBlocks.BASIC_SMELTER, Component.translatable("jei.from-stone-to-stars.info.basic_smelter"));
		registration.addIngredientInfo(ModItems.CRUCIBLE, Component.translatable("jei.from-stone-to-stars.info.crucible"));
		registration.addIngredientInfo(ModBlocks.MORTAR_AND_PESTLE, Component.translatable("jei.from-stone-to-stars.info.mortar_and_pestle"));
		registration.addIngredientInfo(ModItems.FLOUR, Component.translatable("jei.from-stone-to-stars.info.flour"));
		registration.addIngredientInfo(ModBlocks.SNARE, Component.translatable("jei.from-stone-to-stars.info.snare"));
		registration.addIngredientInfo(ModBlocks.FISH_TRAP, Component.translatable("jei.from-stone-to-stars.info.fish_trap"));
		registration.addIngredientInfo(ModBlocks.HIDE_BED, Component.translatable("jei.from-stone-to-stars.info.hide_bed"));
		registration.addIngredientInfo(ModBlocks.UNFINISHED_WORK_STUMP, Component.translatable("jei.from-stone-to-stars.info.unfinished_work_stump"));
		registration.addIngredientInfo(ModBlocks.WORK_STUMP, Component.translatable("jei.from-stone-to-stars.info.work_stump"));
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
		registration.addCraftingStation(BONE_SAW, ModTools.BONE_SAW);
		registration.addCraftingStation(GRINDING, ModBlocks.MORTAR_AND_PESTLE);
		registration.addCraftingStation(CRUCIBLE_ALLOYING, ModItems.CRUCIBLE);
		registration.addCraftingStation(CRUCIBLE_ALLOYING, ModBlocks.BASIC_SMELTER);
		registration.addCraftingStation(WORLD_INTERACTION, ModTools.FLINT_AXE);
		registration.addCraftingStation(WORLD_INTERACTION, ModTools.BONE_AXE);
	}
}
