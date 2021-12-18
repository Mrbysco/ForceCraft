package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.entities.EnderTotEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public class EnderTotModel<T extends EnderTotEntity> extends HumanoidModel<T> {
	public boolean isCarrying;
	public boolean isAttacking;

	public EnderTotModel(float scale) {
		super(0.0F, 8F, 64, 32);
		float f = 8F;

		hat = new ModelPart(this);
		hat.setPos(0.0F, f, 0.0F);
		hat.texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, scale - 0.5F, false);

		body = new ModelPart(this);
		body.setPos(0.0F, f, 0.0F);
		body.texOffs(32, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F, scale, false);

		rightArm = new ModelPart(this);
		rightArm.setPos(5.0F, 9.0F, 0.0F);
		setRotationAngle(rightArm, 0.0F, 0.0F, -0.1F);
		rightArm.texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, scale, false);

		leftArm = new ModelPart(this);
		leftArm.setPos(-5.0F, 9.0F, 0.0F);
		setRotationAngle(leftArm, 0.0F, 0.0F, 0.1F);
		leftArm.texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, scale, true);

		rightLeg = new ModelPart(this);
		rightLeg.setPos(2.0F, 16.0F, 0.0F);
		rightLeg.texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, scale, false);

		leftLeg = new ModelPart(this);
		leftLeg.setPos(-2.0F, 16.0F, 0.0F);
		leftLeg.texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, scale, true);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.head.visible = true;
		float f = 8.0F;
		this.body.xRot = 0.0F;
		this.body.y = f;
		this.body.z = -0.0F;
		this.rightLeg.xRot -= 0.0F;
		this.leftLeg.xRot -= 0.0F;
		this.rightArm.xRot = (float)((double)this.rightArm.xRot * 0.5D);
		this.leftArm.xRot = (float)((double)this.leftArm.xRot * 0.5D);
		this.rightLeg.xRot = (float)((double)this.rightLeg.xRot * 0.5D);
		this.leftLeg.xRot = (float)((double)this.leftLeg.xRot * 0.5D);
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