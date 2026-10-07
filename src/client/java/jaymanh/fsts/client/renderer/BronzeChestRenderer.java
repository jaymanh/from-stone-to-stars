package jaymanh.fsts.client.renderer;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.entity.BronzeChestBlockEntity;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Draws the bronze chest with a hinged lid. The base stops where the lid starts.
 */
public class BronzeChestRenderer implements BlockEntityRenderer<BronzeChestBlockEntity, ChestRenderState> {
	private static final MultiblockChestResources<SpriteId> SPRITES = new MultiblockChestResources<>(
			Sheets.CHEST_MAPPER.apply(FromStoneToStars.id("bronze")),
			Sheets.CHEST_MAPPER.apply(FromStoneToStars.id("bronze_left")),
			Sheets.CHEST_MAPPER.apply(FromStoneToStars.id("bronze_right"))
	);

	private final SpriteGetter sprites;
	private final MultiblockChestResources<ChestModel> models;

	public BronzeChestRenderer(BlockEntityRendererProvider.Context context) {
		this.sprites = context.sprites();
		this.models = new MultiblockChestResources<>(
				new ChestModel(context.bakeLayer(BronzeChestModel.SINGLE)),
				new ChestModel(context.bakeLayer(BronzeChestModel.LEFT)),
				new ChestModel(context.bakeLayer(BronzeChestModel.RIGHT))
		);
	}

	@Override
	public ChestRenderState createRenderState() {
		return new ChestRenderState();
	}

	@Override
	public void extractRenderState(
			BronzeChestBlockEntity chest,
			ChestRenderState state,
			float partialTick,
			Vec3 cameraPosition,
			ModelFeatureRenderer.CrumblingOverlay breakProgress
	) {
		BlockEntityRenderer.super.extractRenderState(chest, state, partialTick, cameraPosition, breakProgress);
		BlockState blockState = chest.getBlockState();
		state.type = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
		state.facing = blockState.getValue(ChestBlock.FACING);
		state.material = ChestRenderState.ChestMaterialType.REGULAR;
		Level level = chest.getLevel();
		if (level != null && blockState.getBlock() instanceof ChestBlock chestBlock) {
			var combined = chestBlock.combine(blockState, level, chest.getBlockPos(), true);
			Float2FloatFunction openness = combined.apply(ChestBlock.opennessCombiner(chest));
			state.open = openness.get(partialTick);
			if (state.type != ChestType.SINGLE) {
				Int2IntFunction light = combined.apply(new BrightnessCombiner<>());
				state.lightCoords = light.applyAsInt(state.lightCoords);
			}
		} else {
			state.open = chest.getOpenNess(partialTick);
		}
	}

	@Override
	public void submit(ChestRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.mulPose(ChestRenderer.modelTransformation(state.facing));
		float open = 1.0F - state.open;
		open = 1.0F - open * open * open;
		collector.submitModel(
				models.select(state.type),
				open,
				poseStack,
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				-1,
				SPRITES.select(state.type),
				sprites,
				0,
				state.breakProgress
		);
		poseStack.popPose();
	}
}
