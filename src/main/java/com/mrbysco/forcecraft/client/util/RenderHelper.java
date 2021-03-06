package com.mrbysco.forcecraft.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class RenderHelper {
	public static void drawFluidTankInGUI(FluidStack fluid, double x, double y, double percent, int height) {
		if(fluid == null || fluid.isEmpty())
			return;

		ResourceLocation flowing = fluid.getFluid().getAttributes().getStillTexture(fluid);

		Texture texture = Minecraft.getInstance().getTextureManager().getTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
		if (texture instanceof AtlasTexture) {
			TextureAtlasSprite sprite = ((AtlasTexture) texture).getSprite(flowing);
			if (sprite != null) {
				float minU = sprite.getMinU();
				float maxU = sprite.getMaxU();
				float minV = sprite.getMinV();
				float maxV = sprite.getMaxV();
				float deltaV = maxV - minV;
				double tankLevel = percent * height;

				Minecraft.getInstance().textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);

				Color color = new Color(fluid.getFluid()
						.getAttributes()
						.getColor());

				RenderSystem.color4f(
						(float) color.getRed() / 255.0F,
						(float) color.getGreen() / 255.0F,
						(float) color.getBlue() / 255.0F,
						(float) color.getAlpha() / 255.0F
				);
				RenderSystem.enableBlend();
				int count = 1 + ((int) Math.ceil(tankLevel)) / 16;
				for(int i = 0; i < count; i++) {
					double subHeight = Math.min(16.0, tankLevel - (16.0 * i));
					double offsetY = height - 16.0 * i - subHeight;
					drawQuad(x, y + offsetY, 16, subHeight, minU, (float) (maxV - deltaV * (subHeight / 16.0)), maxU, maxV);
				}
				RenderSystem.disableBlend();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	private static void drawQuad(double x, double y, double width, double height, float minU, float minV, float maxU, float maxV) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y + height, 0).tex(minU, maxV).endVertex();
		buffer.pos(x + width, y + height, 0).tex(maxU, maxV).endVertex();
		buffer.pos(x + width, y, 0).tex(maxU, minV).endVertex();
		buffer.pos(x, y, 0).tex(minU, minV).endVertex();
		tessellator.draw();
	}

	public static float getTankPercentage(int fluidAmount, int fluidMax) {
		return (float) fluidAmount / (float) fluidMax;
	}

	public static int getFluidGuiPercentage(int percentage, int maxHeight) {
		return (int) Math.ceil(percentage * (float) maxHeight);
	}
}
