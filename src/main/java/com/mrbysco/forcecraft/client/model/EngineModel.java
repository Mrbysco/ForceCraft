package com.mrbysco.forcecraft.client.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class EngineModel {
	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("piston", CubeListBuilder.create().texOffs(0, 0)
				.addBox(0, 0, 0, 12, 4, 12), PartPose.offset(2, 6, 2));
		root.addOrReplaceChild("extension", CubeListBuilder.create().texOffs(0, 0)
				.addBox(0, 0, 0, 10, 2, 10), PartPose.offset(3, 5, 3));

		return LayerDefinition.create(mesh, 64, 32);
	}

}
