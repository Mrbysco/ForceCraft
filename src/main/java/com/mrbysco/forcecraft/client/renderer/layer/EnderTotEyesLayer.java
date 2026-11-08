package com.mrbysco.forcecraft.client.renderer.layer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.EnderTotModel;
import com.mrbysco.forcecraft.client.state.EnderTotRenderState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class EnderTotEyesLayer extends EyesLayer<EnderTotRenderState, EnderTotModel> {
	private static final RenderType ENDERTOT_EYES = RenderTypes.eyes(Reference.modLoc("textures/entity/ender_tot_eyes.png"));

	public EnderTotEyesLayer(RenderLayerParent<EnderTotRenderState, EnderTotModel> renderer) {
		super(renderer);
	}

	@Override
	public RenderType renderType() {
		return ENDERTOT_EYES;
	}
}
