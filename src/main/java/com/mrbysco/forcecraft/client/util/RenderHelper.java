package com.mrbysco.forcecraft.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.awt.Color;

public class RenderHelper {
	public static void drawFluidTankInGUI(FluidStack fluid, double x, double y, double percent, int height) {
		if (fluid == null || fluid.isEmpty())
			return;

		ResourceLocation flowing = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);

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
					drawQuad(x, y + offsetY, 16, subHeight, minU, (float) (maxV - deltaV * (subHeight / 16.0)), maxU, maxV);
				}
				RenderSystem.disableBlend();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	private static void drawQuad(double x, double y, double width, double height, float minU, float minV, float maxU, float maxV) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder buffer = tessellator.getBuilder();
		buffer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.vertex(x, y + height, 0).uv(minU, maxV).endVertex();
		buffer.vertex(x + width, y + height, 0).uv(maxU, maxV).endVertex();
		buffer.vertex(x + width, y, 0).uv(maxU, minV).endVertex();
		buffer.vertex(x, y, 0).uv(minU, minV).endVertex();
		tessellator.end();
	}

	public static float getTankPercentage(int fluidAmount, int fluidMax) {
		return (float) fluidAmount / (float) fluidMax;
	}

	public static int getFluidGuiPercentage(int percentage, int maxHeight) {
		return (int) Math.ceil(percentage * (float) maxHeight);
	}
}
