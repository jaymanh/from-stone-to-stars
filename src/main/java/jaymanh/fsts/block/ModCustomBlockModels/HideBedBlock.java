package jaymanh.fsts.block.ModCustomBlockModels;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;

/**
 * Age 0 sleep spot: dried hides on straw. Behaves like a vanilla bed (two parts, spawn point).
 */
public class HideBedBlock extends BedBlock {
	public HideBedBlock(Properties properties) {
		super(DyeColor.BROWN, properties);
	}
}
