package com.mrbysco.forcecraft.client.gui.pack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.gui.widgets.ItemButton;
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.networking.message.PackChangeMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class RenameAndRecolorScreen extends Screen {
	private ItemStack itemstack;
	private final InteractionHand usedHand;
	private EditBox textfield;
	private int selectedColor;

	protected RenameAndRecolorScreen(ItemStack stack, InteractionHand hand) {
		super(new TranslatableComponent("forcecraft.pack.rename"));

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
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		this.addRenderableWidget(new ItemButton(this.width / 2 - 89, this.height / 2 + 5, 18, 18, CommonComponents.GUI_DONE, this.itemstack, (button) -> {
			ItemButton itemButton = (ItemButton) button;
			this.selectedColor++;
			if (selectedColor > 15) {
				this.selectedColor = 0;
			}

			CompoundTag tag = itemButton.getButtonStack().getOrCreateTag();
			tag.putInt("Color", this.selectedColor);
			itemButton.getButtonStack().setTag(tag);

			this.itemstack = itemButton.getButtonStack();
		}));

		this.addRenderableWidget(new Button(this.width / 2 - 34, this.height / 2 + 3, 60, 20, CommonComponents.GUI_CANCEL, (p_238847_1_) -> {
			this.minecraft.setScreen((Screen) null);
		}));

		this.addRenderableWidget(new Button(this.width / 2 + 31, this.height / 2 + 3, 60, 20, CommonComponents.GUI_DONE, (p_238847_1_) -> {
			PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new PackChangeMessage(usedHand, textfield.getValue(), this.selectedColor));
			this.minecraft.setScreen((Screen) null);
		}));

		this.textfield = new EditBox(this.minecraft.font, this.width / 2 - 90, this.height / 2 - 24, 180, 20, new TextComponent("Name"));
		this.textfield.setMaxLength(31);
		this.textfield.setValue(itemstack.getHoverName().getString());
		this.textfield.setTextColor(-1);
		this.addWidget(this.textfield);
		setInitialFocus(textfield);
	}

	@Override
	public void removed() {
		super.removed();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public void tick() {
		super.tick();
		this.textfield.tick();
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/rename_screen.png");
		RenderSystem.setShaderTexture(0, TEXTURE);

		int xSize = 197;
		int ySize = 66;
		int guiLeft = (this.width - xSize) / 2;
		int guiTop = (this.height - ySize) / 2;

		this.blit(poseStack, guiLeft, guiTop, 0, 0, xSize, ySize);

		this.textfield.render(poseStack, mouseX, mouseY, partialTicks);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		font.draw(poseStack, new TextComponent("Color"), (int) (this.width / 2d - 68), (int) (this.height / 2d + 9), 5592405);
	}
}
