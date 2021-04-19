package mrbysco.forcecraft.client.model;

import mrbysco.forcecraft.entities.EnderTotEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class EnderTotModel<T extends EnderTotEntity> extends BipedModel<T> {
	public boolean isCarrying;
	public boolean isAttacking;

	public EnderTotModel(float scale) {
		super(0.0F, 8F, 64, 32);
		float f = 8F;

		bipedHeadwear = new ModelRenderer(this);
		bipedHeadwear.setRotationPoint(0.0F, f, 0.0F);
		bipedHeadwear.setTextureOffset(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, scale - 0.5F, false);

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, f, 0.0F);
		bipedBody.setTextureOffset(32, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F, scale, false);

		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(5.0F, 9.0F, 0.0F);
		setRotationAngle(bipedRightArm, 0.0F, 0.0F, -0.1F);
		bipedRightArm.setTextureOffset(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, scale, false);

		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(-5.0F, 9.0F, 0.0F);
		setRotationAngle(bipedLeftArm, 0.0F, 0.0F, 0.1F);
		bipedLeftArm.setTextureOffset(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, scale, true);

		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(2.0F, 16.0F, 0.0F);
		bipedRightLeg.setTextureOffset(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, scale, false);

		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(-2.0F, 16.0F, 0.0F);
		bipedLeftLeg.setTextureOffset(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, scale, true);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.bipedHead.showModel = true;
		float f = 8.0F;
		this.bipedBody.rotateAngleX = 0.0F;
		this.bipedBody.rotationPointY = f;
		this.bipedBody.rotationPointZ = -0.0F;
		this.bipedRightLeg.rotateAngleX -= 0.0F;
		this.bipedLeftLeg.rotateAngleX -= 0.0F;
		this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX * 0.5D);
		this.bipedLeftArm.rotateAngleX = (float)((double)this.bipedLeftArm.rotateAngleX * 0.5D);
		this.bipedRightLeg.rotateAngleX = (float)((double)this.bipedRightLeg.rotateAngleX * 0.5D);
		this.bipedLeftLeg.rotateAngleX = (float)((double)this.bipedLeftLeg.rotateAngleX * 0.5D);
		float f1 = 0.4F;
		if (this.bipedRightArm.rotateAngleX > f1) {
			this.bipedRightArm.rotateAngleX = f1;
		}

		if (this.bipedLeftArm.rotateAngleX > f1) {
			this.bipedLeftArm.rotateAngleX = f1;
		}

		if (this.bipedRightArm.rotateAngleX < -f1) {
			this.bipedRightArm.rotateAngleX = -f1;
		}

		if (this.bipedLeftArm.rotateAngleX < -f1) {
			this.bipedLeftArm.rotateAngleX = -f1;
		}

		if (this.bipedRightLeg.rotateAngleX > f1) {
			this.bipedRightLeg.rotateAngleX = f1;
		}

		if (this.bipedLeftLeg.rotateAngleX > f1) {
			this.bipedLeftLeg.rotateAngleX = f1;
		}

		if (this.bipedRightLeg.rotateAngleX < -f1) {
			this.bipedRightLeg.rotateAngleX = -f1;
		}

		if (this.bipedLeftLeg.rotateAngleX < -f1) {
			this.bipedLeftLeg.rotateAngleX = -f1;
		}

		if (this.isCarrying) {
			this.bipedRightArm.rotateAngleX = -0.5F;
			this.bipedLeftArm.rotateAngleX = -0.5F;
			this.bipedRightArm.rotateAngleZ = 0.05F;
			this.bipedLeftArm.rotateAngleZ = -0.05F;
		}

		this.bipedRightArm.rotationPointZ = 0.0F;
		this.bipedLeftArm.rotationPointZ = 0.0F;
		this.bipedRightLeg.rotationPointZ = 0.0F;
		this.bipedLeftLeg.rotationPointZ = 0.0F;
		this.bipedRightLeg.rotationPointY = 16.0F;
		this.bipedLeftLeg.rotationPointY = 16.0F;
		this.bipedHead.rotationPointZ = -0.0F;
		this.bipedHead.rotationPointY = f;
		this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
		this.bipedHeadwear.rotationPointY = 8.5F;
		this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
		if (this.isAttacking) {
			this.bipedHead.rotationPointY -= 5.0F;
		}

		this.bipedRightArm.setRotationPoint(-5.0F, 9.0F, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 9.0F, 0.0F);
	}


	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}