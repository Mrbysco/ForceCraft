package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;

public class ForceArrowRenderer extends ArrowRenderer<ForceArrowEntity> {
	public static final ResourceLocation FORCE_ARROW = new ResourceLocation(Reference.MOD_ID, "textures/entity/projectiles/force_arrow.png");

	public ForceArrowRenderer(EntityRenderDispatcher manager) {
		super(manager);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(ForceArrowEntity entity) {
		return FORCE_ARROW;
	}
}
