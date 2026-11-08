package com.mrbysco.forcecraft.client.renderer.cold;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.state.ColdChickenRenderState;
import com.mrbysco.forcecraft.entity.cold.ColdChicken;
import net.minecraft.client.model.animal.chicken.ColdChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class ColdChickenRenderer extends MobRenderer<ColdChicken, ColdChickenRenderState, ColdChickenModel> {
	private static final Identifier CHICKEN_TEXTURES = Reference.modLoc("textures/entity/cold_chicken.png");

	public ColdChickenRenderer(EntityRendererProvider.Context context) {
		super(context, new ColdChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
	}

	@Override
	public Identifier getTextureLocation(ColdChickenRenderState state) {
		return CHICKEN_TEXTURES;
	}

	@Override
	public ColdChickenRenderState createRenderState() {
		return new ColdChickenRenderState();
	}

	@Override
	public void extractRenderState(ColdChicken entity, ColdChickenRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
		state.flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
		state.variant = entity.getVariant().value();
		state.headEatAngleScale = entity.getHeadEatAngleScale(entity.getXRot(partialTicks), partialTicks);
		state.headEatPositionScale = entity.getHeadEatPositionScale(partialTicks);
	}
}
