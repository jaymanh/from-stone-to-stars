package jaymanh.fsts.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class StoragePotBlockEntity extends BaseContainerBlockEntity {
	public static final int SIZE = 27;

	private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

	public StoragePotBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.STORAGE_POT, pos, state);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.from-stone-to-stars.storage_pot");
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.items = items;
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return ChestMenu.threeRows(containerId, inventory, this);
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(input, this.items);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		ContainerHelper.saveAllItems(output, this.items);
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (this.level != null) {
			Containers.dropContents(this.level, pos, this.items);
		}
		super.preRemoveSideEffects(pos, state);
	}
}
