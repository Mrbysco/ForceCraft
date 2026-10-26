package com.mrbysco.forcecraft.client.screen.pack;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.ForcePackMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ForcePackScreen extends AbstractContainerScreen<ForcePackMenu> {

	private final Identifier TEXTURE = Reference.modLoc("textures/gui/container/forcepack.png");
	private final Identifier TEXTURE_UPGRADE_1 = Reference.modLoc("textures/gui/container/forcepack_upgrade_1.png");
	private final Identifier TEXTURE_UPGRADE_2 = Reference.modLoc("textures/gui/container/forcepack_upgrade_2.png");
	private final Identifier TEXTURE_UPGRADE_3 = Reference.modLoc("textures/gui/container/forcepack_upgrade_3.png");
	private final Identifier TEXTURE_UPGRADE_4 = Reference.modLoc("textures/gui/container/forcepack_upgrade_4.png");

	public ForcePackScreen(ForcePackMenu menu, Inventory inv, Component titleIn) {
		super(menu, inv, titleIn, 176, 136 + (menu.getUpgrades() * 18));
		this.inventoryLabelY = 42 + (this.menu.getUpgrades() * 18);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(graphics, mouseX, mouseY, partialTick);
		extractTooltip(graphics, mouseX, mouseY);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		Identifier texture = switch (this.menu.getUpgrades()) {
			case 1 -> this.TEXTURE_UPGRADE_1;
			case 2 -> this.TEXTURE_UPGRADE_2;
			case 3 -> this.TEXTURE_UPGRADE_3;
			case 4 -> this.TEXTURE_UPGRADE_4;
			default -> this.TEXTURE;
		};
		graphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
	}
}
