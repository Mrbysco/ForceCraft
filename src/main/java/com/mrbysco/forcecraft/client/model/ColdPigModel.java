package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.entities.ColdPigEntity;
import net.minecraft.client.renderer.entity.model.PigModel;

public class ColdPigModel<T extends ColdPigEntity> extends PigModel<T> {
	private float headRotationAngleX;

	public ColdPigModel() {
		super();
	}

	public ColdPigModel(float scale) {
		super(scale);
	}

	public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
		this.head.y = 12.0F + entityIn.getHeadRotationPointY(partialTick) * 4.0F;
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
