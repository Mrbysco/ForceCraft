package com.mrbysco.forcecraft.blocks.infuser;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.gui.widgets.ProgressBar;
import com.mrbysco.forcecraft.client.util.RenderHelper;
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.networking.message.InfuserMessage;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.network.PacketDistributor;
import org.lwjgl.opengl.GL11;

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

		this.imageHeight = 208;
	}

	@Override
	protected void init() {
		super.init();

		this.infuserProgress = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 20, 
				leftPos + 134, topPos + 93,
				176, 0);
		
		int btnSize = 13;

		int x = 123;
		int y = 16;
		this.addButton(new Button(leftPos + x, topPos + y, 13, 13, new TranslationTextComponent("gui.forcecraft.infuser.button.guide"), (button) -> {
			if(ModList.get().isLoaded("patchouli")) {
				com.mrbysco.forcecraft.compat.patchouli.PatchouliCompat.openBook();
			} else {
				this.inventory.player.displayClientMessage(new TranslationTextComponent("gui.forcecraft.infuser.patchouli"), false);
			}
		}) {
			@Override
			public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
				// skip drawing me

				ItemStack bookStack = menu.getTile().getBookInSlot();
				if (!bookStack.isEmpty()) {
					Minecraft minecraft = Minecraft.getInstance();
					minecraft.getTextureManager().bind(TEXTURE);
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
		buttonInfuse = this.addButton(new Button(leftPos + x, topPos + y, btnSize, btnSize, new TranslationTextComponent(""), (button) -> {
			ItemStack bookStack = menu.getTile().getBookInSlot();
			if (bookStack.isEmpty()) {
				this.inventory.player.displayClientMessage(new TranslationTextComponent("gui.forcecraft.infuser.nobook"), false);
			} else {
				PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new InfuserMessage(true));
			}
//			container.getTile().canWork = false;
		}) {
			@Override
			public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
				// skip drawing me
				boolean flag = getMenu().validRecipe[0] == 1;
				if(flag) {
					// render special
//				    super.renderWidget(ms, mouseX, mouseY, partialTicks);
					Minecraft minecraft = Minecraft.getInstance();
					minecraft.getTextureManager().bind(TEXTURE);
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

		boolean flag = getMenu().validRecipe[0] == 1;
		if(buttonInfuse.active != flag) {
			buttonInfuse.active = flag;
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		this.minecraft.getTextureManager().bind(TEXTURE);
		this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		

		this.drawFluidBar(matrixStack);
		this.drawEnergyBar(matrixStack);
		this.drawProgressBar(matrixStack);
	}
	
	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);

		InfuserTileEntity tile = menu.getTile();

		if (isHovering(123, 16, 12, 12, mouseX, mouseY)
				&& tile.handler.getStackInSlot(InfuserTileEntity.SLOT_GEM).isEmpty()) {
			List<ITextComponent> text = new ArrayList<>();
			text.add(new TranslationTextComponent("gui.forcecraft.infuser.help.tooltip")
					.withStyle(TextFormatting.GRAY));
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isHovering(39, 101, 12, 12, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			if(getMenu().isWorkAllowed()) {
				text.add(new TranslationTextComponent("gui.forcecraft.infuser.start.tooltip")
						.withStyle(TextFormatting.GRAY));
			} else {
				boolean modifiersEmpty = tile.areAllModifiersEmpty();
				if(!modifiersEmpty && tile.getEnergyStored() < tile.getEnergyCostPer()) {
					text.add(new TranslationTextComponent("gui.forcecraft.infuser.missing.rf.tooltip")
							.withStyle(TextFormatting.RED));
				} else {
					text.add(new TranslationTextComponent("gui.forcecraft.infuser.missing.tooltip")
							.withStyle(TextFormatting.RED));
				}
			}
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isHovering(156, 8, 12, 112, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			IFormattableTextComponent tt = new StringTextComponent(tile.getEnergyStored() + " RF")
					.withStyle(TextFormatting.GOLD);
			text.add(tt);
			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}

		if (isHovering(10, 41, 15, 82, mouseX, mouseY)) {
			List<ITextComponent> text = new ArrayList<>();
			if (tile.getFluid() == null) {
				text.add(new TranslationTextComponent("gui.forcecraft.infuser.empty.tooltip"));
			} else {
				text.add(new TranslationTextComponent("fluid.forcecraft.fluid_force_source"));

				text.add(new StringTextComponent(tile.getFluidAmount() + " mb")
						.withStyle(TextFormatting.YELLOW));
			}

			GuiUtils.drawHoveringText(matrixStack, text, actualMouseX, actualMouseY, width, height, -1, font);
		}
	}

	private void drawFluidBar(MatrixStack matrixStack) {
		if (menu.getTile() == null || menu.getTile().getFluid() == null) {
			return;
		}
		FluidStack fluidStack = menu.getTile().getFluidStack();
		float tankPercentage = RenderHelper.getTankPercentage(getMenu().getTile().getFluidAmount(), 50000);
		RenderHelper.drawFluidTankInGUI(fluidStack, leftPos + 8, topPos + 41, tankPercentage, 82);

		minecraft.textureManager.bind(TEXTURE);
		blit(matrixStack, leftPos + 8, topPos + 41, 188, 26, 16, 82);
	}

	private void drawEnergyBar(MatrixStack ms) {
		if (menu.getTile() == null || menu.getTile().energyStorage.getMaxEnergyStored() <= 0) {
			return;
		}

		minecraft.textureManager.bind(ENERGY);
		float energ = getMenu().getTile().getEnergyStored();
		float capacity = menu.getTile().energyStorage.getMaxEnergyStored();
		float pct = Math.min(energ / capacity, 1.0F);

		final float height = 107;
		int width = 12; 
		blit(ms, leftPos + 156, topPos + 13, 0, 0, 
				width, (int) (height * pct), 
				width, (int) height);
	}

	private void drawProgressBar(MatrixStack matrixStack) {
		InfuserTileEntity tile = menu.getTile();
		if(tile.canWork) {
			this.infuserProgress.setMin(tile.processTime).setMax(tile.maxProcessTime);
			this.infuserProgress.draw(matrixStack, this.minecraft);
		}
	}
}
