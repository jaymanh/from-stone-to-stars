package jaymanh.fsts.inventory;

import jaymanh.fsts.block.ModBlocks;
import jaymanh.fsts.block.ModCustomBlockModels.WorkStumpBlock;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Crafting table recipes with anvil-like wear on the work stump when taking results.
 */
public class WorkStumpMenu extends CraftingMenu {
	/** Same chance as {@link net.minecraft.world.inventory.AnvilMenu}. */
	public static final float DAMAGE_CHANCE = 0.12F;

	private final ContainerLevelAccess access;

	public WorkStumpMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
		super(containerId, inventory, access);
		this.access = access;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(this.access, player, ModBlocks.WORK_STUMP);
	}

	/**
	 * Called after a craft result is taken. 12% chance to wear or destroy the stump.
	 */
	public void onCraftTaken(Player player) {
		this.access.execute((level, pos) -> {
			BlockState state = level.getBlockState(pos);
			if (!state.is(ModBlocks.WORK_STUMP)) {
				return;
			}
			if (player.getRandom().nextFloat() >= DAMAGE_CHANCE) {
				return;
			}

			BlockState damaged = WorkStumpBlock.damage(state);
			if (damaged == null) {
				level.levelEvent(1029, pos, 0);
				level.removeBlock(pos, false);
				Block.popResource(level, pos, new ItemStack(Items.STICK, 1 + player.getRandom().nextInt(2)));
			} else {
				level.setBlock(pos, damaged, Block.UPDATE_ALL);
				level.levelEvent(1030, pos, 0);
			}
		});
	}
}
