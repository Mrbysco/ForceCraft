package mrbysco.forcecraft.client.gui.pack;

import com.mojang.blaze3d.matrix.MatrixStack;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.gui.widgets.ItemButton;
import mrbysco.forcecraft.networking.PacketHandler;
import mrbysco.forcecraft.networking.message.PackChangeMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class PackRenameScreen extends Screen {
	private ItemStack packStack;
	private final Hand usedHand;
	private TextFieldWidget textfield;
	private int selectedColor;

	protected PackRenameScreen(ItemStack stack, Hand hand) {
		super(new TranslationTextComponent("forcecraft.pack.rename"));

		this.packStack = stack.copy();
		this.usedHand = hand;
	}

	public static void openScreen(ItemStack packName, Hand hand) {
		Minecraft.getInstance().displayGuiScreen(new PackRenameScreen(packName, hand));
	}

	@Override
	protected void init() {
		super.init();
		selectedColor = packStack.getOrCreateTag().getInt("Color");
		this.minecraft.keyboardListener.enableRepeatEvents(true);
		this.addButton(new ItemButton(this.width / 2 - 88, this.height / 2 + 5, 18, 18, DialogTexts.GUI_DONE, this.packStack, (button) -> {
			ItemButton itemButton = (ItemButton)button;
			this.selectedColor++;
			if(selectedColor > 15) {
				this.selectedColor = 0;
			}

			CompoundNBT tag = itemButton.getButtonStack().getOrCreateTag();
			tag.putInt("Color", this.selectedColor);
			itemButton.getButtonStack().setTag(tag);

			this.packStack = itemButton.getButtonStack();
		}));

		this.addButton(new Button(this.width / 2 - 34, this.height / 2 + 3, 60, 20, DialogTexts.GUI_CANCEL, (p_238847_1_) -> {
			this.minecraft.displayGuiScreen((Screen)null);
		}));

		this.addButton(new Button(this.width / 2 + 31, this.height / 2 + 3, 60, 20, DialogTexts.GUI_DONE, (p_238847_1_) -> {
			//TODO: MAKE/SEND PACKET!
			PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new PackChangeMessage(usedHand, textfield.getText(), this.selectedColor));
			this.minecraft.displayGuiScreen((Screen)null);
		}));

		this.textfield = new TextFieldWidget(this.minecraft.fontRenderer, this.width / 2 - 90, this.height / 2 - 24, 180, 20, new StringTextComponent("Name"));
		this.textfield.setMaxStringLength(31);
		this.textfield.setText(packStack.getDisplayName().getString());
		this.textfield.setTextColor(-1);
		this.children.add(this.textfield);
		setFocusedDefault(textfield);
	}

	@Override
	public void onClose() {
		super.onClose();
		this.minecraft.keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public void tick() {
		super.tick();
		this.textfield.tick();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/rename_screen.png");
		this.minecraft.getTextureManager().bindTexture(TEXTURE);

		int xSize = 197;
		int ySize = 66;
		int guiLeft = (this.width - xSize) / 2;
		int guiTop = (this.height - ySize) / 2;

		this.blit(matrixStack, guiLeft, guiTop, 0,0, xSize, ySize);

		this.textfield.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		font.drawText(matrixStack, new StringTextComponent("Color"), this.width / 2 - 68, this.height / 2 + 9, 5592405);
	}
}
