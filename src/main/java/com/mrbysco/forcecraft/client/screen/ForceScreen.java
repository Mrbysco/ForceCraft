package com.mrbysco.forcecraft.client.screen;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.util.RenderHelper;
import com.mrbysco.forcecraft.menu.ForceMenu;
import com.mrbysco.forcecraft.registry.ForceFluids;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ForceScreen extends AbstractContainerScreen<ForceMenu> {
	private final Identifier TEXTURE = Reference.modLoc("textures/gui/container/force.png");

	public ForceScreen(ForceMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title, 176, 178);
	}

	@Override
	public void extractRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		this.extractTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(graphics, mouseX, mouseY, partialTicks);
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
		extractFuelTank(graphics);
	}

	@Override
	protected void containerTick() {
		super.containerTick();
	}

	@Override
	protected void extractLabels(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		this.inventoryLabelY = 84;
		this.titleLabelX = 46;

		super.extractLabels(graphics, mouseX, mouseY);

		if (isHovering(62, 19, 16, 58, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (menu.getStoredForce() == 0) {
				text.add(Component.translatable("gui.forcecraft.force_engine.empty"));
			} else {
				text.add(Component.translatable("forcecraft.container.force"));
				text.add(Component.literal(menu.getStoredForce() + " mb")
						.withStyle(ChatFormatting.GOLD));
			}

			graphics.setComponentTooltipForNextFrame(font, text, mouseX, mouseY);
		}

	}

	private void extractFuelTank(GuiGraphicsExtractor graphics) {
		if (menu.getStoredForce() <= 0) return;

		int amountStored = menu.getStoredForce();
		FluidStack fluidStack = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE, amountStored);
		float tankPercentage = RenderHelper.getTankPercentage(amountStored, 10_000);
		RenderHelper.extractFluidTankInGUI(graphics, fluidStack, leftPos + 62, topPos + 19, tankPercentage, 58);

		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 62, topPos + 19, 176, 0, 16, 64, 256, 256);
	}
}
