package mrbysco.forcecraft.client.renderer;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.model.FairyModel;
import mrbysco.forcecraft.entities.FairyEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class FairyRenderer extends MobRenderer<FairyEntity, FairyModel<FairyEntity>> {
	private static final ResourceLocation FAIRY_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/fairy.png");

	public FairyRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new FairyModel<>(), 0.2F);
	}

	@Override
	public ResourceLocation getEntityTexture(FairyEntity entity) {
		return FAIRY_TEXTURES;
	}
}
