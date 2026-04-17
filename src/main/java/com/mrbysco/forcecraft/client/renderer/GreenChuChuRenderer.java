package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.Slime;

public class GreenChuChuRenderer extends SlimeRenderer {
	private static final Identifier CHU_CHU_TEXTURE = Reference.modLoc("textures/entity/green_chu_chu.png");

	public GreenChuChuRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public Identifier getTextureLocation(Slime entity) {
		return CHU_CHU_TEXTURE;
	}
}
