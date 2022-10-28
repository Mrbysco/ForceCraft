package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.FairyModel;
import com.mrbysco.forcecraft.entities.FairyEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class FairyRenderer extends MobRenderer<FairyEntity, FairyModel<FairyEntity>> {
	private static final ResourceLocation FAIRY_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/fairy.png");

	public FairyRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new FairyModel<>(), 0.2F);
	}

	@Override
	public ResourceLocation getTextureLocation(FairyEntity entity) {
		return FAIRY_TEXTURES;
	}
}
