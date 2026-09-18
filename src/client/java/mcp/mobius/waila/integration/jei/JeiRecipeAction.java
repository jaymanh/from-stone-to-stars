package mcp.mobius.waila.integration.jei;

import mezz.jei.api.IModPlugin;

import net.minecraft.resources.Identifier;

/**
 * WTHIT 20.0.0 lists this class as its {@code jei_mod_plugin} entrypoint, but the
 * published jar comments the implementation out ({@code TODO: Implement JEI again}).
 * JEI still tries to load it and logs an error. This empty plugin satisfies that
 * entrypoint until WTHIT ships the class again.
 */
public final class JeiRecipeAction implements IModPlugin {
	@Override
	public Identifier getPluginUid() {
		return Identifier.fromNamespaceAndPath("wthit", "jei");
	}
}
