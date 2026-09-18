package jaymanh.fsts.multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Live instance of a matched structure plus its running process.
 */
public final class FormedMultiblock {
	private final MultiblockMatch match;
	private final MultiblockProcess process;
	private boolean active = true;

	public FormedMultiblock(MultiblockMatch match, MultiblockProcess process) {
		this.match = match;
		this.process = process;
	}

	public MultiblockMatch match() {
		return this.match;
	}

	public MultiblockProcess process() {
		return this.process;
	}

	public int getProcessProgress() {
		return this.process instanceof ProgressAware aware ? aware.getProgress() : 0;
	}

	public boolean isActive() {
		return this.active;
	}

	public void onFormed(ServerLevel level) {
		this.process.onFormed(level, this);
	}

	public void tick(ServerLevel level) {
		if (!this.active) {
			return;
		}
		if (!MultiblockMatcher.stillValid(level, this.match)) {
			this.cancel(level);
			return;
		}
		if (!this.process.tick(level, this)) {
			this.active = false;
		}
	}

	public void cancel(ServerLevel level) {
		if (!this.active) {
			return;
		}
		this.active = false;
		this.process.onCancel(level, this);
	}

	public void saveAdditional(ValueOutput output) {
		output.putString("Definition", this.match.definition().id().toString());
		output.putString("Rotation", this.match.rotation().name());
		output.putInt("OriginX", this.match.origin().getX());
		output.putInt("OriginY", this.match.origin().getY());
		output.putInt("OriginZ", this.match.origin().getZ());

		int progress = this.process instanceof ProgressAware aware ? aware.getProgress() : 0;
		output.putInt("ProcessProgress", progress);

		int size = this.match.members().size();
		int[] xs = new int[size];
		int[] ys = new int[size];
		int[] zs = new int[size];
		String[] roleNames = new String[size];
		for (int i = 0; i < size; i++) {
			MultiblockMatch.Member member = this.match.members().get(i);
			xs[i] = member.pos().getX();
			ys[i] = member.pos().getY();
			zs[i] = member.pos().getZ();
			roleNames[i] = member.role().getSerializedName();
		}
		output.putIntArray("MemberX", xs);
		output.putIntArray("MemberY", ys);
		output.putIntArray("MemberZ", zs);
		output.putString("MemberRoles", String.join(",", roleNames));
	}

	public static Optional<FormedMultiblock> load(ValueInput input) {
		Optional<String> definitionId = input.getString("Definition");
		if (definitionId.isEmpty()) {
			return Optional.empty();
		}
		Optional<MultiblockDefinition> definition = MultiblockRegistry.get(Identifier.parse(definitionId.get()));
		if (definition.isEmpty()) {
			return Optional.empty();
		}

		Rotation rotation = Rotation.valueOf(input.getString("Rotation").orElse(Rotation.NONE.name()));
		BlockPos origin = new BlockPos(
				input.getIntOr("OriginX", 0),
				input.getIntOr("OriginY", 0),
				input.getIntOr("OriginZ", 0)
		);

		int[] xs = input.getIntArray("MemberX").orElse(new int[0]);
		int[] ys = input.getIntArray("MemberY").orElse(new int[0]);
		int[] zs = input.getIntArray("MemberZ").orElse(new int[0]);
		String rolesJoined = input.getString("MemberRoles").orElse("");
		String[] roleNames = rolesJoined.isEmpty() ? new String[0] : rolesJoined.split(",");

		List<MultiblockMatch.Member> members = new ArrayList<>();
		int count = Math.min(xs.length, Math.min(ys.length, Math.min(zs.length, roleNames.length)));
		for (int i = 0; i < count; i++) {
			MultiblockRole role = MultiblockRole.valueOf(roleNames[i].toUpperCase());
			members.add(new MultiblockMatch.Member(new BlockPos(xs[i], ys[i], zs[i]), role));
		}

		MultiblockMatch match = new MultiblockMatch(definition.get(), origin, rotation, members);
		MultiblockProcess process = definition.get().processFactory().create(match);
		if (process instanceof ProgressAware progressAware) {
			progressAware.setProgress(input.getIntOr("ProcessProgress", 0));
		}
		return Optional.of(new FormedMultiblock(match, process));
	}

	public interface ProgressAware {
		int getProgress();

		void setProgress(int progress);
	}
}
