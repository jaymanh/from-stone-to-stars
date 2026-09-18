package jaymanh.fsts.client.compat.jei;

import java.util.Arrays;
import java.util.List;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.ItemLike;

public record SimpleProcessRecipe(
		Identifier id,
		List<SlotDisplay> inputs,
		List<SlotDisplay> catalysts,
		SlotDisplay output,
		int durationTicks
) {
	public static SlotDisplay item(ItemLike item) {
		return new SlotDisplay.ItemSlotDisplay(item.asItem());
	}

	public static SlotDisplay stack(ItemStack stack) {
		return new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(stack));
	}

	public static SlotDisplay tag(TagKey<Item> tag) {
		return new SlotDisplay.TagSlotDisplay(tag);
	}

	public static SlotDisplay any(ItemLike... items) {
		return new SlotDisplay.Composite(Arrays.stream(items).map(SimpleProcessRecipe::item).toList());
	}
}
