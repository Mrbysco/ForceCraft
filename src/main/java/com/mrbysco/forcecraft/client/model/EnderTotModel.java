package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.entities.EnderTotEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class EnderTotModel<T extends EnderTotEntity> extends HumanoidModel<T> {
	public boolean isCarrying;
	public boolean isAttacking;

	public EnderTotModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, -14.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartPose partpose = PartPose.offset(0.0F, -13.0F, 0.0F);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create()
				.texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), partpose);
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), partpose);
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(32, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F), PartPose.offset(0.0F, -14.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, -12.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(56, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, -12.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offset(-2.0F, -5.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(56, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offset(2.0F, -5.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.head.visible = true;
		float f = 8.0F;
		this.body.xRot = 0.0F;
		this.body.y = f;
		this.body.z = -0.0F;
		this.rightLeg.xRot -= 0.0F;
		this.leftLeg.xRot -= 0.0F;
		this.rightArm.xRot = (float) ((double) this.rightArm.xRot * 0.5D);
		this.leftArm.xRot = (float) ((double) this.leftArm.xRot * 0.5D);
		this.rightLeg.xRot = (float) ((double) this.rightLeg.xRot * 0.5D);
		this.leftLeg.xRot = (float) ((double) this.leftLeg.xRot * 0.5D);
		float f1 = 0.4F;
		if (this.rightArm.xRot > f1) {
			this.rightArm.xRot = f1;
		}

		if (this.leftArm.xRot > f1) {
			this.leftArm.xRot = f1;
		}

		if (this.rightArm.xRot < -f1) {
			this.rightArm.xRot = -f1;
		}

		if (this.leftArm.xRot < -f1) {
			this.leftArm.xRot = -f1;
		}

		if (this.rightLeg.xRot > f1) {
			this.rightLeg.xRot = f1;
		}

		if (this.leftLeg.xRot > f1) {
			this.leftLeg.xRot = f1;
		}

		if (this.rightLeg.xRot < -f1) {
			this.rightLeg.xRot = -f1;
		}

		if (this.leftLeg.xRot < -f1) {
			this.leftLeg.xRot = -f1;
		}

		if (this.isCarrying) {
			this.rightArm.xRot = -0.5F;
			this.leftArm.xRot = -0.5F;
			this.rightArm.zRot = 0.05F;
			this.leftArm.zRot = -0.05F;
		}

		this.rightArm.z = 0.0F;
		this.leftArm.z = 0.0F;
		this.rightLeg.z = 0.0F;
		this.leftLeg.z = 0.0F;
		this.rightLeg.y = 16.0F;
		this.leftLeg.y = 16.0F;
		this.head.z = -0.0F;
		this.head.y = f;
		this.hat.x = this.head.x;
		this.hat.y = 8.5F;
		this.hat.z = this.head.z;
		this.hat.xRot = this.head.xRot;
		this.hat.yRot = this.head.yRot;
		this.hat.zRot = this.head.zRot;
		if (this.isAttacking) {
			this.head.y -= 5.0F;
		}

		this.rightArm.setPos(-5.0F, 9.0F, 0.0F);
		this.leftArm.setPos(5.0F, 9.0F, 0.0F);
	}


	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}