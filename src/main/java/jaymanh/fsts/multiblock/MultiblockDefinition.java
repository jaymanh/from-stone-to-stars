package jaymanh.fsts.multiblock;

import java.util.function.Predicate;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Registered structure blueprint. Later machines add larger patterns and a CONTROLLER role
 * without changing the matcher or formed-structure tracking.
 */
public final class MultiblockDefinition {
	private final Identifier id;
	private final MultiblockPattern pattern;
	private final Predicate<BlockState> originPredicate;
	private final MultiblockProcess.Factory processFactory;

	public MultiblockDefinition(
			Identifier id,
			MultiblockPattern pattern,
			Predicate<BlockState> originPredicate,
			MultiblockProcess.Factory processFactory
	) {
		this.id = id;
		this.pattern = pattern;
		this.originPredicate = originPredicate;
		this.processFactory = processFactory;
	}

	public Identifier id() {
		return this.id;
	}

	public MultiblockPattern pattern() {
		return this.pattern;
	}

	public Predicate<BlockState> originPredicate() {
		return this.originPredicate;
	}

	public MultiblockProcess.Factory processFactory() {
		return this.processFactory;
	}
}
