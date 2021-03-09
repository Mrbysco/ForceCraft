package mrbysco.forcecraft.client.renderer;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.model.ColdPigModel;
import mrbysco.forcecraft.entities.ColdPigEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.util.ResourceLocation;

public class ColdPigRenderer extends MobRenderer<ColdPigEntity, ColdPigModel<ColdPigEntity>> {
	private static final ResourceLocation PIG_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/cold_pig.png");

	public ColdPigRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new ColdPigModel<>(), 0.7F);
		this.addLayer(new SaddleLayer<>(this, new ColdPigModel<>(0.5F), new ResourceLocation("textures/entity/pig/pig_saddle.png")));
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(ColdPigEntity entity) {
		return PIG_TEXTURES;
	}
}