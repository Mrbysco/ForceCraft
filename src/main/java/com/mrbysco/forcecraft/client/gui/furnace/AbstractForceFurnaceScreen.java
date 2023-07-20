package com.mrbysco.forcecraft.client.gui.furnace;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrbysco.forcecraft.menu.furnace.AbstractForceFurnaceMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractForceFurnaceScreen<T extends AbstractForceFurnaceMenu> extends AbstractContainerScreen<T> {
	private final ResourceLocation GUI_TEXTURE;

	public AbstractForceFurnaceScreen(T screenContainer, Inventory inv, Component titleIn, ResourceLocation guiTextureIn) {
		super(screenContainer, inv, titleIn);
		this.GUI_TEXTURE = guiTextureIn;
	}

	public void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	public void containerTick() {
		super.containerTick();
	}

	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);

		if (isHovering(60, 36, 10, 12, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			text.add(Component.literal(String.valueOf(menu.getBurn()))
					.withStyle(ChatFormatting.GRAY));
			guiGraphics.renderComponentTooltip(font, text, mouseX, mouseY + 10);
		}

		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int i = this.leftPos;
		int j = this.topPos;
		guiGraphics.blit(GUI_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
		if (this.menu.isBurning()) {
			int k = this.menu.getBurnLeftScaled();
			guiGraphics.blit(GUI_TEXTURE, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int l = this.menu.getCookProgressionScaled();
		guiGraphics.blit(GUI_TEXTURE, i + 79, j + 34, 176, 14, l + 1, 16);
	}
}
