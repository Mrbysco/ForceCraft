package mrbysco.forcecraft.client.renderer;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.model.ColdChickenModel;
import mrbysco.forcecraft.entities.ColdChickenEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ColdChickenRenderer extends MobRenderer<ColdChickenEntity, ColdChickenModel<ColdChickenEntity>> {
	private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/cold_chicken.png");

	public ColdChickenRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new ColdChickenModel<>(), 0.3F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(ColdChickenEntity entity) {
		return CHICKEN_TEXTURES;
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(ColdChickenEntity livingBase, float partialTicks) {
		float f = MathHelper.lerp(partialTicks, livingBase.oFlap, livingBase.wingRotation);
		float f1 = MathHelper.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.destPos);
		return (MathHelper.sin(f) + 1.0F) * f1;
	}
}
