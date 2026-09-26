package jaymanh.fsts.multiblock;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.resources.Identifier;

public final class AssemblyRegistry {
	private static final Map<Identifier, AssemblyDefinition> DEFINITIONS = new LinkedHashMap<>();

	private AssemblyRegistry() {
	}

	public static void register(AssemblyDefinition definition) {
		if (DEFINITIONS.putIfAbsent(definition.id(), definition) != null) {
			throw new IllegalStateException("Duplicate assembly definition: " + definition.id());
		}
	}

	public static Optional<AssemblyDefinition> get(Identifier id) {
		return Optional.ofNullable(DEFINITIONS.get(id));
	}

	public static Collection<AssemblyDefinition> all() {
		return Collections.unmodifiableCollection(DEFINITIONS.values());
	}

	public static void clearForTests() {
		DEFINITIONS.clear();
	}
}
