package com.mrbysco.forcecraft.client.gui.engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.util.RenderHelper;
import com.mrbysco.forcecraft.container.engine.ForceEngineContainer;
import com.mrbysco.forcecraft.tiles.ForceEngineTile;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class ForceEngineScreen extends AbstractContainerScreen<ForceEngineContainer> {
	private final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/force_engine.png");

	public ForceEngineScreen(ForceEngineContainer screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		this.minecraft.getTextureManager().bind(TEXTURE);
		this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

		this.drawFuelTank(matrixStack);
		this.drawThrottleTank(matrixStack);
	}

	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);

		ForceEngineTile tile = getMenu().getTile();
		if (isHovering(66, 11, 16, 58, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (tile.getFuelFluid() == null) {
				text.add(new TranslatableComponent("gui.forcecraft.force_engine.empty"));
			} else {
				if(tile.getFuelFluidStack() != null) {
					text.add(tile.getFuelFluidStack().getDisplayName());
					text.add(new TextComponent(tile.getFuelAmount() + " mb")
							.withStyle(ChatFormatting.GOLD));
				}
			}

			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isHovering(94, 11, 16, 58, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (tile.getThrottleFluid() == null) {
				text.add(new TranslatableComponent("gui.forcecraft.force_engine.empty"));
			} else {
				if(tile.getThrottleFluidStack() != null) {
					text.add(tile.getThrottleFluidStack().getDisplayName());
					text.add(new TextComponent(tile.getThrottleAmount() + " mb")
							.withStyle(ChatFormatting.GOLD));
				}
			}

			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}
	}

	private void drawFuelTank(PoseStack matrixStack) {
		if (menu.getTile() == null || menu.getTile().getFuelFluid() == null) {
			return;
		}
		FluidStack fluidStack = menu.getTile().getFuelFluidStack();
		float tankPercentage = RenderHelper.getTankPercentage(getMenu().getTile().getFuelAmount(), 10000);
		RenderHelper.drawFluidTankInGUI(fluidStack, leftPos + 66, topPos + 11, tankPercentage, 58);

		minecraft.textureManager.bind(TEXTURE);
		blit(matrixStack, leftPos + 66, topPos + 11, 176, 0, 16, 64);
	}

	private void drawThrottleTank(PoseStack matrixStack) {
		if (menu.getTile() == null || menu.getTile().getThrottleFluid() == null) {
			return;
		}
		FluidStack fluidStack = menu.getTile().getThrottleFluidStack();
		float tankPercentage = RenderHelper.getTankPercentage(getMenu().getTile().getThrottleAmount(), 10000);
		RenderHelper.drawFluidTankInGUI(fluidStack, leftPos + 94, topPos + 11, tankPercentage, 58);

		minecraft.textureManager.bind(TEXTURE);
		blit(matrixStack, leftPos + 94, topPos + 11, 176, 0, 16, 64);
	}

}
