package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.entities.FairyEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class FairyModel<T extends FairyEntity> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart inner;
	private final ModelPart leftWings;
	private final ModelPart rightWings;

	public FairyModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.root = root;
		this.inner = root.getChild("inner");
		this.leftWings = inner.getChild("left_wings");
		this.rightWings = inner.getChild("right_wings");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition inner = partdefinition.addOrReplaceChild("inner", CubeListBuilder.create()
						.texOffs(0, 8).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leftWings = inner.addOrReplaceChild("left_wings", CubeListBuilder.create()
						.texOffs(16, 5).addBox(2.0F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F),
				PartPose.ZERO);

		leftWings.addOrReplaceChild("left_wings_top", CubeListBuilder.create()
						.texOffs(16, 0).addBox(0.0F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F),
				PartPose.offsetAndRotation(2.0F, -3.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition rightWings = inner.addOrReplaceChild("right_wings", CubeListBuilder.create()
						.texOffs(22, 5).addBox(-5.0F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F),
				PartPose.ZERO);

		rightWings.addOrReplaceChild("right_wings_top", CubeListBuilder.create()
						.texOffs(22, 0).addBox(-3.0F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F),
				PartPose.offsetAndRotation(-2.0F, -3.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("outer", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 64);
	}

	@Override
	public ModelPart root() {
		return root;
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.leftWings.yRot = 0.0F;
		this.rightWings.yRot = 0.0F;
		boolean flag = entityIn.onGround() && entityIn.getDeltaMovement().lengthSqr() < 1.0E-7D;
		if (flag) {
			this.leftWings.yRot = -0.2618F;
			this.rightWings.yRot = 0.2618F;
		} else {
			float f = ageInTicks * 2.1F;
			this.rightWings.yRot = Mth.cos(f) * (float) Math.PI * 0.15F;
			this.leftWings.yRot = -this.rightWings.yRot;
		}
	}
}