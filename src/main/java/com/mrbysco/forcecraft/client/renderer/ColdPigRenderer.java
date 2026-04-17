package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.ColdPigModel;
import com.mrbysco.forcecraft.entities.ColdPigEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.Identifier;

public class ColdPigRenderer extends MobRenderer<ColdPigEntity, ColdPigModel<ColdPigEntity>> {
	private static final Identifier PIG_TEXTURES = Reference.modLoc("textures/entity/cold_pig.png");

	public ColdPigRenderer(EntityRendererProvider.Context context) {
		super(context, new ColdPigModel<>(context.bakeLayer(ModelLayers.PIG)), 0.7F);
		this.addLayer(new SaddleLayer<>(this, new ColdPigModel<>(context.bakeLayer(ModelLayers.PIG_SADDLE)),
				Identifier.withDefaultNamespace("textures/entity/pig/pig_saddle.png")));
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public Identifier getTextureLocation(ColdPigEntity entity) {
		return PIG_TEXTURES;
	}
}