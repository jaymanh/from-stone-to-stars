package jaymanh.fsts.client.renderer;

import jaymanh.fsts.FromStoneToStars;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Vanilla chest layers overlap the lid and the base by one pixel. These layers stop the base
 * where the lid starts, so the seam does not z-fight.
 */
public final class BronzeChestModel {
	public static final ModelLayerLocation SINGLE = layer("bronze_chest");
	public static final ModelLayerLocation LEFT = layer("bronze_chest_left");
	public static final ModelLayerLocation RIGHT = layer("bronze_chest_right");

	private static final int TEX_WIDTH = 64;
	private static final int TEX_HEIGHT = 64;
	private static final float BASE_HEIGHT = 9.0F;

	private BronzeChestModel() {
	}

	public static LayerDefinition createSingleLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild(
				"bottom",
				CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, BASE_HEIGHT, 14.0F),
				PartPose.ZERO
		);
		root.addOrReplaceChild(
				"lid",
				CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F),
				PartPose.offset(0.0F, BASE_HEIGHT, 1.0F)
		);
		root.addOrReplaceChild(
				"lock",
				CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F),
				PartPose.offset(0.0F, BASE_HEIGHT, 1.0F)
		);
		return LayerDefinition.create(mesh, TEX_WIDTH, TEX_HEIGHT);
	}

	public static LayerDefinition createLeftLayer() {
		return createDoubleLayer(0.0F, 0.0F);
	}

	public static LayerDefinition createRightLayer() {
		return createDoubleLayer(1.0F, 15.0F);
	}

	private static LayerDefinition createDoubleLayer(float boxX, float lockX) {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild(
				"bottom",
				CubeListBuilder.create().texOffs(0, 19).addBox(boxX, 0.0F, 1.0F, 15.0F, BASE_HEIGHT, 14.0F),
				PartPose.ZERO
		);
		root.addOrReplaceChild(
				"lid",
				CubeListBuilder.create().texOffs(0, 0).addBox(boxX, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F),
				PartPose.offset(0.0F, BASE_HEIGHT, 1.0F)
		);
		root.addOrReplaceChild(
				"lock",
				CubeListBuilder.create().texOffs(0, 0).addBox(lockX, -2.0F, 14.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offset(0.0F, BASE_HEIGHT, 1.0F)
		);
		return LayerDefinition.create(mesh, TEX_WIDTH, TEX_HEIGHT);
	}

	private static ModelLayerLocation layer(String name) {
		return new ModelLayerLocation(FromStoneToStars.id(name), "main");
	}
}
