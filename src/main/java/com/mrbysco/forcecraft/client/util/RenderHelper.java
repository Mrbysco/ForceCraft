package com.mrbysco.forcecraft.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.awt.*;

public class RenderHelper {
	public static void drawFluidTankInGUI(FluidStack fluid, float x, float y, float percent, int height) {
		if (fluid == null || fluid.isEmpty())
			return;

		Identifier flowing = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);

		AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS);
		if (texture instanceof TextureAtlas) {
			TextureAtlasSprite sprite = ((TextureAtlas) texture).getSprite(flowing);
			if (sprite != null) {
				float minU = sprite.getU0();
				float maxU = sprite.getU1();
				float minV = sprite.getV0();
				float maxV = sprite.getV1();
				float deltaV = maxV - minV;
				double tankLevel = percent * height;

				RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

				Color color = new Color(IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid));

				RenderSystem.setShaderColor((float) color.getRed() / 255.0F,
						(float) color.getGreen() / 255.0F,
						(float) color.getBlue() / 255.0F,
						(float) color.getAlpha() / 255.0F);
				RenderSystem.enableBlend();
				int count = 1 + ((int) Math.ceil(tankLevel)) / 16;
				for (int i = 0; i < count; i++) {
					double subHeight = Math.min(16.0, tankLevel - (16.0 * i));
					double offsetY = height - 16.0 * i - subHeight;
					drawQuad(x, (float) (y + offsetY), 16F, (float) subHeight, minU, (float) (maxV - deltaV * (subHeight / 16.0)), maxU, maxV);
				}
				RenderSystem.disableBlend();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	private static void drawQuad(float x, float y, float width, float height, float minU, float minV, float maxU, float maxV) {
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder buffer = tesselator.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.addVertex(x, y + height, 0).setUv(minU, maxV);
		buffer.addVertex(x + width, y + height, 0).setUv(maxU, maxV);
		buffer.addVertex(x + width, y, 0).setUv(maxU, minV);
		buffer.addVertex(x, y, 0).setUv(minU, minV);
		BufferUploader.drawWithShader(buffer.buildOrThrow());
	}

	public static float getTankPercentage(int fluidAmount, int fluidMax) {
		return (float) fluidAmount / (float) fluidMax;
	}

	public static int getFluidGuiPercentage(int percentage, int maxHeight) {
		return (int) Math.ceil(percentage * (float) maxHeight);
	}
}
