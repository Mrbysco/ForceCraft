package mrbysco.forcecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class CreeperTotModel<T extends Entity> extends SegmentedModel<T> {
	private final ModelRenderer Head;
	private final ModelRenderer HeadWear;
	private final ModelRenderer Torso;
	private final ModelRenderer Leg2;
	private final ModelRenderer Leg4;
	private final ModelRenderer Leg3;
	private final ModelRenderer Leg1;

	public CreeperTotModel() {
		this(0.0F);
	}

	public CreeperTotModel(float size) {
		textureWidth = 64;
		textureHeight = 32;

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 14.0F, 0.0F);
		Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, size, false);

		HeadWear = new ModelRenderer(this);
		HeadWear.setRotationPoint(0.0F, 14.0F, 0.0F);
		HeadWear.setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, size + 0.5F, false);

		Torso = new ModelRenderer(this);
		Torso.setRotationPoint(0.0F, 20.0F, 0.0F);
		Torso.setTextureOffset(16, 16).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, size, false);

		Leg2 = new ModelRenderer(this);
		Leg2.setRotationPoint(-2.0F, 20.0F, 4.0F);
		Leg2.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, size, false);

		Leg4 = new ModelRenderer(this);
		Leg4.setRotationPoint(-2.0F, 20.0F, -4.0F);
		Leg4.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, size, false);

		Leg3 = new ModelRenderer(this);
		Leg3.setRotationPoint(2.0F, 20.0F, -4.0F);
		Leg3.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, size, false);

		Leg1 = new ModelRenderer(this);
		Leg1.setRotationPoint(2.0F, 20.0F, 4.0F);
		Leg1.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, size, false);
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.Head, this.Torso, this.Leg1, this.Leg2, this.Leg3, this.Leg4);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		this.Head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
		this.Head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
		this.Leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.Leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.Leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.Leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}
}