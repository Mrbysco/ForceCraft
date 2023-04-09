package com.mrbysco.forcecraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.model.CreeperTotModel;
import com.mrbysco.forcecraft.client.renderer.layer.CreeperTotChargeLayer;
import com.mrbysco.forcecraft.entities.CreeperTotEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CreeperTotRenderer extends MobRenderer<CreeperTotEntity, CreeperTotModel<CreeperTotEntity>> {
	private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/creeper_tot.png");

	public CreeperTotRenderer(EntityRendererProvider.Context context) {
		super(context, new CreeperTotModel(context.bakeLayer(ClientHandler.CREEPER_TOT)), 0.2F);
		this.addLayer(new CreeperTotChargeLayer(this, context.getModelSet()));
	}

	protected void scale(CreeperTotEntity entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
		float f = entitylivingbaseIn.getSwelling(partialTickTime);
		float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
		f = Mth.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		poseStack.scale(f2, f3, f2);
	}

	protected float getWhiteOverlayProgress(CreeperTotEntity livingEntityIn, float partialTicks) {
		float f = livingEntityIn.getSwelling(partialTicks);
		return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(CreeperTotEntity entity) {
		return CREEPER_TEXTURES;
	}
}
