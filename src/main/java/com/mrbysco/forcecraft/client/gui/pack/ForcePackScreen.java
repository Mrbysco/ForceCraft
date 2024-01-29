package com.mrbysco.forcecraft.client.gui.pack;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.ForcePackMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ForcePackScreen extends AbstractContainerScreen<ForcePackMenu> {

	private final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack.png");
	private final ResourceLocation TEXTURE_UPGRADE_1 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_1.png");
	private final ResourceLocation TEXTURE_UPGRADE_2 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_2.png");
	private final ResourceLocation TEXTURE_UPGRADE_3 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_3.png");
	private final ResourceLocation TEXTURE_UPGRADE_4 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_4.png");

	public ForcePackScreen(ForcePackMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);

		this.imageHeight = 136 + (this.menu.getUpgrades() * 18);
		this.inventoryLabelY = 42 + (this.menu.getUpgrades() * 18);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
		ResourceLocation texture = switch (this.menu.getUpgrades()) {
			default -> this.TEXTURE;
			case 1 -> this.TEXTURE_UPGRADE_1;
			case 2 -> this.TEXTURE_UPGRADE_2;
			case 3 -> this.TEXTURE_UPGRADE_3;
			case 4 -> this.TEXTURE_UPGRADE_4;
		};
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}
}
