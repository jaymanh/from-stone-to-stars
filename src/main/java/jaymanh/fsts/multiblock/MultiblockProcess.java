package jaymanh.fsts.multiblock;

import net.minecraft.server.level.ServerLevel;

/**
 * Timed behaviour owned by a formed structure (firing, smelting, later machine cycles).
 */
public interface MultiblockProcess {
	/**
	 * Called once when the structure is newly formed (not on world reload).
	 */
	void onFormed(ServerLevel level, FormedMultiblock formed);

	/**
	 * @return {@code true} if the process is still active
	 */
	boolean tick(ServerLevel level, FormedMultiblock formed);

	void onCancel(ServerLevel level, FormedMultiblock formed);

	@FunctionalInterface
	interface Factory {
		MultiblockProcess create(StructureMatch match);
	}
}
