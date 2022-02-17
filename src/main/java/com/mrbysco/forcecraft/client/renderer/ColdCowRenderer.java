package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.ColdCowModel;
import com.mrbysco.forcecraft.entities.ColdCowEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ColdCowRenderer extends MobRenderer<ColdCowEntity, ColdCowModel<ColdCowEntity>> {
	private static final ResourceLocation COW_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/cold_cow.png");

	public ColdCowRenderer(EntityRendererProvider.Context context) {
		super(context, new ColdCowModel(context.bakeLayer(ModelLayers.COW)), 0.7F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(ColdCowEntity entity) {
		return COW_TEXTURES;
	}
}
