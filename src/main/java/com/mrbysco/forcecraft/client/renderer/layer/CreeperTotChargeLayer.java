package com.mrbysco.forcecraft.client.renderer.layer;

import com.mrbysco.forcecraft.client.model.CreeperTotModel;
import com.mrbysco.forcecraft.entities.CreeperTotEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class CreeperTotChargeLayer extends EnergySwirlLayer<CreeperTotEntity, CreeperTotModel<CreeperTotEntity>> {
	private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final CreeperTotModel<CreeperTotEntity> creeperModel;

	public CreeperTotChargeLayer(RenderLayerParent<CreeperTotEntity, CreeperTotModel<CreeperTotEntity>> renderLayerParent, EntityModelSet modelSet) {
		super(renderLayerParent);
		this.creeperModel = new CreeperTotModel<>(modelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
	}

	protected float xOffset(float p_225634_1_) {
		return p_225634_1_ * 0.01F;
	}

	protected ResourceLocation getTextureLocation() {
		return LIGHTNING_TEXTURE;
	}

	protected EntityModel<CreeperTotEntity> model() {
		return this.creeperModel;
	}
}