package jaymanh.fsts.client.renderer;

import jaymanh.fsts.block.ModCustomBlockModels.SawmillPortBlock;
import jaymanh.fsts.block.entity.SawmillPortBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Loops logs riding the infeed rollers from each intake into the blades, and planks
 * riding the outfeed rollers from the blades into each outfeed, while the mill is cutting.
 * Purely cosmetic: the speed does not follow the real cut rate.
 */
public class SawmillPortRenderer implements BlockEntityRenderer<SawmillPortBlockEntity, SawmillPortRenderer.State> {
	/** Blocks per tick along the rollers. */
	private static final float SPEED = 1.0F / 20.0F;
	private static final float LOG_SPACING = 1.25F;
	private static final float PLANK_SPACING = 0.6F;
	private static final float LOG_SCALE = 0.45F;
	private static final float PLANK_SCALE = 0.55F;
	private static final float PLANK_THICKNESS = 0.12F;
	/** Top of the wooden rollers model. */
	private static final float ROLLER_TOP = 5.0F / 16.0F;

	private final ItemModelResolver itemModelResolver;

	public SawmillPortRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(
			SawmillPortBlockEntity port,
			State state,
			float partialTick,
			Vec3 cameraPosition,
			ModelFeatureRenderer.CrumblingOverlay breakProgress
	) {
		BlockEntityRenderer.super.extractRenderState(port, state, partialTick, cameraPosition, breakProgress);
		BlockState blockState = port.getBlockState();
		Level level = port.getLevel();
		state.working = level != null
				&& blockState.hasProperty(SawmillPortBlock.WORKING)
				&& blockState.getValue(SawmillPortBlock.WORKING);
		if (!state.working) {
			return;
		}
		state.intake = port.isIntake();
		state.facing = blockState.getValue(SawmillPortBlock.FACING);
		state.distance = Math.max(1, port.travelDistance());
		state.time = (level.getGameTime() % 24000L) + partialTick;

		Direction towardRollers = state.intake ? state.facing : state.facing.getOpposite();
		state.lightCoords = LightCoordsUtil.getLightCoords(level, port.getBlockPos().relative(towardRollers));

		ItemStack display = port.displayItem();
		if (display.isEmpty()) {
			display = new ItemStack(state.intake ? Items.OAK_LOG : Items.OAK_PLANKS);
		}
		this.itemModelResolver.updateForTopItem(
				state.item, display, ItemDisplayContext.NONE, level, null, (int) port.getBlockPos().asLong()
		);
	}

	@Override
	public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		if (!state.working || state.item.isEmpty()) {
			return;
		}
		// Offsets are along FACING from the port's centre: intakes run 0 → +distance, outfeeds -distance → 0.
		float start = state.intake ? 0.35F : -state.distance + 0.35F;
		float end = state.intake ? state.distance - 0.3F : -0.35F;
		float span = end - start;
		float spacing = state.intake ? LOG_SPACING : PLANK_SPACING;
		float scroll = (state.time * SPEED) % spacing;
		float yCenter = ROLLER_TOP + (state.intake ? LOG_SCALE : PLANK_THICKNESS) / 2.0F;

		for (float along = scroll; along < span; along += spacing) {
			float offset = start + along;
			poseStack.pushPose();
			poseStack.translate(
					0.5F + state.facing.getStepX() * offset,
					yCenter,
					0.5F + state.facing.getStepZ() * offset
			);
			poseStack.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot()));
			if (state.intake) {
				poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
				poseStack.scale(LOG_SCALE, LOG_SCALE, LOG_SCALE);
			} else {
				poseStack.scale(PLANK_SCALE, PLANK_THICKNESS, PLANK_SCALE);
			}
			state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	/** Items travel over neighbouring rollers, outside this block's bounds. */
	@Override
	public boolean shouldRenderOffScreen() {
		return true;
	}

	public static class State extends BlockEntityRenderState {
		final ItemStackRenderState item = new ItemStackRenderState();
		boolean working;
		boolean intake;
		Direction facing = Direction.NORTH;
		int distance = 1;
		float time;
	}
}
