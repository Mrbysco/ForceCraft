package com.mrbysco.forcecraft.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ItemButton extends Button {
	protected final ItemStack buttonStack;

	public ItemButton(int x, int y, int width, int height, Component title, ItemStack stack, OnPress pressedAction, CreateNarration createNarration) {
		super(x, y, width, height, title, pressedAction, createNarration);
		this.buttonStack = stack;
	}

	protected ItemButton(ItemButton.Builder builder) {
		this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.stack, builder.onPress, builder.createNarration);
		setTooltip(builder.tooltip); // Forge: Make use of the Builder tooltip
	}

	public ItemStack getButtonStack() {
		return buttonStack;
	}

	@Override
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		//RENDER THE STACK
		Minecraft minecraft = Minecraft.getInstance();
		ItemRenderer itemRender = minecraft.getItemRenderer();
		itemRender.renderGuiItem(this.buttonStack, this.getX(), this.getY());
//		super.renderWidget(poseStack, mouseX, mouseY, partialTicks);
	}

	public static ItemButton.Builder builder(Component component, ItemStack stack, Button.OnPress onPress) {
		return new ItemButton.Builder(component, stack, onPress);
	}

	public static class Builder {
		private final Component message;
		private final ItemStack stack;
		private final Button.OnPress onPress;
		@Nullable
		private Tooltip tooltip;
		private int x;
		private int y;
		private int width = 150;
		private int height = 20;
		private Button.CreateNarration createNarration = Button.DEFAULT_NARRATION;

		public Builder(Component component, ItemStack stack, Button.OnPress onPress) {
			this.message = component;
			this.stack = stack;
			this.onPress = onPress;
		}

		public ItemButton.Builder pos(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public ItemButton.Builder width(int width) {
			this.width = width;
			return this;
		}

		public ItemButton.Builder size(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public ItemButton.Builder bounds(int x, int y, int width, int height) {
			return this.pos(x, y).size(width, height);
		}

		public ItemButton.Builder tooltip(@Nullable Tooltip tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public ItemButton.Builder createNarration(Button.CreateNarration createNarration) {
			this.createNarration = createNarration;
			return this;
		}

		public ItemButton build() {
			return build(ItemButton::new);
		}

		public ItemButton build(java.util.function.Function<ItemButton.Builder, ItemButton> builder) {
			return builder.apply(this);
		}
	}
}
