package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.client.state.BombRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BombModel extends EntityModel<BombRenderState> {

	public BombModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("bomb", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
						.texOffs(0, 16).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 1.0F, 4.0F)
						.texOffs(16, 16).addBox(-0.5F, -11.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, 24.0F, 0.0F)
		);

		return LayerDefinition.create(meshdefinition, 32, 32);
	}
}