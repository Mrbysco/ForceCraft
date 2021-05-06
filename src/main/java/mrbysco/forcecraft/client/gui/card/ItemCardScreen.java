package mrbysco.forcecraft.client.gui.card;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.ItemCardContainer;
import mrbysco.forcecraft.networking.PacketHandler;
import mrbysco.forcecraft.networking.message.SaveCardRecipeMessage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ItemCardScreen extends ContainerScreen<ItemCardContainer> {
	private static final ResourceLocation ITEM_CARD_GUI = new ResourceLocation(Reference.MOD_ID, "textures/gui/crafting3x3.png");
	private static final StringTextComponent saveText = new StringTextComponent("✔");
	private static final StringTextComponent invalidText = new StringTextComponent("✘");
	private Button buttonSave;

	public ItemCardScreen(ItemCardContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	protected void init() {
		super.init();
		this.titleX = 29;

		this.buttonSave = this.addButton(new Button(this.width / 2 + 62, this.height / 2 - 76, 20, 20, saveText, (button) -> {
			PacketHandler.CHANNEL.sendToServer(new SaveCardRecipeMessage());
			this.minecraft.displayGuiScreen((Screen)null);
		}));
	}

	public void tick() {
		super.tick();

		ItemStack resultStack = getContainer().getCraftResult().getStackInSlot(0);
		if(resultStack.isEmpty()) {
			if(this.buttonSave.getMessage().getString() != invalidText.getString()) {
				this.buttonSave.setMessage(invalidText);
			}
			if(buttonSave.active) {
				buttonSave.active = false;
			}
		} else {
			if(this.buttonSave.getMessage().getString() != saveText.getString()) {
				this.buttonSave.setMessage(saveText);
			}
			if(!buttonSave.active) {
				buttonSave.active = true;
			}
		}
	}

	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(ITEM_CARD_GUI);
		int i = this.guiLeft;
		int j = (this.height - this.ySize) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
	}
}
