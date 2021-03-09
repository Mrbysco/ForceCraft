package mrbysco.forcecraft.client.model;

import mrbysco.forcecraft.entities.ColdChickenEntity;
import net.minecraft.client.renderer.entity.model.ChickenModel;

public class ColdChickenModel<T extends ColdChickenEntity> extends ChickenModel<T> {
	private float headRotationAngleX;

	public ColdChickenModel() {
		super();
	}

	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
		this.head.rotationPointY = 15.0F + entityIn.getHeadRotationPointY(partialTick) * 4.0F;
		this.bill.rotationPointY = 15.0F + entityIn.getHeadRotationPointY(partialTick) * 4.0F;
		this.chin.rotationPointY = 15.0F + entityIn.getHeadRotationPointY(partialTick) * 4.0F;
		this.headRotationAngleX = entityIn.getHeadRotationAngleX(partialTick);
	}

	/**
	 * Sets this entity's model rotation angles
	 */
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.head.rotateAngleX = this.headRotationAngleX;
		this.bill.rotateAngleX = this.headRotationAngleX;
		this.chin.rotateAngleX = this.headRotationAngleX;
	}
}
