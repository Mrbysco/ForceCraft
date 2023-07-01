package com.mrbysco.forcecraft.client.gui.infuser;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.client.gui.widgets.ProgressBar;
import com.mrbysco.forcecraft.client.util.RenderHelper;
import com.mrbysco.forcecraft.menu.infuser.InfuserMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class InfuserScreen extends AbstractContainerScreen<InfuserMenu> {

	private Inventory inventory;
	private ProgressBar infuserProgress;
	// 12 by 107
	private final ResourceLocation INFO = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/info.png");
	private final ResourceLocation ENERGY = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/energy.png");
	private final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forceinfuser.png");
	private Button buttonInfuse;

	public InfuserScreen(InfuserMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);

		this.inventory = inv;
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
		this.addRenderableWidget(new Button(leftPos + x, topPos + y, 13, 13, Component.translatable("gui.forcecraft.infuser.button.guide"), (button) -> {
			if (ModList.get().isLoaded("patchouli")) {
				com.mrbysco.forcecraft.compat.patchouli.PatchouliCompat.openBook();
			} else {
				this.inventory.player.displayClientMessage(Component.translatable("gui.forcecraft.infuser.patchouli"), false);
			}
		}) {
			@Override
			public void renderButton(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
				// skip drawing me

				ItemStack bookStack = menu.getTile().getBookInSlot();
				if (!bookStack.isEmpty()) {
					Minecraft minecraft = Minecraft.getInstance();
					RenderSystem.setShaderTexture(0, TEXTURE);
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
					RenderSystem.enableBlend();
					RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
					this.blit(ms, x, y, 201, 0, btnSize, btnSize);

					this.renderBg(ms, minecraft, mouseX, mouseY);
				}
			}
		});
		x = 39;
		y = 101;
		buttonInfuse = this.addRenderableWidget(new Button(leftPos + x, topPos + y, btnSize, btnSize, Component.literal(""), (button) -> {
			ItemStack bookStack = menu.getTile().getBookInSlot();
			if (bookStack.isEmpty()) {
				this.inventory.player.displayClientMessage(Component.translatable("gui.forcecraft.infuser.nobook"), false);
			} else {
				this.minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
			}
//			container.getTile().canWork = false;
		}) {
			@Override
			public void renderButton(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
				// skip drawing me
				boolean flag = getMenu().validRecipe[0] == 1;
				if (flag) {
					// render special
//				    super.renderWidget(ms, mouseX, mouseY, partialTicks);
					Minecraft minecraft = Minecraft.getInstance();
					RenderSystem.setShaderTexture(0, TEXTURE);
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
					RenderSystem.enableBlend();
					RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
					this.blit(ms, x, y, 188, 0, btnSize, btnSize);

					this.renderBg(ms, minecraft, mouseX, mouseY);
				}
			}
		});
	}

	@Override
	protected void containerTick() {
		super.containerTick();

		boolean flag = getMenu().validRecipe[0] == 1;
		if (buttonInfuse.active != flag) {
			buttonInfuse.active = flag;
		}
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);

		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
		RenderSystem.setShaderTexture(0, TEXTURE);
		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);


		this.drawFluidBar(poseStack);
		this.drawEnergyBar(poseStack);
		this.drawProgressBar(poseStack);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);

		InfuserBlockEntity tile = menu.getTile();

		if (isHovering(123, 16, 12, 12, mouseX, mouseY)
				&& tile.handler.getStackInSlot(InfuserBlockEntity.SLOT_GEM).isEmpty()) {
			List<Component> text = new ArrayList<>();
			text.add(Component.translatable("gui.forcecraft.infuser.help.tooltip")
					.withStyle(ChatFormatting.GRAY));
			renderComponentTooltip(poseStack, text, actualMouseX, actualMouseY);
		}

		if (isHovering(39, 101, 12, 12, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (getMenu().isWorkAllowed()) {
				text.add(Component.translatable("gui.forcecraft.infuser.start.tooltip")
						.withStyle(ChatFormatting.GRAY));
			} else {
				boolean modifiersEmpty = tile.areAllModifiersEmpty();
				if (!modifiersEmpty && tile.getEnergyStored() < tile.getEnergyCostPer()) {
					text.add(Component.translatable("gui.forcecraft.infuser.missing.rf.tooltip")
							.withStyle(ChatFormatting.RED));
				} else {
					text.add(Component.translatable("gui.forcecraft.infuser.missing.tooltip")
							.withStyle(ChatFormatting.RED));
				}
			}
			renderComponentTooltip(poseStack, text, actualMouseX, actualMouseY);
		}

		if (isHovering(156, 8, 12, 112, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			MutableComponent tt = Component.literal(tile.getEnergyStored() + " RF")
					.withStyle(ChatFormatting.GOLD);
			text.add(tt);
			renderComponentTooltip(poseStack, text, actualMouseX, actualMouseY);
		}

		if (isHovering(10, 41, 15, 82, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (tile.getFluid() == null) {
				text.add(Component.translatable("gui.forcecraft.infuser.empty.tooltip"));
			} else {
				text.add(Component.translatable("fluid.forcecraft.fluid_force_source"));

				text.add(Component.literal(tile.getFluidAmount() + " mb")
						.withStyle(ChatFormatting.YELLOW));
			}

			renderComponentTooltip(poseStack, text, actualMouseX, actualMouseY);
		}
	}

	private void drawFluidBar(PoseStack poseStack) {
		if (menu.getTile() == null || menu.getTile().getFluid() == null) {
			return;
		}
		FluidStack fluidStack = menu.getTile().getFluidStack();
		float tankPercentage = RenderHelper.getTankPercentage(getMenu().getTile().getFluidAmount(), 50000);
		RenderHelper.drawFluidTankInGUI(fluidStack, leftPos + 8, topPos + 41, tankPercentage, 82);

		RenderSystem.setShaderTexture(0, TEXTURE);
		blit(poseStack, leftPos + 8, topPos + 41, 188, 26, 16, 82);
	}

	private void drawEnergyBar(PoseStack ms) {
		if (menu.getTile() == null || menu.getTile().energyStorage.getMaxEnergyStored() <= 0) {
			return;
		}

		RenderSystem.setShaderTexture(0, ENERGY);
		float energy = getMenu().getTile().getEnergyStored();
		float capacity = menu.getTile().energyStorage.getMaxEnergyStored();
		float pct = Math.min(energy / capacity, 1.0F);

		final float height = 107;
		int width = 12;
		blit(ms, leftPos + 156, topPos + 13, 0, 0,
				width, (int) (height * pct),
				width, (int) height);
	}

	private void drawProgressBar(PoseStack poseStack) {
		InfuserBlockEntity tile = menu.getTile();
		if (tile.canWork) {
			this.infuserProgress.setMin(tile.processTime).setMax(tile.maxProcessTime);
			this.infuserProgress.draw(poseStack, this.minecraft);
		}
	}
}
