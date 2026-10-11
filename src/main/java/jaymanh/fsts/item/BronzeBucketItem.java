package jaymanh.fsts.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

/**
 * Empty, water, and lava bronze buckets. Pickup and placement follow {@link BucketItem}.
 */
public class BronzeBucketItem extends BucketItem {
	public BronzeBucketItem(Fluid content, Item.Properties properties) {
		super(content, properties);
	}
}
