package jaymanh.fsts.compat.energy;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.compat.CompatMods;

import team.reborn.energy.api.EnergyStorage;

public final class TeamRebornEnergyCompat {
	private TeamRebornEnergyCompat() {
	}

	public static void initialize() {
		if (!CompatMods.TEAM_REBORN_ENERGY) {
			FromStoneToStars.LOGGER.debug("Team Reborn Energy is not loaded; skipping electrical lookup registration");
			return;
		}

		// Touch the lookup so the API initialises. Block entities that store energy
		// should call EnergyStorage.SIDED.registerForBlockEntity(...) when they exist.
		EnergyStorage.SIDED.getClass();

		if (CompatMods.AE2) {
			FromStoneToStars.LOGGER.info("AE2 present: machines that expose Team Reborn EnergyStorage will be usable by AE2 energy acceptors");
		} else {
			FromStoneToStars.LOGGER.info("Team Reborn Energy API ready (Fabric FE equivalent). AE2 and other energy mods remain optional");
		}
	}
}
