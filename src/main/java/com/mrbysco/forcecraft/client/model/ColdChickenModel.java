package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.client.state.ColdChickenRenderState;
import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;

public class ColdChickenModel extends ChickenModel {
	private final ModelPart head;

	public ColdChickenModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
	}

	@Override
	public void setupAnim(ChickenRenderState state) {
		super.setupAnim(state);
		if (state instanceof ColdChickenRenderState coldRenderState) {
			this.head.y = this.head.y + coldRenderState.headEatPositionScale * 9.0F * state.ageScale;
			this.head.xRot = coldRenderState.headEatAngleScale;
		}
	}
}
