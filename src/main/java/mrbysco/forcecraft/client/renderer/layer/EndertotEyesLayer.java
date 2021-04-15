package mrbysco.forcecraft.client.renderer.layer;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.model.EnderTotModel;
import mrbysco.forcecraft.entities.EnderTotEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.util.ResourceLocation;

public class EndertotEyesLayer<T extends EnderTotEntity> extends AbstractEyesLayer<T, EnderTotModel<T>> {
	private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation(Reference.MOD_ID, "textures/entity/ender_tot_eyes.png"));

	public EndertotEyesLayer(IEntityRenderer<T, EnderTotModel<T>> rendererIn) {
		super(rendererIn);
	}

	public RenderType getRenderType() {
		return RENDER_TYPE;
	}
}