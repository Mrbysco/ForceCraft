package com.mrbysco.forcecraft.client.renderer.layer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.EnderTotModel;
import com.mrbysco.forcecraft.entities.EnderTotEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class EndertotEyesLayer<T extends EnderTotEntity> extends EyesLayer<T, EnderTotModel<T>> {
	private static final RenderType RENDER_TYPE = RenderType.eyes(new ResourceLocation(Reference.MOD_ID, "textures/entity/ender_tot_eyes.png"));

	public EndertotEyesLayer(RenderLayerParent<T, EnderTotModel<T>> rendererIn) {
		super(rendererIn);
	}

	public RenderType renderType() {
		return RENDER_TYPE;
	}
}