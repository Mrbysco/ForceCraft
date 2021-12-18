package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.entities.ColdCowEntity;
import net.minecraft.client.model.CowModel;

public class ColdCowModel<T extends ColdCowEntity> extends CowModel<T> {
	private float headRotationAngleX;

	public ColdCowModel() {
		super();
	}

	public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
		this.head.y = 4.0F + entityIn.getHeadRotationPointY(partialTick) * 5.0F;
		this.headRotationAngleX = entityIn.getHeadRotationAngleX(partialTick);
	}

	/**
	 * Sets this entity's model rotation angles
	 */
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.head.xRot = this.headRotationAngleX;
	}
}
