package jaymanh.fsts.rotation;

public final class RotationConstants {
	public static final int MAX_SHAFT_LENGTH = 8;
	public static final int MACHINE_COST = 1;
	public static final int POWERED_QUERN_TICKS_PER_GRIND = 8;
	/** About four hits every three seconds. */
	public static final int TRIP_HAMMER_TICKS_PER_HIT = 15;
	/** Vanilla hoppers move one item every 8 ticks. This hopper waits twice as long. */
	public static final int BRONZE_HOPPER_TICKS_PER_MOVE = 16;
	/** A formed sawmill loads the network once per blade lane. */
	public static final int SAW_ARBOR_COST_PER_LANE = 1;

	private RotationConstants() {
	}
}
