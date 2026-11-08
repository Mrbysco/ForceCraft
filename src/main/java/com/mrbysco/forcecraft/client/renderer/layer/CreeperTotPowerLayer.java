package com.mrbysco.forcecraft.client.renderer.layer;

import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.model.CreeperTotModel;
import com.mrbysco.forcecraft.client.state.CreeperTotRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.monster.creeper.CreeperModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.Identifier;

public class CreeperTotPowerLayer extends EnergySwirlLayer<CreeperTotRenderState, CreeperTotModel> {
	private static final Identifier POWER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");
	private final CreeperTotModel model;

	public CreeperTotPowerLayer(RenderLayerParent<CreeperTotRenderState, CreeperTotModel> renderer, EntityModelSet modelSet) {
		super(renderer);
		this.model = new CreeperTotModel(modelSet.bakeLayer(ClientHandler.CREEPER_TOT_ARMOR));
	}

	@Override
	protected boolean isPowered(CreeperTotRenderState state) {
		return state.isPowered;
	}

	@Override
	protected float xOffset(float t) {
		return t * 0.01F;
	}

	@Override
	protected Identifier getTextureLocation() {
		return POWER_LOCATION;
	}

	@Override
	protected CreeperTotModel model() {
		return this.model;
	}
}
