package com.mrbysco.forcecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;

public class CreeperTotModel<T extends Entity> extends ListModel<T> {
	private final ModelPart Head;
	private final ModelPart HeadWear;
	private final ModelPart Torso;
	private final ModelPart Leg2;
	private final ModelPart Leg4;
	private final ModelPart Leg3;
	private final ModelPart Leg1;

	public CreeperTotModel() {
		this(0.0F);
	}

	public CreeperTotModel(float size) {
		texWidth = 64;
		texHeight = 32;

		Head = new ModelPart(this);
		Head.setPos(0.0F, 14.0F, 0.0F);
		Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, size, false);

		HeadWear = new ModelPart(this);
		HeadWear.setPos(0.0F, 14.0F, 0.0F);
		HeadWear.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, size + 0.5F, false);

		Torso = new ModelPart(this);
		Torso.setPos(0.0F, 20.0F, 0.0F);
		Torso.texOffs(16, 16).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, size, false);

		Leg2 = new ModelPart(this);
		Leg2.setPos(-2.0F, 20.0F, 4.0F);
		Leg2.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, size, false);

		Leg4 = new ModelPart(this);
		Leg4.setPos(-2.0F, 20.0F, -4.0F);
		Leg4.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, size, false);

		Leg3 = new ModelPart(this);
		Leg3.setPos(2.0F, 20.0F, -4.0F);
		Leg3.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, size, false);

		Leg1 = new ModelPart(this);
		Leg1.setPos(2.0F, 20.0F, 4.0F);
		Leg1.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, size, false);
	}

	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.Head, this.Torso, this.Leg1, this.Leg2, this.Leg3, this.Leg4);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		this.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.Head.xRot = headPitch * ((float)Math.PI / 180F);
		this.Leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.Leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.Leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.Leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}
}