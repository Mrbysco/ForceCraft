package mrbysco.forcecraft.blocks.infuser;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.gui.infuser.ProgressBar;
import mrbysco.forcecraft.networking.PacketHandler;
import mrbysco.forcecraft.networking.message.InfuserMessage;
import mrbysco.forcecraft.recipe.InfuseRecipe;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
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
		infoButton = this.addButton(new Button(guiLeft + 124, guiTop + 17, 12, 12, new TranslationTextComponent("gui.forcecraft.infuser.button.guide"), (button) -> {
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
			 	  //super.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
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
		
		
		if(this.showingPop) {
			this.renderOverride(matrixStack,mouseX,mouseY,partialTicks);
			this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
		 
				//show it now only
				this.drawPopup(matrixStack);
			
		} 
		else {

			super.render(matrixStack, mouseX, mouseY, partialTicks);
		}
	}

	private void renderOverride(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
	    int i = this.guiLeft;
	      int j = this.guiTop;
	      this.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
	      net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawBackground(this, matrixStack, mouseX, mouseY));
//	      RenderSystem.disableRescaleNormal();
//	      RenderSystem.disableDepthTest();
//	      super.render(matrixStack, mouseX, mouseY, partialTicks);
//	      RenderSystem.pushMatrix();
//	      RenderSystem.translatef((float)i, (float)j, 0.0F);
//	      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//	      RenderSystem.enableRescaleNormal();
//	      this.hoveredSlot = null;
//	      int k = 240;
//	      int l = 240;
//	      RenderSystem.glMultiTexCoord2f(33986, 240.0F, 240.0F);
//	      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//	      for(int i1 = 0; i1 < this.container.inventorySlots.size(); ++i1) {
//	         Slot slot = this.container.inventorySlots.get(i1);
//	         if (slot.isEnabled()) {
//	            this.moveItems(matrixStack, slot);
//	         }
//
//	         if (this.isSlotSelected(slot, (double)mouseX, (double)mouseY) && slot.isEnabled()) {
//	            this.hoveredSlot = slot;
//	            RenderSystem.disableDepthTest();
//	            int j1 = slot.xPos;
//	            int k1 = slot.yPos;
//	            RenderSystem.colorMask(true, true, true, false);
//	            int slotColor = this.getSlotColor(i1);
//	            this.fillGradient(matrixStack, j1, k1, j1 + 16, k1 + 16, slotColor, slotColor);
//	            RenderSystem.colorMask(true, true, true, true);
//	            RenderSystem.enableDepthTest();
//	         }
//	      }
//
//	      this.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
//	      net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, matrixStack, mouseX, mouseY));
//	      PlayerInventory playerinventory = this.minecraft.player.inventory;
//	      ItemStack itemstack = this.draggedStack.isEmpty() ? playerinventory.getItemStack() : this.draggedStack;
//	      if (!itemstack.isEmpty()) {
//	         int j2 = 8;
//	         int k2 = this.draggedStack.isEmpty() ? 8 : 16;
//	         String s = null;
//	         if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
//	            itemstack = itemstack.copy();
//	            itemstack.setCount(MathHelper.ceil((float)itemstack.getCount() / 2.0F));
//	         } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
//	            itemstack = itemstack.copy();
//	            itemstack.setCount(this.dragSplittingRemnant);
//	            if (itemstack.isEmpty()) {
//	               s = "" + TextFormatting.YELLOW + "0";
//	            }
//	         }
//
//	         this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k2, s);
//	      }
//
//	      if (!this.returningStack.isEmpty()) {
//	         float f = (float)(Util.milliTime() - this.returningStackTime) / 100.0F;
//	         if (f >= 1.0F) {
//	            f = 1.0F;
//	            this.returningStack = ItemStack.EMPTY;
//	         }
//
//	         int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
//	         int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
//	         int l1 = this.touchUpX + (int)((float)l2 * f);
//	         int i2 = this.touchUpY + (int)((float)i3 * f);
//	         this.drawItemStack(this.returningStack, l1, i2, (String)null);
//	      }
//
//	      RenderSystem.popMatrix();
//	      RenderSystem.enableDepthTest();
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
		int height = 290;
		int width = 240;
		int x = guiLeft + 4 , y = guiTop + 36;
	    blit(matrixStack, x, y, 0, 0, width, height, width, height);

	    y = guiTop + 40;
	    for(int tier = 1; tier <= 7; tier++) {
	    	//ok next tier, go down and right
	    	x = guiLeft + 8;
	    	
		    List<InfuseRecipe> tierSorted = InfuseRecipe.RECIPESBYLEVEL.get(tier);
		    if(tierSorted == null || tierSorted.size() == 0) {

	    		ForceCraft.LOGGER.info("Tier has no recipes{}", tier);
	    		continue;
		    }
			for(InfuseRecipe recipe : tierSorted) {
		    	if(recipe.input.getMatchingStacks().length == 0) {
		    		ForceCraft.LOGGER.info("Cannot render recipe with no input {}", recipe);
		    		continue;
		    	}
		    	//
	//	    	ForceCraft.LOGGER.info(""+recipe.input.getMatchingStacks()[0]);
		    	this.drawItemStack(recipe.input.getMatchingStacks()[0], x, y, "");
		    	x += 20;
		    }
	    	y += 18;
	    }
	  
	}
	
	   /**
	    * Draws an ItemStack.
	    *  
	    * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
	    */
	   private void drawItemStack(ItemStack stack, int x, int y, String altText) {
	      RenderSystem.translatef(0.0F, 0.0F, 32.0F);
	      this.setBlitOffset(200);
	      this.itemRenderer.zLevel = 200.0F;
	      net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
	      if (font == null) font = this.font;
	      this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
	      this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y - 0, altText);
	      this.setBlitOffset(0);
	      this.itemRenderer.zLevel = 0.0F;
	   }
}


