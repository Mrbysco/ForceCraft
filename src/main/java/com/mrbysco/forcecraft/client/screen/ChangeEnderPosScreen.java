package com.mrbysco.forcecraft.client.screen;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.item.tool.EnderData;
import com.mrbysco.forcecraft.networking.message.ChangeEnderPosPayload;
import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ChangeEnderPosScreen extends Screen {
	private static final Identifier TEXTURE = Reference.modLoc("textures/gui/container/rename_screen.png");
	private final InteractionHand usedHand;
	private final EnderData data;
	private EditBox name;

	private int x, y, z;

	protected ChangeEnderPosScreen(ItemStack stack, InteractionHand hand) {
		super(Component.translatable("gui.forcecraft.rename.title"));
		this.usedHand = hand;
		this.data = stack.getOrDefault(ForceComponents.ENDER_DATA, EnderData.getDefault());
		this.x = (int) this.data.x();
		this.y = (int) this.data.y();
		this.z = (int) this.data.z();
	}

	public static void openScreen(ItemStack packName, InteractionHand hand) {
		Minecraft.getInstance().setScreen(new ChangeEnderPosScreen(packName, hand));
	}

	@Override
	protected void init() {
		super.init();

		int yPos = this.height / 2 - 24;

		int buttonY = yPos + 26;
		int buttonX = this.width / 2 - 90;
		this.addRenderableWidget(Button.builder(Component.literal("Set"), button -> {
			if (this.minecraft != null && this.minecraft.player != null) {
				this.x = this.minecraft.player.blockPosition().getX();
				this.y = this.minecraft.player.blockPosition().getY();
				this.z = this.minecraft.player.blockPosition().getZ();
			}
		}).bounds(buttonX, buttonY, 58, 20).build());

		this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> {
			this.onClose();
		}).bounds(buttonX + 61, buttonY, 58, 20).build());

		this.addRenderableWidget(Button.builder(Component.literal("Save"), button -> {
			if (this.minecraft != null && this.minecraft.level != null) {
				ClientPacketDistributor.sendToServer(new ChangeEnderPosPayload(usedHand,
						GlobalPos.of(this.minecraft.level.dimension(), BlockPos.containing(x, y, z)), name.getValue())
				);
			}
			this.onClose();
		}).bounds(buttonX + 122, buttonY, 58, 20).build());

		this.name = new EditBox(this.minecraft.font, this.width / 2 - 90, yPos, 180, 20, Component.literal("Name"));
		this.name.setMaxLength(31);
		this.name.setValue(data.name());
		this.name.setTextColor(-1);
		this.name.setFocused(true);
		this.addRenderableWidget(this.name);
		this.setInitialFocus(this.name);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(graphics, mouseX, mouseY, partialTick);

		// Display X Y and Z above the name field (with a label)
		graphics.centeredText(this.font,
				Component.literal("X: " + this.x + "  Y: " + this.y + "  Z: " + this.z),
				this.width / 2, this.name.getY() - 12, -1);
	}

}
