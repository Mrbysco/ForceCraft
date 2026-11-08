package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.client.state.ColdCowRenderState;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class ColdCowModel extends CowModel {

	public ColdCowModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(LivingEntityRenderState state) {
		super.setupAnim(state);
		if (state instanceof ColdCowRenderState coldRenderState) {
			this.head.y = this.head.y + coldRenderState.headEatPositionScale * 9.0F * state.ageScale;
			this.head.xRot = coldRenderState.headEatAngleScale;
		}
	}
}
