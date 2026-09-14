package jaymanh.fsts.client.compat.emi;

/**
 * EMI plugin placeholder.
 *
 * EMI has not published a Fabric 26.2 artifact yet, so this class is not registered
 * as an {@code emi} entrypoint. When EMI ports, implement {@code dev.emi.emi.api.EmiPlugin}
 * here and add the entrypoint in {@code fabric.mod.json}.
 *
 * JEI covers recipe viewing on 26.2 until then.
 */
public final class FstsEmiPlugin {
	private FstsEmiPlugin() {
	}
}
