package mrbysco.forcecraft.blocks.infuser;

import com.mojang.blaze3d.matrix.MatrixStack;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.gui.infuser.ProgressBar;
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
import net.minecraft.util.text.IFormattableTextComponent;
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
	private boolean showingPop = false;
	//12 by 107
	private ResourceLocation INFO = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/info.png");
	private ResourceLocation ENERGY = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/energy.png");
	private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forceinfuser.png");
	private Button canWorkButton;
	private Button infoButton;

	public InfuserScreen(InfuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

//		this.xSize = 176;
		this.ySize = 208;



		this.infuserProgress = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 20, 134, 93, 176, 0);
	}

	@Override
	protected void init() {
		super.init();

		//123, 17, 
		infoButton = this.addButton(new Button(guiLeft + 124, guiTop + 101, 12, 12, new TranslationTextComponent("gui.forcecraft.infuser.button.guide"), (button) -> {
			PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new InfuserMessage(false));
			showingPop = !showingPop;
			ForceCraft.LOGGER.info("here test sub gui {}", showingPop);
//			if(screenContainer.getTile().handler.getStackInSlot(9).isEmpty()) {
//				//Insert behavior
//			}
		}) {
			@Override
			   public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
					//skip drawing me		
			 	super.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
			   }
		}); 
		
		
		// "gui.forcecraft.infuser.button.button"
		canWorkButton = this.addButton(new Button(guiLeft + 39, guiTop + 101, 12, 12, new TranslationTextComponent(""), (button) -> { 
			PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new InfuserMessage(true));
			container.getTile().canWork = true;
			//TODO: change button
		}) {
				@Override
			    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
					//skip drawing me		   
			    }
		});
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		if(this.showingPop == false) {
			this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
		} 
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		this.blit(matrixStack, this.guiLeft, this.guiTop, 0,0,this.xSize, this.ySize);
		

		
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
		int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

		this.infuserProgress.setMin(container.getTile().processTime).setMax(container.getTile().maxProcessTime);
		this.infuserProgress.draw(matrixStack, this.minecraft);
		
		this.drawFluidBar(matrixStack);
		this.drawEnergyBar(matrixStack);
		

		if (isPointInRegion(123, 16, 12, 12, mouseX, mouseY) && container.getTile().handler.getStackInSlot(InfuserTileEntity.SLOT_GEM).isEmpty()) {
			List<ITextComponent> text = new ArrayList<>();
			text.add(new TranslationTextComponent("gui.forcecraft.infuser.help.tooltip").mergeStyle(TextFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if(isPointInRegion(39, 101, 12, 12, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			text.add(new TranslationTextComponent("gui.forcecraft.infuser.start.tooltip").mergeStyle(TextFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if(isPointInRegion(150, 8, 16, 82, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			IFormattableTextComponent tt = new TranslationTextComponent(" " + this.container.tile.getEnergyStored()).mergeStyle(TextFormatting.GRAY);
			text.add(tt);
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if(isPointInRegion(10, 36, 15, 82, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			if (container.tile.getFluid() == null) {
				text.add(new TranslationTextComponent("gui.forcecraft.infuser.empty.tooltip"));
			} else {
				// TODO: liquid force tooltip
				text.add(new StringTextComponent("Liquid Force" + TextFormatting.WHITE + " (" + this.container.tile.getFluidAmount() + ")").mergeStyle(TextFormatting.YELLOW));
			}

			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if(this.showingPop) {
			//show it now only
			this.drawPopup(matrixStack);
		}

	}


	private void drawFluidBar(MatrixStack matrixStack) {
		if(container.getTile() == null || container.getTile().getFluid() == null) {
			return;
		}
		FluidStack fluidStack = container.getTile().getFluidStack();
		ResourceLocation flowing = fluidStack.getFluid().getAttributes().getStillTexture(fluidStack);
		
		if (flowing != null) {
			Texture texture = minecraft.getTextureManager().getTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
			if (texture instanceof AtlasTexture) {
				TextureAtlasSprite sprite = ((AtlasTexture) texture).getSprite(flowing);
				if (sprite != null) {
					minecraft.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
					int fluidHeight = container.tile.getFluidGuiHeight(82);
					blit(matrixStack, 8, 65 + (58 - fluidHeight), 0, 16, fluidHeight, sprite);
				}
			}
		}
	}

	private void drawEnergyBar(MatrixStack ms) {
		if(container.getTile() == null || container.getTile().energyStorage.getMaxEnergyStored() <= 0) {
			return;
		}

		minecraft.textureManager.bindTexture(ENERGY);
		float energ = container.getTile().getEnergyStored();
		float capacity = container.getTile().energyStorage.getMaxEnergyStored();
	    float pct = Math.min(energ / capacity, 1.0F);
	    
		float height = 107;
		int width = 12;
		int x = 31, y = -3;
	    blit(ms, guiLeft + x, guiTop + y, 0, 0, width, (int)(height * pct), width,  (int)height);
	}

	private void drawPopup(MatrixStack matrixStack) {

		minecraft.textureManager.bindTexture(INFO);
		int height = 170;
		int width = 220;
		int x = 26, y = 0;
	    blit(matrixStack, x, y, 0, 0, width, height, width, height);
	}
}
