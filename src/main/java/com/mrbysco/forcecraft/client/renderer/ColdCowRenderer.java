package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.ColdCowModel;
import com.mrbysco.forcecraft.entities.ColdCowEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class ColdCowRenderer extends MobRenderer<ColdCowEntity, ColdCowModel<ColdCowEntity>> {
	private static final ResourceLocation COW_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/cold_cow.png");

	public ColdCowRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new ColdCowModel<>(), 0.7F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(ColdCowEntity entity) {
		return COW_TEXTURES;
	}
}
