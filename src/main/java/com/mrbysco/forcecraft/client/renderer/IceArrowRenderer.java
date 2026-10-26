package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.client.state.IceArrowRenderState;
import com.mrbysco.forcecraft.entity.ForceArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class IceArrowRenderer extends ArrowRenderer<ForceArrow, IceArrowRenderState> {
	public static final Identifier ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");

	public IceArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	protected Identifier getTextureLocation(IceArrowRenderState state) {
		return ARROW_LOCATION;
	}

	public IceArrowRenderState createRenderState() {
		return new IceArrowRenderState();
	}

	public void extractRenderState(ForceArrow entity, IceArrowRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}
}
