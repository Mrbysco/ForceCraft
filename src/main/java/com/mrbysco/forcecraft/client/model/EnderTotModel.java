package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.client.state.EnderTotRenderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class EnderTotModel extends HumanoidModel<EnderTotRenderState> {

	public EnderTotModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, -14.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartPose partpose = PartPose.offset(0.0F, 7.0F, 0.0F);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create()
				.texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), partpose);
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), partpose);
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(32, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F), PartPose.offset(0.0F, 7.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, 9.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(56, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, 9.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offset(-2.0F, 16.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(56, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offset(2.0F, 16.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(EnderTotRenderState state) {
		super.setupAnim(state);
		this.head.visible = true;
		this.rightArm.xRot *= 0.5F;
		this.leftArm.xRot *= 0.5F;
		this.rightLeg.xRot *= 0.5F;
		this.leftLeg.xRot *= 0.5F;
		this.rightArm.xRot = Mth.clamp(this.rightArm.xRot, -0.4F, 0.4F);
		this.leftArm.xRot = Mth.clamp(this.leftArm.xRot, -0.4F, 0.4F);
		this.rightLeg.xRot = Mth.clamp(this.rightLeg.xRot, -0.4F, 0.4F);
		this.leftLeg.xRot = Mth.clamp(this.leftLeg.xRot, -0.4F, 0.4F);
		if (!state.carriedBlock.isEmpty()) {
			this.rightArm.xRot = -0.5F;
			this.leftArm.xRot = -0.5F;
			this.rightArm.zRot = 0.05F;
			this.leftArm.zRot = -0.05F;
		}

		if (state.isCreepy) {
			this.head.y -= 5.0F;
			this.hat.y += 5.0F;
		}
	}
}
