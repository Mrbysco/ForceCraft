package mrbysco.forcecraft.blocks.infuser;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.gui.widgets.ProgressBar;
import mrbysco.forcecraft.networking.PacketHandler;
import mrbysco.forcecraft.networking.message.InfuserMessage;
import mrbysco.forcecraft.recipe.InfuseRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.network.PacketDistributor;
import org.lwjgl.opengl.GL11;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.ArrayList;
import java.util.List;

public class InfuserScreen extends ContainerScreen<InfuserContainer> {

	private ProgressBar infuserProgress;
	// 12 by 107
	private final ResourceLocation INFO = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/info.png");
	private final ResourceLocation ENERGY = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/energy.png");
	private final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forceinfuser.png");
	private Button buttonInfuse;

	public InfuserScreen(InfuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		this.ySize = 208;

	}

	@Override
	protected void init() {
		super.init();

		this.infuserProgress = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 20, 
				guiLeft + 134, guiTop + 93,
				176, 0);
		
		int btnSize = 13;
		int x = 123;
		int y = 16;

		this.addButton(new Button(guiLeft + x, guiTop + y, 12, 12, new TranslationTextComponent("gui.forcecraft.infuser.button.guide"), (button) -> {
			PatchouliAPI.get().openBookGUI(new ResourceLocation("forcecraft:force_and_you"));
		}) {
			@Override
			public void renderWidget(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
				// skip drawing me

				ItemStack bookStack = container.getTile().getBookInSlot();
				if (!bookStack.isEmpty()) {
					Minecraft minecraft = Minecraft.getInstance();
					minecraft.getTextureManager().bindTexture(TEXTURE);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, this.alpha);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					this.blit(ms, x, y, 201, 0, btnSize, btnSize);

					this.renderBg(ms, minecraft, mouseX, mouseY);
				} 
			}
		});
		x = 39;
		y = 101;
		buttonInfuse = this.addButton(new Button(guiLeft + x, guiTop + y, btnSize, btnSize, new TranslationTextComponent(""), (button) -> {
			ItemStack bookStack = container.getTile().getBookInSlot();
			if (bookStack.isEmpty()) {
				this.playerInventory.player.sendStatusMessage(new TranslationTextComponent("gui.forcecraft.infuser.nobook"), false);
				return;
			} else {
				PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new InfuserMessage(true));
			}
//			container.getTile().canWork = false;
		}) {
			@Override
			public void renderWidget(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
				// skip drawing me
				if(getContainer().isWorkAllowed()) {
					// render special
//				    super.renderWidget(ms, mouseX, mouseY, partialTicks);
					Minecraft minecraft = Minecraft.getInstance();
					minecraft.getTextureManager().bindTexture(TEXTURE);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, this.alpha);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					this.blit(ms, x, y, 188, 0, btnSize, btnSize);

					this.renderBg(ms, minecraft, mouseX, mouseY);
				}
			}
		});
	}

	@Override
	public void tick() {
		super.tick();

		if(buttonInfuse.active != getContainer().isWorkAllowed()) {
			buttonInfuse.active = getContainer().isWorkAllowed();
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		

		this.drawFluidBar(matrixStack);
		this.drawEnergyBar(matrixStack);
		this.drawProgressBar(matrixStack);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
		int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

		InfuserTileEntity tile = container.getTile();

		if (isPointInRegion(123, 16, 12, 12, mouseX, mouseY)
				&& tile.handler.getStackInSlot(InfuserTileEntity.SLOT_GEM).isEmpty()) {
			List<ITextComponent> text = new ArrayList<>();
			text.add(new TranslationTextComponent("gui.forcecraft.infuser.help.tooltip")
					.mergeStyle(TextFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isPointInRegion(39, 101, 12, 12, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			if(getContainer().isWorkAllowed()) {
				text.add(new TranslationTextComponent("gui.forcecraft.infuser.start.tooltip")
						.mergeStyle(TextFormatting.GRAY));
			} else {
				boolean modifiersEmpty = tile.areAllModifiersEmpty();
				if(!modifiersEmpty && tile.getEnergyStored() < tile.getEnergyCostPer()) {
					text.add(new TranslationTextComponent("gui.forcecraft.infuser.missing.rf.tooltip")
							.mergeStyle(TextFormatting.RED));
				} else {
					text.add(new TranslationTextComponent("gui.forcecraft.infuser.missing.tooltip")
							.mergeStyle(TextFormatting.RED));
				}
			}
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isPointInRegion(156, 8, 12, 112, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			IFormattableTextComponent tt = new StringTextComponent(tile.getEnergyStored() + " RF")
					.mergeStyle(TextFormatting.GOLD);
			text.add(tt);
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isPointInRegion(10, 41, 15, 82, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			if (tile.getFluid() == null) {
				text.add(new TranslationTextComponent("gui.forcecraft.infuser.empty.tooltip"));
			} else {
				text.add(new TranslationTextComponent("fluid.forcecraft.fluid_force_source"));

				text.add(new StringTextComponent(tile.getFluidAmount() + " mb")
						.mergeStyle(TextFormatting.YELLOW));
			}

			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}
	}

	private void drawFluidBar(MatrixStack matrixStack) {
		if (container.getTile() == null || container.getTile().getFluid() == null) {
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
					int fluidHeight = container.getTile().getFluidGuiHeight(82);
					blit(matrixStack, guiLeft + 8, guiTop + 65 + (58 - fluidHeight), 0, 16, fluidHeight, sprite);
				}
			}
		}
	}

	private void drawEnergyBar(MatrixStack ms) {
		if (container.getTile() == null || container.getTile().energyStorage.getMaxEnergyStored() <= 0) {
			return;
		}

		minecraft.textureManager.bindTexture(ENERGY);
		float energ = container.getTile().getEnergyStored();
		float capacity = container.getTile().energyStorage.getMaxEnergyStored();
		float pct = Math.min(energ / capacity, 1.0F);

		final float height = 107;
		int width = 12; 
		blit(ms, guiLeft + 156, guiTop + 13, 0, 0, 
				width, (int) (height * pct), 
				width, (int) height);
	}

	private void drawProgressBar(MatrixStack matrixStack) {
		InfuserTileEntity tile = container.getTile();
		if(tile.canWork) {
			this.infuserProgress.setMin(tile.processTime).setMax(tile.maxProcessTime);
			this.infuserProgress.draw(matrixStack, this.minecraft);
		}
	}

	/**
	 * Draws an ItemStack. Copied from base class
	 * 
	 * The z index is increased by 32 (and not decreased afterwards), and the item
	 * is then rendered at z=200.
	 */
	void drawItemStack(ItemStack stack, int x, int y, String altText) {
		RenderSystem.translatef(0.0F, 0.0F, 32.0F);
		this.setBlitOffset(200);
		this.itemRenderer.zLevel = 200.0F;
		net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = this.font;
		this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y - 0, altText);
		this.setBlitOffset(0);
		this.itemRenderer.zLevel = 0.0F;
	}

	static class TiledRecipe {
		int size = 20;
		InfuserScreen parent;
		InfuseRecipe recipe;
		int x;
		int y;

		public TiledRecipe(InfuserScreen self, InfuseRecipe recipe, int x, int y) {
			parent = self;
			this.recipe = recipe;
			this.x = x;
			this.y = y;
		}

		public void drawItemStack() {
			// TODO: rotate on matching stacks with a timer, just like JEI
			if(recipe.input.getMatchingStacks().length > 0) {
				parent.drawItemStack(recipe.input.getMatchingStacks()[0], parent.guiLeft + x, parent.guiTop + y, "");
			}
		}

		public void drawTooltip(MatrixStack ms, int actualMouseX, int actualMouseY) {
			RenderSystem.translatef(0.0F, 0.0F, 33.0F);

			String recipeSlug = recipe.resultModifier.getTooltip();

			List<ITextComponent> text = new ArrayList<>();
			text.add(new TranslationTextComponent(recipeSlug));

			GuiUtils.drawHoveringText(ms, text, actualMouseX, actualMouseY, parent.width, parent.height, -1, parent.font);
			
			RenderSystem.translatef(0.0F, 0.0F, 0.0F);
		}
	}
}
