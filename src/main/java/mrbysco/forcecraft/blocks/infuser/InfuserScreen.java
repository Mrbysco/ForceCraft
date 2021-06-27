package mrbysco.forcecraft.blocks.infuser;

import com.mojang.blaze3d.matrix.MatrixStack;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.gui.widgets.ProgressBar;
import mrbysco.forcecraft.client.util.RenderHelper;
import mrbysco.forcecraft.networking.PacketHandler;
import mrbysco.forcecraft.networking.message.InfuserMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
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
			} else {
				PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new InfuserMessage(true));
			}
//			container.getTile().canWork = false;
		}) {
			@Override
			public void renderWidget(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
				// skip drawing me
				boolean flag = getContainer().validRecipe[0] == 1;
				if(flag) {
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

		boolean flag = getContainer().validRecipe[0] == 1;
		if(buttonInfuse.active != flag) {
			buttonInfuse.active = flag;
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
		float tankPercentage = RenderHelper.getTankPercentage(getContainer().getTile().getFluidAmount(), 50000);
		RenderHelper.drawFluidTankInGUI(fluidStack, guiLeft + 8, guiTop + 41, tankPercentage, 82);

		minecraft.textureManager.bindTexture(TEXTURE);
		blit(matrixStack, guiLeft + 8, guiTop + 41, 188, 26, 16, 82);
	}

	private void drawEnergyBar(MatrixStack ms) {
		if (container.getTile() == null || container.getTile().energyStorage.getMaxEnergyStored() <= 0) {
			return;
		}

		minecraft.textureManager.bindTexture(ENERGY);
		float energ = getContainer().getTile().getEnergyStored();
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
}
