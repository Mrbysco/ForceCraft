package com.mrbysco.forcecraft.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.CreeperTotModel;
import com.mrbysco.forcecraft.client.renderer.layer.CreeperTotChargeLayer;
import com.mrbysco.forcecraft.entities.CreeperTotEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class CreeperTotRenderer extends MobRenderer<CreeperTotEntity, CreeperTotModel<CreeperTotEntity>> {
	private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/creeper_tot.png");

	public CreeperTotRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new CreeperTotModel<>(), 0.2F);
		this.addLayer(new CreeperTotChargeLayer(this));
	}

	protected void scale(CreeperTotEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		float f = entitylivingbaseIn.getSwelling(partialTickTime);
		float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		matrixStackIn.scale(f2, f3, f2);
	}

	protected float getWhiteOverlayProgress(CreeperTotEntity livingEntityIn, float partialTicks) {
		float f = livingEntityIn.getSwelling(partialTicks);
		return (int) (f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(CreeperTotEntity entity) {
		return CREEPER_TEXTURES;
	}
}
