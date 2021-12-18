package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.ColdChickenModel;
import com.mrbysco.forcecraft.entities.ColdChickenEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ColdChickenRenderer extends MobRenderer<ColdChickenEntity, ColdChickenModel<ColdChickenEntity>> {
	private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/cold_chicken.png");

	public ColdChickenRenderer(EntityRenderDispatcher renderManagerIn) {
		super(renderManagerIn, new ColdChickenModel<>(), 0.3F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(ColdChickenEntity entity) {
		return CHICKEN_TEXTURES;
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float getBob(ColdChickenEntity livingBase, float partialTicks) {
		float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
		float f1 = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
		return (Mth.sin(f) + 1.0F) * f1;
	}
}
