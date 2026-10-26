package jaymanh.fsts.recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.component.CrucibleCharge;
import jaymanh.fsts.item.CrucibleItem;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public final class ModRecipes {
	public static final RecipeType<GrindingRecipe> GRINDING = Registry.register(
			BuiltInRegistries.RECIPE_TYPE,
			FromStoneToStars.id("grinding"),
			new RecipeType<GrindingRecipe>() {
				@Override
				public String toString() {
					return FromStoneToStars.MOD_ID + ":grinding";
				}
			}
	);

	public static final RecipeSerializer<GrindingRecipe> GRINDING_SERIALIZER = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			FromStoneToStars.id("grinding"),
			new RecipeSerializer<>(
					SingleItemRecipe.simpleMapCodec(GrindingRecipe::new),
					SingleItemRecipe.simpleStreamCodec(GrindingRecipe::new)
			)
	);

	public static final RecipeBookCategory GRINDING_CATEGORY = Registry.register(
			BuiltInRegistries.RECIPE_BOOK_CATEGORY,
			FromStoneToStars.id("grinding"),
			new RecipeBookCategory()
	);

	public static final RecipeSerializer<CrucibleAlloyingRecipe> CRUCIBLE_ALLOYING_SERIALIZER = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			FromStoneToStars.id("crucible_alloying"),
			new RecipeSerializer<>(
					CrucibleAlloyingRecipe.MAP_CODEC,
					CrucibleAlloyingRecipe.STREAM_CODEC
			)
	);

	public static final RecipeBookCategory CRUCIBLE_ALLOYING_CATEGORY = Registry.register(
			BuiltInRegistries.RECIPE_BOOK_CATEGORY,
			FromStoneToStars.id("crucible_alloying"),
			new RecipeBookCategory()
	);

	public static final RecipeType<ForgingRecipe> FORGING = Registry.register(
			BuiltInRegistries.RECIPE_TYPE,
			FromStoneToStars.id("forging"),
			new RecipeType<ForgingRecipe>() {
				@Override
				public String toString() {
					return FromStoneToStars.MOD_ID + ":forging";
				}
			}
	);

	public static final RecipeSerializer<ForgingRecipe> FORGING_SERIALIZER = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			FromStoneToStars.id("forging"),
			new RecipeSerializer<>(
					ForgingRecipe.MAP_CODEC,
					ForgingRecipe.STREAM_CODEC
			)
	);

	public static final RecipeBookCategory FORGING_CATEGORY = Registry.register(
			BuiltInRegistries.RECIPE_BOOK_CATEGORY,
			FromStoneToStars.id("forging"),
			new RecipeBookCategory()
	);

	private ModRecipes() {
	}

	public static void initialize() {
	}

	public static Optional<RecipeHolder<GrindingRecipe>> findGrinding(Level level, ItemStack stack) {
		if (level == null || stack.isEmpty()) {
			return Optional.empty();
		}
		if (!(level.recipeAccess() instanceof RecipeManager recipes)) {
			return Optional.empty();
		}
		return recipes.getRecipeFor(GRINDING, new SingleRecipeInput(stack), level);
	}

	/**
	 * Every forging recipe for this metal, sorted by recipe id.
	 * Count is not a filter: the stump shows recipes the stack cannot afford yet.
	 * The server menu sends this order to the client. The client recipe list does not include forging.
	 */
	public static List<RecipeHolder<ForgingRecipe>> listForgingForMetal(Level level, ItemStack stack) {
		List<RecipeHolder<ForgingRecipe>> list = new ArrayList<>();
		if (level == null || stack.isEmpty() || !(level.recipeAccess() instanceof RecipeManager recipes)) {
			return list;
		}
		for (RecipeHolder<ForgingRecipe> holder : recipes.getAllOfType(FORGING)) {
			if (holder.value().matchesMetal(stack)) {
				list.add(holder);
			}
		}
		list.sort(Comparator.comparing(holder -> holder.id().identifier().toString()));
		return list;
	}

	public static boolean isForgingMetal(Level level, ItemStack stack) {
		if (level == null || stack.isEmpty() || !(level.recipeAccess() instanceof RecipeManager recipes)) {
			return false;
		}
		for (RecipeHolder<ForgingRecipe> holder : recipes.getAllOfType(FORGING)) {
			if (holder.value().matchesMetal(stack)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasAnyForgingRecipe(Level level) {
		if (level == null || !(level.recipeAccess() instanceof RecipeManager recipes)) {
			return false;
		}
		return !recipes.getAllOfType(FORGING).isEmpty();
	}

	public static Optional<RecipeHolder<ForgingRecipe>> getForging(Level level, Identifier id) {
		if (level == null || id == null || !(level.recipeAccess() instanceof RecipeManager recipes)) {
			return Optional.empty();
		}
		Optional<RecipeHolder<?>> found = recipes.byKey(ResourceKey.create(Registries.RECIPE, id));
		if (found.isPresent() && found.get().value() instanceof ForgingRecipe forging) {
			return Optional.of(new RecipeHolder<>(found.get().id(), forging));
		}
		return Optional.empty();
	}

	public static List<CrucibleAlloyingRecipe> listAlloying(Level level) {
		List<CrucibleAlloyingRecipe> list = new ArrayList<>();
		if (level == null || !(level.recipeAccess() instanceof RecipeManager recipes)) {
			return list;
		}
		for (RecipeHolder<CraftingRecipe> holder : recipes.getAllOfType(RecipeType.CRAFTING)) {
			if (holder.value() instanceof CrucibleAlloyingRecipe alloying) {
				list.add(alloying);
			}
		}
		return list;
	}

	public static Optional<CrucibleCharge> getCharge(ItemStack stack) {
		if (!CrucibleItem.isCharged(stack)) {
			return Optional.empty();
		}
		return Optional.ofNullable(CrucibleItem.getCharge(stack));
	}
}
