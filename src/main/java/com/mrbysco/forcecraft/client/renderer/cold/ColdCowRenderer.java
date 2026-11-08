package com.mrbysco.forcecraft.client.renderer.cold;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.ColdCowModel;
import com.mrbysco.forcecraft.client.state.ColdCowRenderState;
import com.mrbysco.forcecraft.entity.cold.ColdCow;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class ColdCowRenderer extends MobRenderer<ColdCow, ColdCowRenderState, ColdCowModel> {
	private static final Identifier COW_TEXTURES = Reference.modLoc("textures/entity/cold_cow.png");

	public ColdCowRenderer(EntityRendererProvider.Context context) {
		super(context, new ColdCowModel(context.bakeLayer(ModelLayers.COW)), 0.7F);
	}

	@Override
	public Identifier getTextureLocation(ColdCowRenderState state) {
		return COW_TEXTURES;
	}

	@Override
	public ColdCowRenderState createRenderState() {
		return new ColdCowRenderState();
	}

	@Override
	public void extractRenderState(ColdCow entity, ColdCowRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.variant = entity.getVariant().value();
		state.headEatAngleScale = entity.getHeadEatAngleScale(entity.getXRot(partialTicks), partialTicks);
		state.headEatPositionScale = entity.getHeadEatPositionScale(partialTicks);
	}
}
