package com.mrbysco.forcecraft.client.gui.card;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.ItemCardMenu;
import com.mrbysco.forcecraft.networking.message.SaveCardRecipePayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class ItemCardScreen extends AbstractContainerScreen<ItemCardMenu> {
	private static final ResourceLocation ITEM_CARD_GUI = new ResourceLocation(Reference.MOD_ID, "textures/gui/crafting3x3.png");
	private static final Component saveText = Component.literal("\u2714");
	private static final Component invalidText = Component.literal("\u2718");
	private Button buttonSave;

	public ItemCardScreen(ItemCardMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	protected void init() {
		super.init();
		this.titleLabelX = 29;

		this.buttonSave = this.addRenderableWidget(Button.builder(saveText, (button) -> {
			PacketDistributor.SERVER.noArg().send(new SaveCardRecipePayload());
		}).bounds(this.width / 2 + 62, this.height / 2 - 76, 20, 20).build());
	}

	public void containerTick() {
		super.containerTick();

		ItemStack resultStack = getMenu().getCraftResult().getItem(0);
		if (resultStack.isEmpty()) {
			if (!this.buttonSave.getMessage().getString().equals(invalidText.getString())) {
				this.buttonSave.setMessage(invalidText);
			}
			if (buttonSave.active) {
				buttonSave.active = false;
			}
		} else {
			if (!this.buttonSave.getMessage().getString().equals(saveText.getString())) {
				this.buttonSave.setMessage(saveText);
			}
			if (!buttonSave.active) {
				buttonSave.active = true;
			}
		}
	}

	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int i = this.leftPos;
		int j = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(ITEM_CARD_GUI, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
}
