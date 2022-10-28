package com.mrbysco.forcecraft.client.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import net.minecraft.client.gui.widget.button.Button.IPressable;

public class ItemButton extends Button {
	protected final ItemStack buttonStack;

	public ItemButton(int x, int y, int width, int height, ITextComponent title, ItemStack stack, IPressable pressedAction) {
		super(x, y, width, height, title, pressedAction);
		this.buttonStack = stack;
	}

	public ItemStack getButtonStack() {
		return buttonStack;
	}

	@Override
	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		//RENDER THE STACK
		Minecraft minecraft = Minecraft.getInstance();
		ItemRenderer itemRender = minecraft.getItemRenderer();
		itemRender.renderGuiItem(this.buttonStack, this.x, this.y);
//		super.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
	}
}
