package jaymanh.fsts.multiblock;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * Fuzzy structure: controller block + component rules inside a local volume.
 */
public final class AssemblyDefinition {
	private final Identifier id;
	private final Predicate<BlockState> controllerPredicate;
	private final AABB scanBox;
	private final List<ComponentRule> rules;
	private final MultiblockProcess.Factory processFactory;
	private final Component displayName;

	public AssemblyDefinition(
			Identifier id,
			Predicate<BlockState> controllerPredicate,
			AABB scanBox,
			List<ComponentRule> rules,
			MultiblockProcess.Factory processFactory,
			Component displayName
	) {
		this.id = id;
		this.controllerPredicate = controllerPredicate;
		this.scanBox = scanBox;
		this.rules = List.copyOf(rules);
		this.processFactory = processFactory;
		this.displayName = displayName;
	}

	public Identifier id() {
		return this.id;
	}

	public Predicate<BlockState> controllerPredicate() {
		return this.controllerPredicate;
	}

	public AABB scanBox() {
		return this.scanBox;
	}

	public List<ComponentRule> rules() {
		return this.rules;
	}

	public MultiblockProcess.Factory processFactory() {
		return this.processFactory;
	}

	public Component displayName() {
		return this.displayName;
	}
}
