package jaymanh.fsts.recipe;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.component.CrucibleCharge;
import jaymanh.fsts.item.CrucibleItem;
import jaymanh.fsts.item.ModItems;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
import net.minecraft.world.level.Level;

/**
 * Data-driven crucible alloy. Crafting packs an empty crucible with the metals;
 * the basic smelter fires the charged crucible using the embedded charge data.
 */
public class CrucibleAlloyingRecipe implements CraftingRecipe {
	public static final MapCodec<CrucibleAlloyingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountedIngredient.CODEC.listOf().fieldOf("ingredients").forGetter(CrucibleAlloyingRecipe::ingredients),
			ItemStackTemplate.MAP_CODEC.fieldOf("result").forGetter(CrucibleAlloyingRecipe::result),
			Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(CrucibleAlloyingRecipe::experience),
			ExtraCodecs.POSITIVE_INT.optionalFieldOf("cookingtime", 200).forGetter(CrucibleAlloyingRecipe::cookingTime)
	).apply(instance, CrucibleAlloyingRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CrucibleAlloyingRecipe> STREAM_CODEC = StreamCodec.composite(
			CountedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
			CrucibleAlloyingRecipe::ingredients,
			ItemStackTemplate.STREAM_CODEC,
			CrucibleAlloyingRecipe::result,
			ByteBufCodecs.FLOAT,
			CrucibleAlloyingRecipe::experience,
			ByteBufCodecs.VAR_INT,
			CrucibleAlloyingRecipe::cookingTime,
			CrucibleAlloyingRecipe::new
	);

	private final List<CountedIngredient> ingredients;
	private final ItemStackTemplate result;
	private final float experience;
	private final int cookingTime;
	private PlacementInfo placementInfo;

	public CrucibleAlloyingRecipe(List<CountedIngredient> ingredients, ItemStackTemplate result, float experience, int cookingTime) {
		this.ingredients = List.copyOf(ingredients);
		this.result = result;
		this.experience = experience;
		this.cookingTime = cookingTime;
	}

	public List<CountedIngredient> ingredients() {
		return this.ingredients;
	}

	public ItemStackTemplate result() {
		return this.result;
	}

	public float experience() {
		return this.experience;
	}

	public int cookingTime() {
		return this.cookingTime;
	}

	public CrucibleCharge createCharge() {
		return new CrucibleCharge(this.result, this.cookingTime, this.experience);
	}

	private int metalSlotCount() {
		int total = 0;
		for (CountedIngredient ingredient : this.ingredients) {
			total += ingredient.count();
		}
		return total;
	}

	public boolean matchesMetals(List<ItemStack> stacks) {
		if (stacks.size() != this.metalSlotCount()) {
			return false;
		}

		List<ItemStack> remaining = new ArrayList<>(stacks);
		for (CountedIngredient requirement : this.ingredients) {
			int needed = requirement.count();
			for (int i = 0; i < remaining.size() && needed > 0; i++) {
				ItemStack stack = remaining.get(i);
				if (!stack.isEmpty() && requirement.test(stack)) {
					remaining.set(i, ItemStack.EMPTY);
					needed--;
				}
			}
			if (needed > 0) {
				return false;
			}
		}

		for (ItemStack stack : remaining) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack crucible = ItemStack.EMPTY;
		List<ItemStack> metals = new ArrayList<>();

		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) {
				continue;
			}
			if (stack.is(ModItems.CRUCIBLE)) {
				if (!CrucibleItem.isEmpty(stack) || !crucible.isEmpty()) {
					return false;
				}
				crucible = stack;
				continue;
			}
			metals.add(stack);
		}

		return !crucible.isEmpty() && this.matchesMetals(metals);
	}

	@Override
	public ItemStack assemble(CraftingInput input) {
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (CrucibleItem.isEmpty(stack)) {
				return CrucibleItem.createCharged(stack, this.createCharge());
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean showNotification() {
		return true;
	}

	@Override
	public String group() {
		return "crucible_alloying";
	}

	@Override
	public CraftingBookCategory category() {
		return CraftingBookCategory.MISC;
	}

	@Override
	public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
		return ModRecipes.CRUCIBLE_ALLOYING_SERIALIZER;
	}

	@Override
	public PlacementInfo placementInfo() {
		if (this.placementInfo == null) {
			List<net.minecraft.world.item.crafting.Ingredient> list = new ArrayList<>();
			list.add(net.minecraft.world.item.crafting.Ingredient.of(ModItems.CRUCIBLE));
			for (CountedIngredient counted : this.ingredients) {
				for (int i = 0; i < counted.count(); i++) {
					list.add(counted.ingredient());
				}
			}
			this.placementInfo = PlacementInfo.create(list);
		}
		return this.placementInfo;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return ModRecipes.CRUCIBLE_ALLOYING_CATEGORY;
	}

	@Override
	public List<RecipeDisplay> display() {
		List<SlotDisplay> inputs = new ArrayList<>();
		inputs.add(new SlotDisplay.ItemSlotDisplay(ModItems.CRUCIBLE));
		for (CountedIngredient counted : this.ingredients) {
			for (int i = 0; i < counted.count(); i++) {
				inputs.add(counted.ingredient().display());
			}
		}
		return List.of(new StonecutterRecipeDisplay(
				new SlotDisplay.Composite(inputs),
				new SlotDisplay.ItemStackSlotDisplay(this.result),
				new SlotDisplay.ItemSlotDisplay(ModBlocks.BASIC_SMELTER.asItem())
		));
	}
}
