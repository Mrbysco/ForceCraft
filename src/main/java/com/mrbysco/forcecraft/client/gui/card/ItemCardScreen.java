package com.mrbysco.forcecraft.client.gui.card;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.ItemCardMenu;
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.networking.message.SaveCardRecipeMessage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

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
			PacketHandler.CHANNEL.sendToServer(new SaveCardRecipeMessage());
		}).bounds(this.width / 2 + 62, this.height / 2 - 76, 20, 20).build());
	}

	public void containerTick() {
		super.containerTick();

		ItemStack resultStack = getMenu().getCraftResult().getItem(0);
		if (resultStack.isEmpty()) {
			if (this.buttonSave.getMessage().getString() != invalidText.getString()) {
				this.buttonSave.setMessage(invalidText);
			}
			if (buttonSave.active) {
				buttonSave.active = false;
			}
		} else {
			if (this.buttonSave.getMessage().getString() != saveText.getString()) {
				this.buttonSave.setMessage(saveText);
			}
			if (!buttonSave.active) {
				buttonSave.active = true;
			}
		}
	}

	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}

	protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, ITEM_CARD_GUI);
		int i = this.leftPos;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
}
