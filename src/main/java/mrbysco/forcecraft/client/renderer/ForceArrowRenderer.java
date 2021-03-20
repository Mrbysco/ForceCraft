package mrbysco.forcecraft.client.renderer;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ForceArrowRenderer extends ArrowRenderer<ForceArrowEntity> {
	public static final ResourceLocation FORCE_ARROW = new ResourceLocation(Reference.MOD_ID, "textures/entity/projectiles/force_arrow.png");

	public ForceArrowRenderer(EntityRendererManager manager) {
		super(manager);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(ForceArrowEntity entity) {
		return FORCE_ARROW;
	}
}
