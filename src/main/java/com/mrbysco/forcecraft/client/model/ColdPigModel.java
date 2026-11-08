package com.mrbysco.forcecraft.client.model;

import com.mrbysco.forcecraft.client.state.ColdPigRenderState;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class ColdPigModel extends PigModel {

	public ColdPigModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(LivingEntityRenderState state) {
		super.setupAnim(state);
		if (state instanceof ColdPigRenderState coldRenderState) {
			this.head.y = this.head.y + coldRenderState.headEatPositionScale * 9.0F * state.ageScale;
			this.head.xRot = coldRenderState.headEatAngleScale;
		}
	}
}
