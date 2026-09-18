package jaymanh.fsts.client.compat.jei;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record WorldInteractionRecipe(
		Identifier id,
		SlotDisplay tool,
		SlotDisplay target,
		SlotDisplay result
) {
}
