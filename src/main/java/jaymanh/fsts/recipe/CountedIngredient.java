package jaymanh.fsts.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * One alloying input: an ingredient that must occupy {@code count} crafting slots
 * (vanilla crafting consumes one item per occupied slot).
 */
public record CountedIngredient(Ingredient ingredient, int count) {
	public static final Codec<CountedIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("ingredient").forGetter(CountedIngredient::ingredient),
			ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(CountedIngredient::count)
	).apply(instance, CountedIngredient::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CountedIngredient> STREAM_CODEC = StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC,
			CountedIngredient::ingredient,
			ByteBufCodecs.VAR_INT,
			CountedIngredient::count,
			CountedIngredient::new
	);

	public boolean test(ItemStack stack) {
		return this.ingredient.test(stack);
	}
}
