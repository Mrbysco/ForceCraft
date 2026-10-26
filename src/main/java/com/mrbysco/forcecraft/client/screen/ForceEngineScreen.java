package com.mrbysco.forcecraft.client.screen;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentity.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.client.util.RenderHelper;
import com.mrbysco.forcecraft.menu.ForceEngineMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ForceEngineScreen extends AbstractContainerScreen<ForceEngineMenu> {
	private final Identifier TEXTURE = Reference.modLoc("textures/gui/container/force_engine.png");

	public ForceEngineScreen(ForceEngineMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(graphics, mouseX, mouseY, partialTick);
		extractTooltip(graphics, mouseX, mouseY);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

		extractFuelTank(graphics);
		extractThrottleTank(graphics);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		ForceEngineBlockEntity blockEntity = getMenu().getBlockEntity();
		if (isHovering(66, 11, 16, 58, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (blockEntity.getTank().getResource(0).isEmpty()) {
				text.add(Component.translatable("gui.forcecraft.force_engine.empty"));
			} else {
				text.add(blockEntity.getTank().getResource(0).getHoverName());
				text.add(Component.literal(blockEntity.getTank().getAmountAsInt(0) + " mb")
						.withStyle(ChatFormatting.GOLD));
			}

			guiGraphics.setComponentTooltipForNextFrame(font, text, mouseX, mouseY);
		}

		if (isHovering(94, 11, 16, 58, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (blockEntity.getTank().getResource(1).isEmpty()) {
				text.add(Component.translatable("gui.forcecraft.force_engine.empty"));
			} else {
				text.add(blockEntity.getTank().getResource(1).getHoverName());
				text.add(Component.literal(blockEntity.getTank().getAmountAsInt(1) + " mb")
						.withStyle(ChatFormatting.GOLD));
			}

			guiGraphics.setComponentTooltipForNextFrame(font, text, mouseX, mouseY);
		}
	}

	private void extractFuelTank(GuiGraphicsExtractor graphics) {
		if (menu.getBlockEntity() == null || menu.getBlockEntity().getTank().getResource(0).isEmpty()) {
			return;
		}
		var tank = menu.getBlockEntity().getTank();
		FluidStack fluidStack = tank.getResource(0).toStack(tank.getAmountAsInt(0));
		float tankPercentage = RenderHelper.getTankPercentage(tank.getAmountAsInt(0), 10000);
		RenderHelper.extractFluidTankInGUI(graphics, fluidStack, leftPos + 66, topPos + 11, tankPercentage, 58);

		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 66, topPos + 11, 176, 0, 16, 64, 256, 256);
	}

	private void extractThrottleTank(GuiGraphicsExtractor graphics) {
		if (menu.getBlockEntity() == null || menu.getBlockEntity().getTank().getResource(1).isEmpty()) {
			return;
		}
		var tank = menu.getBlockEntity().getTank();
		FluidStack fluidStack = tank.getResource(1).toStack(tank.getAmountAsInt(1));
		float tankPercentage = RenderHelper.getTankPercentage(tank.getAmountAsInt(1), 10000);
		RenderHelper.extractFluidTankInGUI(graphics, fluidStack, leftPos + 94, topPos + 11, tankPercentage, 58);

		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 94, topPos + 11, 176, 0, 16, 64, 256, 256);
	}
}
