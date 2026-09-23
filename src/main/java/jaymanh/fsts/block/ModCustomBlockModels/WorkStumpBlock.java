package jaymanh.fsts.block.ModCustomBlockModels;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import jaymanh.fsts.inventory.WorkStumpMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Age 0 crafting station: same 3×3 recipes as a crafting table, but wears out like an anvil.
 */
public class WorkStumpBlock extends CraftingTableBlock {
	public static final MapCodec<WorkStumpBlock> CODEC = simpleCodec(WorkStumpBlock::new);

	/** 0 = fresh, 1 = worn, 2 = cracked. Next damage stage removes the block. */
	public static final IntegerProperty DAMAGE = IntegerProperty.create("damage", 0, 2);
	public static final int MAX_DAMAGE = 2;

	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 10.0, 15.0);
	private static final Component CONTAINER_TITLE = Component.translatable("container.from-stone-to-stars.work_stump");

	public WorkStumpBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(DAMAGE, 0));
	}

	@Override
	public MapCodec<? extends WorkStumpBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(DAMAGE);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new SimpleMenuProvider(
				(syncId, inventory, player) -> new WorkStumpMenu(syncId, inventory, ContainerLevelAccess.create(level, pos)),
				CONTAINER_TITLE
		);
	}

	/**
	 * @return next damage state, or {@code null} if the stump should break
	 */
	public static @Nullable BlockState damage(BlockState state) {
		int damage = state.getValue(DAMAGE);
		if (damage >= MAX_DAMAGE) {
			return null;
		}
		return state.setValue(DAMAGE, damage + 1);
	}
}
