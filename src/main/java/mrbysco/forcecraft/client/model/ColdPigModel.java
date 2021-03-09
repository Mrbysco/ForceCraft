package mrbysco.forcecraft.client.model;

import mrbysco.forcecraft.entities.ColdPigEntity;
import net.minecraft.client.renderer.entity.model.PigModel;

public class ColdPigModel<T extends ColdPigEntity> extends PigModel<T> {
	private float headRotationAngleX;

	public ColdPigModel() {
		super();
	}

	public ColdPigModel(float scale) {
		super(scale);
	}

	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
		this.headModel.rotationPointY = 12.0F + entityIn.getHeadRotationPointY(partialTick) * 4.0F;
		this.headRotationAngleX = entityIn.getHeadRotationAngleX(partialTick);
	}

	/**
	 * Sets this entity's model rotation angles
	 */
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.headModel.rotateAngleX = this.headRotationAngleX;
	}
}
