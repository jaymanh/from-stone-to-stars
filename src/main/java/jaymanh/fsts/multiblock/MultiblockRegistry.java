package jaymanh.fsts.multiblock;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.resources.Identifier;

public final class MultiblockRegistry {
	private static final Map<Identifier, MultiblockDefinition> DEFINITIONS = new LinkedHashMap<>();

	private MultiblockRegistry() {
	}

	public static void register(MultiblockDefinition definition) {
		if (DEFINITIONS.putIfAbsent(definition.id(), definition) != null) {
			throw new IllegalStateException("Duplicate multiblock definition: " + definition.id());
		}
	}

	public static Optional<MultiblockDefinition> get(Identifier id) {
		return Optional.ofNullable(DEFINITIONS.get(id));
	}

	public static Collection<MultiblockDefinition> all() {
		return Collections.unmodifiableCollection(DEFINITIONS.values());
	}

	public static void clearForTests() {
		DEFINITIONS.clear();
	}
}
