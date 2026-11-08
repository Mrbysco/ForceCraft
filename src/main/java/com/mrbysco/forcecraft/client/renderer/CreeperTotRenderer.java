package com.mrbysco.forcecraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.model.CreeperTotModel;
import com.mrbysco.forcecraft.client.renderer.layer.CreeperTotPowerLayer;
import com.mrbysco.forcecraft.client.state.CreeperTotRenderState;
import com.mrbysco.forcecraft.entity.CreeperTot;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class CreeperTotRenderer extends MobRenderer<CreeperTot, CreeperTotRenderState, CreeperTotModel> {
	private static final Identifier CREEPER_TEXTURES = Reference.modLoc("textures/entity/creeper_tot.png");

	public CreeperTotRenderer(EntityRendererProvider.Context context) {
		super(context, new CreeperTotModel(context.bakeLayer(ClientHandler.CREEPER_TOT)), 0.2F);
		this.addLayer(new CreeperTotPowerLayer(this, context.getModelSet()));
	}

	@Override
	protected void scale(CreeperTotRenderState state, PoseStack poseStack) {
		float g = state.swelling;
		float wobble = 1.0F + Mth.sin(g * 100.0F) * g * 0.01F;
		g = Mth.clamp(g, 0.0F, 1.0F);
		g *= g;
		g *= g;
		float s = (1.0F + g * 0.4F) * wobble;
		float hs = (1.0F + g * 0.1F) / wobble;
		poseStack.scale(s, hs, s);
	}

	@Override
	protected float getWhiteOverlayProgress(CreeperTotRenderState state) {
		float step = state.swelling;
		return (int) (step * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(step, 0.5F, 1.0F);
	}

	@Override
	public Identifier getTextureLocation(CreeperTotRenderState entity) {
		return CREEPER_TEXTURES;
	}

	public CreeperTotRenderState createRenderState() {
		return new CreeperTotRenderState();
	}

	public void extractRenderState(CreeperTot entity, CreeperTotRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.swelling = entity.getSwelling(partialTicks);
		state.isPowered = entity.isPowered();
	}
}