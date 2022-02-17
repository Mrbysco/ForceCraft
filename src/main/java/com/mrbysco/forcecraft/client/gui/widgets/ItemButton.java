package com.mrbysco.forcecraft.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemButton extends Button {
	protected final ItemStack buttonStack;
	public ItemButton(int x, int y, int width, int height, Component title, ItemStack stack, OnPress pressedAction) {
		super(x, y, width, height, title, pressedAction);
		this.buttonStack = stack;
	}

	public ItemStack getButtonStack() {
		return buttonStack;
	}

	@Override
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		//RENDER THE STACK
		Minecraft minecraft = Minecraft.getInstance();
		ItemRenderer itemRender = minecraft.getItemRenderer();
		itemRender.renderGuiItem(this.buttonStack, this.x, this.y);
//		super.renderWidget(poseStack, mouseX, mouseY, partialTicks);
	}
}
