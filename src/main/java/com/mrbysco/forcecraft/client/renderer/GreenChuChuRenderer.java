package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;

public class GreenChuChuRenderer extends SlimeRenderer {
	private static final ResourceLocation CHU_CHU_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/green_chu_chu.png");

	public GreenChuChuRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Slime entity) {
		return CHU_CHU_TEXTURE;
	}
}
