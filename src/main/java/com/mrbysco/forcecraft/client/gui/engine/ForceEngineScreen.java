package com.mrbysco.forcecraft.client.gui.engine;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentities.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.client.util.RenderHelper;
import com.mrbysco.forcecraft.menu.engine.ForceEngineMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ForceEngineScreen extends AbstractContainerScreen<ForceEngineMenu> {
	private final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/force_engine.png");

	public ForceEngineScreen(ForceEngineMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
		guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

		this.drawFuelTank(guiGraphics);
		this.drawThrottleTank(guiGraphics);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);

		ForceEngineBlockEntity tile = getMenu().getTile();
		if (isHovering(66, 11, 16, 58, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (tile.getFuelFluid() == null) {
				text.add(Component.translatable("gui.forcecraft.force_engine.empty"));
			} else {
				if (tile.getFuelFluidStack() != null) {
					text.add(tile.getFuelFluidStack().getDisplayName());
					text.add(Component.literal(tile.getFuelAmount() + " mb")
							.withStyle(ChatFormatting.GOLD));
				}
			}

			guiGraphics.renderComponentTooltip(font, text, actualMouseX, actualMouseY);
		}

		if (isHovering(94, 11, 16, 58, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (tile.getThrottleFluid() == null) {
				text.add(Component.translatable("gui.forcecraft.force_engine.empty"));
			} else {
				if (tile.getThrottleFluidStack() != null) {
					text.add(tile.getThrottleFluidStack().getDisplayName());
					text.add(Component.literal(tile.getThrottleAmount() + " mb")
							.withStyle(ChatFormatting.GOLD));
				}
			}

			guiGraphics.renderComponentTooltip(font, text, actualMouseX, actualMouseY);
		}
	}

	private void drawFuelTank(GuiGraphics guiGraphics) {
		if (menu.getTile() == null || menu.getTile().getFuelFluid() == null) {
			return;
		}
		FluidStack fluidStack = menu.getTile().getFuelFluidStack();
		float tankPercentage = RenderHelper.getTankPercentage(getMenu().getTile().getFuelAmount(), 10000);
		RenderHelper.drawFluidTankInGUI(fluidStack, leftPos + 66, topPos + 11, tankPercentage, 58);

		guiGraphics.blit(TEXTURE, leftPos + 66, topPos + 11, 176, 0, 16, 64);
	}

	private void drawThrottleTank(GuiGraphics guiGraphics) {
		if (menu.getTile() == null || menu.getTile().getThrottleFluid() == null) {
			return;
		}
		FluidStack fluidStack = menu.getTile().getThrottleFluidStack();
		float tankPercentage = RenderHelper.getTankPercentage(getMenu().getTile().getThrottleAmount(), 10000);
		RenderHelper.drawFluidTankInGUI(fluidStack, leftPos + 94, topPos + 11, tankPercentage, 58);

		guiGraphics.blit(TEXTURE, leftPos + 94, topPos + 11, 176, 0, 16, 64);
	}

}
