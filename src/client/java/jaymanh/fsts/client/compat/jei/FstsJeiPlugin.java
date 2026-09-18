package jaymanh.fsts.client.compat.jei;

import java.util.List;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.entity.PlacedBoneSawBlockEntity;
import jaymanh.fsts.block.entity.TanningRackBlockEntity;
import jaymanh.fsts.item.ModItems;
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
	public static final IRecipeType<SimpleProcessRecipe> BONE_SAW = IRecipeType.create(FromStoneToStars.MOD_ID, "bone_saw", SimpleProcessRecipe.class);
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
				new ProcessRecipeCategory(guiHelper, BONE_SAW, Component.translatable("jei.from-stone-to-stars.bone_saw"), ModTools.BONE_SAW),
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
						SimpleProcessRecipe.stack(new ItemStack(ModBlocks.KINDLING, 3)),
						SimpleProcessRecipe.any(Items.FLINT_AND_STEEL, Items.FIRE_CHARGE)
				),
				List.of(),
				SimpleProcessRecipe.item(ModBlocks.CHARCOAL_BLOCK),
				CharcoalPileProcess.DURATION_TICKS
		)));

		registration.addRecipes(BONE_SAW, List.of(new SimpleProcessRecipe(
				FromStoneToStars.id("bone_saw/planks"),
				List.of(SimpleProcessRecipe.tag(ItemTags.LOGS)),
				List.of(SimpleProcessRecipe.item(ModTools.BONE_SAW)),
				SimpleProcessRecipe.tag(ItemTags.PLANKS),
				PlacedBoneSawBlockEntity.STROKE_COUNT * (PlacedBoneSawBlockEntity.COOLDOWN_TICKS + 1)
		)));

		registration.addRecipes(WORLD_INTERACTION, List.of(
				new WorldInteractionRecipe(
						FromStoneToStars.id("world/bark"),
						SimpleProcessRecipe.tag(ItemTags.AXES),
						SimpleProcessRecipe.tag(ItemTags.LOGS),
						SimpleProcessRecipe.item(ModItems.BARK)
				),
				new WorldInteractionRecipe(
						FromStoneToStars.id("world/crafting_table"),
						SimpleProcessRecipe.item(Items.LEATHER),
						SimpleProcessRecipe.tag(ItemTags.LOGS),
						SimpleProcessRecipe.item(Items.CRAFTING_TABLE)
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
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(TANNING, ModBlocks.TANNING_RACK);
		registration.addCraftingStation(CHARCOAL_PILE, ModBlocks.KINDLING);
		registration.addCraftingStation(BONE_SAW, ModTools.BONE_SAW);
		registration.addCraftingStation(WORLD_INTERACTION, ModTools.FLINT_AXE);
		registration.addCraftingStation(WORLD_INTERACTION, ModTools.BONE_AXE);
	}
}
