package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.FairyModel;
import com.mrbysco.forcecraft.entities.FairyEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FairyRenderer extends MobRenderer<FairyEntity, FairyModel<FairyEntity>> {
	private static final ResourceLocation FAIRY_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/fairy.png");

	public FairyRenderer(EntityRenderDispatcher renderManagerIn) {
		super(renderManagerIn, new FairyModel<>(), 0.2F);
	}

	@Override
	public ResourceLocation getTextureLocation(FairyEntity entity) {
		return FAIRY_TEXTURES;
	}
}
