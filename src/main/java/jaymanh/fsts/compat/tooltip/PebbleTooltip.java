package jaymanh.fsts.compat.tooltip;

import java.util.Optional;

import jaymanh.fsts.block.ModCustomBlockModels.PebbleBlock;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public final class PebbleTooltip {
	private PebbleTooltip() {
	}

	public static Optional<Component> waterloggedLine(BlockState state) {
		if (state.hasProperty(PebbleBlock.WATERLOGGED) && state.getValue(PebbleBlock.WATERLOGGED)) {
			return Optional.of(Component.translatable("tooltip.from-stone-to-stars.waterlogged"));
		}

		return Optional.empty();
	}
}
