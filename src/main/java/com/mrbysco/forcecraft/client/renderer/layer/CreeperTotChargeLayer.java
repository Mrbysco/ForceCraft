package com.mrbysco.forcecraft.client.renderer.layer;

import com.mrbysco.forcecraft.client.model.CreeperTotModel;
import com.mrbysco.forcecraft.entities.CreeperTotEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class CreeperTotChargeLayer extends EnergyLayer<CreeperTotEntity, CreeperTotModel<CreeperTotEntity>> {
	private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final CreeperTotModel<CreeperTotEntity> creeperModel = new CreeperTotModel<>(2.0F);

	public CreeperTotChargeLayer(IEntityRenderer<CreeperTotEntity, CreeperTotModel<CreeperTotEntity>> p_i50947_1_) {
		super(p_i50947_1_);
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