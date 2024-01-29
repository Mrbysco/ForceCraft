package com.mrbysco.forcecraft.client.gui.pack;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.gui.widgets.ItemButton;
import com.mrbysco.forcecraft.networking.message.PackChangePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class RenameAndRecolorScreen extends Screen {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/rename_screen.png");
	private ItemStack itemstack;
	private final InteractionHand usedHand;
	private EditBox textfield;
	private int selectedColor;

	protected RenameAndRecolorScreen(ItemStack stack, InteractionHand hand) {
		super(Component.translatable("gui.forcecraft.rename.title"));

		this.itemstack = stack.copy();
		this.usedHand = hand;
	}

	public static void openScreen(ItemStack packName, InteractionHand hand) {
		Minecraft.getInstance().setScreen(new RenameAndRecolorScreen(packName, hand));
	}

	@Override
	protected void init() {
		super.init();
		selectedColor = itemstack.getOrCreateTag().getInt("Color");

		this.addRenderableWidget(ItemButton.builder(CommonComponents.GUI_DONE, this.itemstack, (button) -> {
			ItemButton itemButton = (ItemButton) button;
			this.selectedColor++;
			if (selectedColor > 15) {
				this.selectedColor = 0;
			}

			CompoundTag tag = itemButton.getButtonStack().getOrCreateTag();
			tag.putInt("Color", this.selectedColor);
			itemButton.getButtonStack().setTag(tag);

			this.itemstack = itemButton.getButtonStack();
		}).bounds(this.width / 2 - 89, this.height / 2 + 5, 18, 18).build());

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
			this.minecraft.setScreen((Screen) null);
		}).bounds(this.width / 2 - 34, this.height / 2 + 3, 60, 20).build());

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
			PacketDistributor.SERVER.noArg().send(new PackChangePayload(usedHand, textfield.getValue(), this.selectedColor));
			this.minecraft.setScreen((Screen) null);
		}).bounds(this.width / 2 + 31, this.height / 2 + 3, 60, 20).build());

		this.textfield = new EditBox(this.minecraft.font, this.width / 2 - 90, this.height / 2 - 24, 180, 20, Component.literal("Name"));
		this.textfield.setMaxLength(31);
		this.textfield.setValue(itemstack.getHoverName().getString());
		this.textfield.setTextColor(-1);
		this.addWidget(this.textfield);
		setInitialFocus(textfield);
	}

	@Override
	public void removed() {
		super.removed();
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

		int xSize = 197;
		int ySize = 66;
		int guiLeft = (this.width - xSize) / 2;
		int guiTop = (this.height - ySize) / 2;

		guiGraphics.blit(TEXTURE, guiLeft, guiTop, 0, 0, xSize, ySize);

		this.textfield.render(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.drawString(font, Component.literal("Color"), (int) (this.width / 2d - 68), (int) (this.height / 2d + 9), 5592405, false);
	}
}
