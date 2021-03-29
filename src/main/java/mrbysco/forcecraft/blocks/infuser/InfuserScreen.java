package mrbysco.forcecraft.blocks.infuser;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.gui.infuser.ProgressBar;
import mrbysco.forcecraft.items.infuser.UpgradeBookData;
import mrbysco.forcecraft.items.infuser.UpgradeBookTier;
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

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class InfuserScreen extends ContainerScreen<InfuserContainer> {

	private List<TiledRecipe> renderMyTiles = new ArrayList<>();
	private ProgressBar infuserProgress;
	private boolean showingPop = false;
	// 12 by 107
	private ResourceLocation INFO = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/info.png");
	private ResourceLocation ENERGY = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/energy.png");
	private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID,
			"textures/gui/container/forceinfuser.png");

	public InfuserScreen(InfuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

//		this.xSize = 176;
		this.ySize = 208;

		this.infuserProgress = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 20, 134, 93,
				176, 0);
	}

	@Override
	protected void init() {
		super.init();

		int btnSize = 13;
		int x = 124;
		int y = 17;
		// Comment out this button to disable Info help ? button
		this.addButton(new Button(guiLeft + x, guiTop + y, 12, 12, new TranslationTextComponent("gui.forcecraft.infuser.button.guide"), (button) -> {

			PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new InfuserMessage(false));

			ItemStack bookStack = container.tile.getBookInSlot();
			if (bookStack.isEmpty()) {
				this.playerInventory.player.sendStatusMessage(new TranslationTextComponent("gui.forcecraft.infuser.nobook"), false);
				showingPop = false;// do not open without a book
				return;
			}

			showingPop = !showingPop;

			if (showingPop) {
				// reset tiles every time we do a hide/show
				renderMyTiles = new ArrayList<>();
			}
		}) {
			@Override
			public void renderWidget(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
				// skip drawing me

				ItemStack bookStack = container.tile.getBookInSlot();
				if (!bookStack.isEmpty()) {
					Minecraft minecraft = Minecraft.getInstance();
					minecraft.getTextureManager().bindTexture(TEXTURE);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, this.alpha);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					this.blit(ms, x, y, 201, 0, btnSize, btnSize);

					this.renderBg(ms, minecraft, mouseX, mouseY);
				}
				// super.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
			}
		});
		x = 39;
		y = 101;
		this.addButton(new Button(guiLeft + x, guiTop + y, btnSize, btnSize, new TranslationTextComponent(""), (button) -> {
			PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new InfuserMessage(true));
			container.getTile().canWork = true;
			// TODO: change button
		}) {
			@Override
			public void renderWidget(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
				// skip drawing me
//				ForceCraft.LOGGER.info("canWork = "+ container.getTile().canWork);
				if (!container.getTile().canWork) {
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
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);

		if (this.showingPop) {
			this.renderOverride(matrixStack, mouseX, mouseY, partialTicks);

			// show it now only
			this.drawPopup(matrixStack);

			for (TiledRecipe tile : renderMyTiles) {
				if (isPointInRegion(tile.x, tile.y, tile.size, tile.size, mouseX, mouseY)) {

					tile.drawTooltip(matrixStack, mouseX, mouseY);
				}
			}

		} else {
			super.render(matrixStack, mouseX, mouseY, partialTicks);
			this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
		}
	}

	private void renderOverride(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		// render only background texture, not the item stacks
		this.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		

		this.drawFluidBar(matrixStack);
		this.drawEnergyBar(matrixStack);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
		int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

		this.infuserProgress.setMin(container.getTile().processTime).setMax(container.getTile().maxProcessTime);
		this.infuserProgress.draw(matrixStack, this.minecraft);


		if (isPointInRegion(123, 16, 12, 12, mouseX, mouseY)
				&& container.getTile().handler.getStackInSlot(InfuserTileEntity.SLOT_GEM).isEmpty()) {
			List<ITextComponent> text = new ArrayList<>();
			text.add(new TranslationTextComponent("gui.forcecraft.infuser.help.tooltip")
					.mergeStyle(TextFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isPointInRegion(39, 101, 12, 12, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			text.add(new TranslationTextComponent("gui.forcecraft.infuser.start.tooltip")
					.mergeStyle(TextFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isPointInRegion(150, 8, 16, 82, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			IFormattableTextComponent tt = new TranslationTextComponent("" + this.container.tile.getEnergyStored())
					.mergeStyle(TextFormatting.GRAY);
			text.add(tt);
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isPointInRegion(10, 36, 15, 82, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			if (container.tile.getFluid() == null) {
				text.add(new TranslationTextComponent("gui.forcecraft.infuser.empty.tooltip"));
			} else {
				text.add(new TranslationTextComponent("fluid.forcecraft.fluid_force_source"));

				text.add(new StringTextComponent("(" + this.container.tile.getFluidAmount() + ")")
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
					int fluidHeight = container.tile.getFluidGuiHeight(82);
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

	private void drawPopup(MatrixStack matrixStack) {

		ItemStack bookStack = container.tile.getBookInSlot();
		if (bookStack.isEmpty()) {
			// draw string. actually, button should be locked
			return;
		}

		minecraft.textureManager.bindTexture(INFO);
		int height = 290;
		int width = 240;
		int x = guiLeft + 4, y = guiTop + 36;
		blit(matrixStack, x, y, 0, 0, width, height, width, height);

		UpgradeBookData bd = new UpgradeBookData(bookStack);

		if (renderMyTiles.isEmpty()) {
			// re build the whole list
			y = 40;
			for (int tier = UpgradeBookTier.ZERO.ordinal(); tier <= UpgradeBookTier.FINAL.ordinal(); tier++) {

				if (tier > bd.getTier().ordinal()) {
					break;
				}

				// ok next tier, go down and right
				x = 8;

				List<InfuseRecipe> tierSorted = InfuseRecipe.RECIPESBYLEVEL.get(tier);
				if (tierSorted == null || tierSorted.size() == 0) {
					ForceCraft.LOGGER.info("Tier has no recipes{}", tier);
					continue;
				}
				for (InfuseRecipe recipe : tierSorted) {
					if (recipe.input.getMatchingStacks().length == 0) {
						ForceCraft.LOGGER.info("Cannot render recipe with no input {}", recipe);
						continue;
					}
					renderMyTiles.add(new TiledRecipe(this, recipe, x, y));

					x += 20;
				}
				y += 18;
			}
		}
		// else just render
		for (TiledRecipe tile : renderMyTiles) {
			tile.drawItemStack();
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
