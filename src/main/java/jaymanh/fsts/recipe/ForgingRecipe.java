package jaymanh.fsts.recipe;

import java.util.List;

import com.mojang.datafixers.Products;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import jaymanh.fsts.block.ModBlocks;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * Hammer heated metal on a forging stump.
 * Input stack count must exactly equal {@link #ingredientCount()}.
 */
public class ForgingRecipe implements Recipe<SingleRecipeInput> {
	public static final MapCodec<ForgingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> {
		Products.P5<RecordCodecBuilder.Mu<ForgingRecipe>, Recipe.CommonInfo, Ingredient, Integer, Integer, ItemStackTemplate> fields = instance.group(
				Recipe.CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
				Ingredient.CODEC.fieldOf("ingredient").forGetter(ForgingRecipe::ingredient),
				ExtraCodecs.POSITIVE_INT.optionalFieldOf("ingredient_count", 1).forGetter(ForgingRecipe::ingredientCount),
				ExtraCodecs.POSITIVE_INT.optionalFieldOf("hits", 4).forGetter(ForgingRecipe::hits),
				ItemStackTemplate.CODEC.fieldOf("result").forGetter(ForgingRecipe::result)
		);
		return fields.apply(instance, ForgingRecipe::new);
	});

	public static final StreamCodec<RegistryFriendlyByteBuf, ForgingRecipe> STREAM_CODEC = StreamCodec.composite(
			Recipe.CommonInfo.STREAM_CODEC,
			recipe -> recipe.commonInfo,
			Ingredient.CONTENTS_STREAM_CODEC,
			ForgingRecipe::ingredient,
			ByteBufCodecs.VAR_INT,
			ForgingRecipe::ingredientCount,
			ByteBufCodecs.VAR_INT,
			ForgingRecipe::hits,
			ItemStackTemplate.STREAM_CODEC,
			ForgingRecipe::result,
			ForgingRecipe::new
	);

	private final Recipe.CommonInfo commonInfo;
	private final Ingredient ingredient;
	private final int ingredientCount;
	private final int hits;
	private final ItemStackTemplate result;
	private @Nullable PlacementInfo placementInfo;

	public ForgingRecipe(
			Recipe.CommonInfo commonInfo,
			Ingredient ingredient,
			int ingredientCount,
			int hits,
			ItemStackTemplate result
	) {
		this.commonInfo = commonInfo;
		this.ingredient = ingredient;
		this.ingredientCount = Math.max(1, ingredientCount);
		this.hits = Math.max(1, hits);
		this.result = result;
	}

	public Ingredient ingredient() {
		return this.ingredient;
	}

	public int ingredientCount() {
		return this.ingredientCount;
	}

	public int hits() {
		return this.hits;
	}

	public ItemStackTemplate result() {
		return this.result;
	}

	public ItemStack resultStack() {
		return this.result.create();
	}

	@Override
	public boolean matches(SingleRecipeInput input, Level level) {
		ItemStack stack = input.item();
		return !stack.isEmpty()
				&& this.ingredient.test(stack)
				&& stack.getCount() == this.ingredientCount;
	}

	@Override
	public ItemStack assemble(SingleRecipeInput input) {
		return this.result.create();
	}

	@Override
	public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
		return ModRecipes.FORGING_SERIALIZER;
	}

	@Override
	public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
		return ModRecipes.FORGING;
	}

	@Override
	public PlacementInfo placementInfo() {
		if (this.placementInfo == null) {
			this.placementInfo = PlacementInfo.create(this.ingredient);
		}
		return this.placementInfo;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return ModRecipes.FORGING_CATEGORY;
	}

	@Override
	public boolean showNotification() {
		return this.commonInfo.showNotification();
	}

	@Override
	public String group() {
		return "";
	}

	@Override
	public List<RecipeDisplay> display() {
		return List.of(new StonecutterRecipeDisplay(
				this.ingredient.display(),
				new SlotDisplay.ItemStackSlotDisplay(this.result),
				new SlotDisplay.ItemSlotDisplay(ModBlocks.FORGING_STUMP.asItem())
		));
	}
}
