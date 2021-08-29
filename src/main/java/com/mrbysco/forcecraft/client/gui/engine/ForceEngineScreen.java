package com.mrbysco.forcecraft.client.gui.engine;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.util.RenderHelper;
import com.mrbysco.forcecraft.container.engine.ForceEngineContainer;
import com.mrbysco.forcecraft.tiles.ForceEngineTile;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class ForceEngineScreen extends ContainerScreen<ForceEngineContainer> {
	private final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/force_engine.png");

	public ForceEngineScreen(ForceEngineContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		this.drawFuelTank(matrixStack);
		this.drawThrottleTank(matrixStack);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
		int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

		ForceEngineTile tile = getContainer().getTile();
		if (isPointInRegion(66, 11, 16, 58, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			if (tile.getFuelFluid() == null) {
				text.add(new TranslationTextComponent("gui.forcecraft.force_engine.empty"));
			} else {
				if(tile.getFuelFluidStack() != null) {
					text.add(tile.getFuelFluidStack().getDisplayName());
					text.add(new StringTextComponent(tile.getFuelAmount() + " mb")
							.mergeStyle(TextFormatting.GOLD));
				}
			}

			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isPointInRegion(94, 11, 16, 58, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			if (tile.getThrottleFluid() == null) {
				text.add(new TranslationTextComponent("gui.forcecraft.force_engine.empty"));
			} else {
				if(tile.getThrottleFluidStack() != null) {
					text.add(tile.getThrottleFluidStack().getDisplayName());
					text.add(new StringTextComponent(tile.getThrottleAmount() + " mb")
							.mergeStyle(TextFormatting.GOLD));
				}
			}

			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}
	}

	private void drawFuelTank(MatrixStack matrixStack) {
		if (container.getTile() == null || container.getTile().getFuelFluid() == null) {
			return;
		}
		FluidStack fluidStack = container.getTile().getFuelFluidStack();
		float tankPercentage = RenderHelper.getTankPercentage(getContainer().getTile().getFuelAmount(), 10000);
		RenderHelper.drawFluidTankInGUI(fluidStack, guiLeft + 66, guiTop + 11, tankPercentage, 58);

		minecraft.textureManager.bindTexture(TEXTURE);
		blit(matrixStack, guiLeft + 66, guiTop + 11, 176, 0, 16, 64);
	}

	private void drawThrottleTank(MatrixStack matrixStack) {
		if (container.getTile() == null || container.getTile().getThrottleFluid() == null) {
			return;
		}
		FluidStack fluidStack = container.getTile().getThrottleFluidStack();
		float tankPercentage = RenderHelper.getTankPercentage(getContainer().getTile().getThrottleAmount(), 10000);
		RenderHelper.drawFluidTankInGUI(fluidStack, guiLeft + 94, guiTop + 11, tankPercentage, 58);

		minecraft.textureManager.bindTexture(TEXTURE);
		blit(matrixStack, guiLeft + 94, guiTop + 11, 176, 0, 16, 64);
	}

}
