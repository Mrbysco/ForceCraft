package com.mrbysco.forcecraft.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidStateModelSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

public class RenderHelper {
	public static void extractFluidTankInGUI(GuiGraphicsExtractor graphics, FluidStack fluidStack, float x, float y, float percent, int height) {
		if (fluidStack == null || fluidStack.isEmpty())
			return;

		Fluid fluid = fluidStack.getFluid();
		Minecraft minecraft = Minecraft.getInstance();
		ModelManager modelManager = minecraft.getModelManager();
		FluidStateModelSet fluidStateModelSet = modelManager.getFluidStateModelSet();
		FluidModel fluidModel = fluidStateModelSet.get(fluid.defaultFluidState());
		FluidTintSource tintSource = fluidModel.fluidTintSource();
		int tint = tintSource == null ? 0xFFFFFFFF : tintSource.colorAsStack(fluidStack);

		Material.Baked stillMaterial = fluidModel.stillMaterial();
		TextureAtlasSprite sprite = stillMaterial.sprite();

		double tankLevel = percent * height;
		int left = (int) x;
		int bottom = (int) y + height;

		graphics.enableScissor(left, (int) (bottom - tankLevel), left + 16, bottom);
		int count = (int) Math.ceil(tankLevel / 16.0);
		for (int i = 0; i < count; i++) {
			graphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, sprite, left, bottom - 16 * (i + 1), 16, 16, tint);
		}
		graphics.disableScissor();
	}

	public static float getTankPercentage(int fluidAmount, int fluidMax) {
		return (float) fluidAmount / (float) fluidMax;
	}

	public static int getFluidGuiPercentage(int percentage, int maxHeight) {
		return (int) Math.ceil(percentage * (float) maxHeight);
	}
}
