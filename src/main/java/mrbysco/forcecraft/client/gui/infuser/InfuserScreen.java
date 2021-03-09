package mrbysco.forcecraft.client.gui.infuser;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.InfuserContainer;
import mrbysco.forcecraft.networking.PacketHandler;
import mrbysco.forcecraft.networking.message.InfuserMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class InfuserScreen extends ContainerScreen<InfuserContainer> {

	private ProgressBar infuserProgress;
	private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forceinfuser.png");

	public InfuserScreen(InfuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		this.xSize = 176;
		this.ySize = 208;

		this.addButton(new Button(39, 101, 12, 12, new TranslationTextComponent("gui.forcecraft.infuser.button.button"), (button) -> {
			PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new InfuserMessage(true));
			container.getTile().canWork = true;
		}));
		this.addButton(new Button(123, 17, 12, 12, new TranslationTextComponent("gui.forcecraft.infuser.button.guide"), (button) -> {
			if(screenContainer.getTile().handler.getStackInSlot(9).isEmpty()) {
				//Insert behavior
			}
		}));

		this.infuserProgress = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 20, 134, 93, 176, 0);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(this.TEXTURE);
		this.blit(matrixStack, this.guiLeft, this.guiTop, 0,0,this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
		int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

		this.infuserProgress.setMin(this.container.getTile().processTime).setMax(this.container.getTile().maxProcessTime);
		this.infuserProgress.draw(matrixStack, this.minecraft);
		this.drawFluidBar(matrixStack);

		if (isPointInRegion(123, 16, 12, 12, mouseX, mouseY) && this.container.getTile().handler.getStackInSlot(9).isEmpty()) {
			List<ITextComponent> text = new ArrayList<>();
			text.add(new TranslationTextComponent("gui.forcecraft.infuser.help.tooltip").mergeStyle(TextFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if(isPointInRegion(39, 101, 12, 12, mouseX, mouseY)){
			List<ITextComponent> text = new ArrayList<>();
			text.add(new TranslationTextComponent("gui.forcecraft.infuser.start.tooltip").mergeStyle(TextFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if(isPointInRegion(10, 36, 15, 82, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			if (this.container.getTile().getFluid() == null) {
				text.add(new TranslationTextComponent("gui.forcecraft.infuser.empty.tooltip"));
			} else {
				text.add(new StringTextComponent("Liquid Force" + TextFormatting.WHITE + " (" + this.container.getTile().getFluidAmount() + ")").mergeStyle(TextFormatting.YELLOW));
			}

			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if(isPointInRegion(152, 11, 12, 106, mouseX, mouseY)){
			List<ITextComponent> text = new ArrayList<>();
			text.add(new StringTextComponent(this.container.getTile().getEnergyStored() + " FE"));

			//this.drawHoveringText(text, actualMouseX, actualMouseY);
		}
	}

	private void drawFluidBar(MatrixStack matrixStack){
		if(this.container.getTile().getFluid() != null) {
			FluidStack fluidStack = this.container.getTile().getFluidStack();
			ResourceLocation flowing = fluidStack.getFluid().getAttributes().getStillTexture(fluidStack);
			if (flowing != null) {
				Texture texture = minecraft.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE); //getAtlasSprite
				if (texture instanceof AtlasTexture) {
					TextureAtlasSprite sprite = ((AtlasTexture) texture).getSprite(flowing);
					if (sprite != null) {
						this.minecraft.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
						int fluidHeight = this.container.getTile().getFluidGuiHeight(82);
						blit(matrixStack, 10, 60 + (58 - fluidHeight), 0, 16, fluidHeight, sprite);
					}
				}
			}
		}
	}

	private void drawEnergyBar(){

	}
}
