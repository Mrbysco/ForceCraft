package mrbysco.forcecraft.client.model;

import com.google.common.collect.ImmutableList;
import mrbysco.forcecraft.entities.FairyEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class FairyModel<T extends FairyEntity> extends SegmentedModel<T> {
	private final ModelRenderer Inner;
	private final ModelRenderer RightWings;
	private final ModelRenderer RightTopWing_r1;
	private final ModelRenderer LeftWings;
	private final ModelRenderer LeftTopWing_r1;
	private final ModelRenderer Outer;

	public FairyModel() {
		super(RenderType::getEntityTranslucent);
		textureWidth = 32;
		textureHeight = 32;

		Inner = new ModelRenderer(this);
		Inner.setRotationPoint(0.0F, 24.0F, 0.0F);
		Inner.setTextureOffset(0, 8).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		RightWings = new ModelRenderer(this);
		RightWings.setRotationPoint(0.0F, 0.0F, 0.0F);
		Inner.addChild(RightWings);
		RightWings.setTextureOffset(22, 5).addBox(-5.0F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		RightTopWing_r1 = new ModelRenderer(this);
		RightTopWing_r1.setRotationPoint(-2.0F, -3.0F, 0.0F);
		RightWings.addChild(RightTopWing_r1);
		setRotationAngle(RightTopWing_r1, -0.1745F, 0.0F, 0.0F);
		RightTopWing_r1.setTextureOffset(22, 0).addBox(-3.0F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, false);

		LeftWings = new ModelRenderer(this);
		LeftWings.setRotationPoint(0.0F, 0.0F, 0.0F);
		Inner.addChild(LeftWings);
		LeftWings.setTextureOffset(16, 5).addBox(2.0F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		LeftTopWing_r1 = new ModelRenderer(this);
		LeftTopWing_r1.setRotationPoint(2.0F, -3.0F, 0.0F);
		LeftWings.addChild(LeftTopWing_r1);
		setRotationAngle(LeftTopWing_r1, -0.1745F, 0.0F, 0.0F);
		LeftTopWing_r1.setTextureOffset(16, 0).addBox(0.0F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, false);

		Outer = new ModelRenderer(this);
		Outer.setRotationPoint(0.0F, 24.0F, 0.0F);
		Outer.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.Inner, this.Outer);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.LeftWings.rotateAngleY = 0.0F;
		this.RightWings.rotateAngleY = 0.0F;
		boolean flag = entityIn.isOnGround() && entityIn.getMotion().lengthSquared() < 1.0E-7D;
		if (flag) {
			this.LeftWings.rotateAngleY = -0.2618F;
			this.RightWings.rotateAngleY = 0.2618F;
		} else {
			float f = ageInTicks * 2.1F;
			this.RightWings.rotateAngleY = MathHelper.cos(f) * (float)Math.PI * 0.15F;
			this.LeftWings.rotateAngleY = -this.RightWings.rotateAngleY;
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}