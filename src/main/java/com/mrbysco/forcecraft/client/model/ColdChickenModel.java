package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.entities.ColdChickenEntity;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;

public class ColdChickenModel<T extends ColdChickenEntity> extends ChickenModel<T> {
	private float headRotationAngleX;

	public ColdChickenModel(ModelPart root) {
		super(root);
	}

	public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
		this.head.y = 15.0F + entityIn.getHeadRotationPointY(partialTick) * 4.0F;
		this.beak.y = 15.0F + entityIn.getHeadRotationPointY(partialTick) * 4.0F;
		this.redThing.y = 15.0F + entityIn.getHeadRotationPointY(partialTick) * 4.0F;
		this.headRotationAngleX = entityIn.getHeadRotationAngleX(partialTick);
	}

	/**
	 * Sets this entity's model rotation angles
	 */
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.head.xRot = this.headRotationAngleX;
		this.beak.xRot = this.headRotationAngleX;
		this.redThing.xRot = this.headRotationAngleX;
	}
}
