package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.resources.ResourceLocation;

public class RedChuChuRenderer extends SlimeRenderer {
	private static final ResourceLocation CHU_CHU_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/red_chu_chu.png");

	public RedChuChuRenderer(EntityRenderDispatcher renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public ResourceLocation getTextureLocation(Slime entity) {
		return CHU_CHU_TEXTURE;
	}
}
