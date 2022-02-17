package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;

public class BlueChuChuRenderer extends SlimeRenderer {
	private static final ResourceLocation CHU_CHU_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/blue_chu_chu.png");

	public BlueChuChuRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Slime entity) {
		return CHU_CHU_TEXTURE;
	}
}
