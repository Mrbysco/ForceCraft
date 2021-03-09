package mrbysco.forcecraft.client.model;

import mrbysco.forcecraft.entities.ColdCowEntity;
import net.minecraft.client.renderer.entity.model.CowModel;

public class ColdCowModel<T extends ColdCowEntity> extends CowModel<T> {
	private float headRotationAngleX;

	public ColdCowModel() {
		super();
	}

	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
		this.headModel.rotationPointY = 4.0F + entityIn.getHeadRotationPointY(partialTick) * 5.0F;
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
