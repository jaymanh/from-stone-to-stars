package jaymanh.fsts.client.compat.jei;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.ItemLike;

public record ForgingJeiRecipe(Identifier id, SlotDisplay input, SlotDisplay output, int hits) {
	public static ForgingJeiRecipe of(Identifier id, ItemLike input, int inputCount, ItemLike output, int outputCount, int hits) {
		return new ForgingJeiRecipe(
				id,
				SimpleProcessRecipe.stack(new ItemStack(input, inputCount)),
				SimpleProcessRecipe.stack(new ItemStack(output, outputCount)),
				hits
		);
	}
}
