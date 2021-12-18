package com.mrbysco.forcecraft.client.gui.furnace;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrbysco.forcecraft.container.furnace.AbstractForceFurnaceContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractForceFurnaceScreen<T extends AbstractForceFurnaceContainer> extends AbstractContainerScreen<T> {
	private final ResourceLocation guiTexture;

	public AbstractForceFurnaceScreen(T screenContainer, Inventory inv, Component titleIn, ResourceLocation guiTextureIn) {
		super(screenContainer, inv, titleIn);
		this.guiTexture = guiTextureIn;
	}

	public void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	public void tick() {
		super.tick();
	}

	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);

		if (isHovering(60, 36, 10, 12, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			text.add(new TextComponent(String.valueOf(menu.getBurn()))
					.withStyle(ChatFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, mouseX, mouseY + 10, width, height, -1, font);
		}

		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(this.guiTexture);
		int i = this.leftPos;
		int j = this.topPos;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		if (this.menu.isBurning()) {
			int k = this.menu.getBurnLeftScaled();
			this.blit(matrixStack, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int l = this.menu.getCookProgressionScaled();
		this.blit(matrixStack, i + 79, j + 34, 176, 14, l + 1, 16);
	}
}
