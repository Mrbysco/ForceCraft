package com.mrbysco.forcecraft.client.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemButton extends Button {
	protected final ItemStack buttonStack;

	public ItemButton(int x, int y, int width, int height, Component title, ItemStack stack, OnPress pressedAction, CreateNarration createNarration) {
		super(x, y, width, height, title, pressedAction, createNarration);
		this.buttonStack = stack;
	}

	protected ItemButton(Builder builder) {
		this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.stack, builder.onPress, builder.createNarration);
		setTooltip(builder.tooltip); // Forge: Make use of the Builder tooltip
	}

	public ItemStack getButtonStack() {
		return buttonStack;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		Minecraft minecraft = Minecraft.getInstance();
		graphics.item(this.buttonStack, this.getX(), this.getY());
		graphics.itemDecorations(minecraft.font, this.buttonStack, this.getX(), this.getY());
	}

	public static Builder builder(Component component, ItemStack stack, OnPress onPress) {
		return new Builder(component, stack, onPress);
	}

	public static class Builder {
		private final Component message;
		private final ItemStack stack;
		private final OnPress onPress;
		@Nullable
		private Tooltip tooltip;
		private int x;
		private int y;
		private int width = 150;
		private int height = 20;
		private CreateNarration createNarration = Button.DEFAULT_NARRATION;

		public Builder(Component component, ItemStack stack, OnPress onPress) {
			this.message = component;
			this.stack = stack;
			this.onPress = onPress;
		}

		public Builder pos(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public Builder width(int width) {
			this.width = width;
			return this;
		}

		public Builder size(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public Builder bounds(int x, int y, int width, int height) {
			return this.pos(x, y).size(width, height);
		}

		public Builder tooltip(@Nullable Tooltip tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public Builder createNarration(CreateNarration createNarration) {
			this.createNarration = createNarration;
			return this;
		}

		public ItemButton build() {
			return build(ItemButton::new);
		}

		public ItemButton build(java.util.function.Function<Builder, ItemButton> builder) {
			return builder.apply(this);
		}
	}
}
