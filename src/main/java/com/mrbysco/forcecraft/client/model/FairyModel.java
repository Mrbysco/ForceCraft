package com.mrbysco.forcecraft.client.model;

import com.google.common.collect.ImmutableList;
import com.mrbysco.forcecraft.entities.FairyEntity;
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
		super(RenderType::entityTranslucent);
		texWidth = 32;
		texHeight = 32;

		Inner = new ModelRenderer(this);
		Inner.setPos(0.0F, 24.0F, 0.0F);
		Inner.texOffs(0, 8).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		RightWings = new ModelRenderer(this);
		RightWings.setPos(0.0F, 0.0F, 0.0F);
		Inner.addChild(RightWings);
		RightWings.texOffs(22, 5).addBox(-5.0F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		RightTopWing_r1 = new ModelRenderer(this);
		RightTopWing_r1.setPos(-2.0F, -3.0F, 0.0F);
		RightWings.addChild(RightTopWing_r1);
		setRotationAngle(RightTopWing_r1, -0.1745F, 0.0F, 0.0F);
		RightTopWing_r1.texOffs(22, 0).addBox(-3.0F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, false);

		LeftWings = new ModelRenderer(this);
		LeftWings.setPos(0.0F, 0.0F, 0.0F);
		Inner.addChild(LeftWings);
		LeftWings.texOffs(16, 5).addBox(2.0F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		LeftTopWing_r1 = new ModelRenderer(this);
		LeftTopWing_r1.setPos(2.0F, -3.0F, 0.0F);
		LeftWings.addChild(LeftTopWing_r1);
		setRotationAngle(LeftTopWing_r1, -0.1745F, 0.0F, 0.0F);
		LeftTopWing_r1.texOffs(16, 0).addBox(0.0F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, false);

		Outer = new ModelRenderer(this);
		Outer.setPos(0.0F, 24.0F, 0.0F);
		Outer.texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.Inner, this.Outer);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.LeftWings.yRot = 0.0F;
		this.RightWings.yRot = 0.0F;
		boolean flag = entityIn.isOnGround() && entityIn.getDeltaMovement().lengthSqr() < 1.0E-7D;
		if (flag) {
			this.LeftWings.yRot = -0.2618F;
			this.RightWings.yRot = 0.2618F;
		} else {
			float f = ageInTicks * 2.1F;
			this.RightWings.yRot = MathHelper.cos(f) * (float) Math.PI * 0.15F;
			this.LeftWings.yRot = -this.RightWings.yRot;
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}