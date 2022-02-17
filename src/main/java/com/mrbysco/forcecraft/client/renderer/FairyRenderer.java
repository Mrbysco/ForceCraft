package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.model.FairyModel;
import com.mrbysco.forcecraft.entities.FairyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FairyRenderer extends MobRenderer<FairyEntity, FairyModel<FairyEntity>> {
	private static final ResourceLocation FAIRY_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/fairy.png");

	public FairyRenderer(EntityRendererProvider.Context context) {
		super(context, new FairyModel(context.bakeLayer(ClientHandler.FAIRY)), 0.2F);
	}

	@Override
	public ResourceLocation getTextureLocation(FairyEntity entity) {
		return FAIRY_TEXTURES;
	}
}
