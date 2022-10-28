package com.mrbysco.forcecraft.client.gui.furnace;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrbysco.forcecraft.container.furnace.AbstractForceFurnaceContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractForceFurnaceScreen<T extends AbstractForceFurnaceContainer> extends ContainerScreen<T> {
	private final ResourceLocation guiTexture;

	public AbstractForceFurnaceScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, ResourceLocation guiTextureIn) {
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

	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);

		if (isHovering(60, 36, 10, 12, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			text.add(new StringTextComponent(String.valueOf(menu.getBurn()))
					.withStyle(TextFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, mouseX, mouseY + 10, width, height, -1, font);
		}

		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
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
