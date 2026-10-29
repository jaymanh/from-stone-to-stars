package jaymanh.fsts.block.entity;

import java.util.ArrayList;
import java.util.List;

import jaymanh.fsts.inventory.TappingSpoutMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

/**
 * Where the bore rig puts the ore it brings up. Only the rig fills it; players and hoppers take from it.
 */
public class OreBoxBlockEntity extends ForgeStoreBlockEntity {
	public OreBoxBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ORE_BOX, pos, state);
	}

	/** True when every stack would fit at once. */
	public boolean canFit(List<ItemStack> stacks) {
		List<ItemStack> simulated = new ArrayList<>();
		for (ItemStack held : this.getItems()) {
			simulated.add(held.copy());
		}
		for (ItemStack stack : stacks) {
			ItemStack remaining = stack.copy();
			for (ItemStack held : simulated) {
				if (remaining.isEmpty()) {
					break;
				}
				if (!held.isEmpty() && ItemStack.isSameItemSameComponents(held, remaining)) {
					int moved = Math.min(remaining.getCount(), this.getMaxStackSize(held) - held.getCount());
					held.grow(moved);
					remaining.shrink(moved);
				}
			}
			for (int slot = 0; slot < simulated.size() && !remaining.isEmpty(); slot++) {
				if (simulated.get(slot).isEmpty()) {
					simulated.set(slot, remaining.split(this.getMaxStackSize(remaining)));
				}
			}
			if (!remaining.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public boolean takeAll(Player player) {
		boolean took = false;
		for (int slot = 0; slot < this.getContainerSize(); slot++) {
			ItemStack stack = this.getItem(slot);
			if (!stack.isEmpty()) {
				this.getItems().set(slot, ItemStack.EMPTY);
				player.getInventory().placeItemBackInInventory(stack);
				took = true;
			}
		}
		if (took) {
			this.setChanged();
		}
		return took;
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.from-stone-to-stars.ore_box");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new TappingSpoutMenu(containerId, inventory, this);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return true;
	}
}
