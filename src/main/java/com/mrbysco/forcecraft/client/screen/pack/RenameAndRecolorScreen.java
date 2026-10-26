package com.mrbysco.forcecraft.client.screen.pack;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.screen.widgets.ItemButton;
import com.mrbysco.forcecraft.networking.message.PackChangePayload;
import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class RenameAndRecolorScreen extends Screen {
	private static final Identifier TEXTURE = Reference.modLoc("textures/gui/container/rename_screen.png");
	private ItemStack itemstack;
	private final InteractionHand usedHand;
	private EditBox textfield;
	private DyeColor selectedColor;

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
		selectedColor = itemstack.getOrDefault(ForceComponents.PACK_COLOR, DyeColor.YELLOW);
		this.addRenderableWidget(ItemButton.builder(CommonComponents.GUI_DONE, this.itemstack, (button) -> {
			ItemButton itemButton = (ItemButton) button;
			this.selectedColor = DyeColor.values()[(this.selectedColor.ordinal() + 1) % DyeColor.values().length];

			itemstack.set(ForceComponents.PACK_COLOR, selectedColor);
			this.itemstack = itemButton.getButtonStack();
		}).bounds(this.width / 2 - 89, this.height / 2 + 5, 18, 18).build());

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
			this.minecraft.setScreen(null);
		}).bounds(this.width / 2 - 34, this.height / 2 + 3, 60, 20).build());

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
			ClientPacketDistributor.sendToServer(new PackChangePayload(usedHand, textfield.getValue(), this.selectedColor));
			this.minecraft.setScreen(null);
		}).bounds(this.width / 2 + 31, this.height / 2 + 3, 60, 20).build());

		this.textfield = new EditBox(this.minecraft.font, this.width / 2 - 90, this.height / 2 - 24, 180, 20, Component.literal("Name"));
		this.textfield.setMaxLength(31);
		this.textfield.setValue(itemstack.getHoverName().getString());
		this.textfield.setTextColor(-1);
		this.addRenderableWidget(this.textfield);
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
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
		int xSize = 197;
		int ySize = 66;
		int guiLeft = (this.width - xSize) / 2;
		int guiTop = (this.height - ySize) / 2;

		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, guiLeft, guiTop, 0, 0, xSize, ySize, 256, 256);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(graphics, mouseX, mouseY, partialTick);

		graphics.text(font, Component.literal("Color"), (int) (this.width / 2d - 68), (int) (this.height / 2d + 9), 5592405, false);
	}
}
