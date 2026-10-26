package com.mrbysco.forcecraft.client.screen;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserBlockEntity;
import com.mrbysco.forcecraft.client.screen.widgets.ProgressBar;
import com.mrbysco.forcecraft.client.util.RenderHelper;
import com.mrbysco.forcecraft.menu.InfuserMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class InfuserScreen extends AbstractContainerScreen<InfuserMenu> {

	private Inventory inventory;
	private ProgressBar infuserProgress;
	// 12 by 107
	private final Identifier INFO = Reference.modLoc("textures/gui/container/info.png");
	private final Identifier ENERGY = Reference.modLoc("textures/gui/container/energy.png");
	private final Identifier TEXTURE = Reference.modLoc("textures/gui/container/forceinfuser.png");

	private static final WidgetSprites GUIDE_SPRITE = new WidgetSprites(
			Reference.modLoc("infuser/guide"),
			Reference.modLoc("infuser/guide_highlighted")
	);
	private static final WidgetSprites CHISEL = new WidgetSprites(
			Reference.modLoc("infuser/chisel"),
			Reference.modLoc("infuser/chisel_highlight")
	);

	private Button buttonInfuse;
	private Button buttonGuide;

	public InfuserScreen(InfuserMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn, 176, 208);

		this.inventory = inv;
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


		buttonGuide = this.addRenderableWidget(new ImageButton(leftPos + x, topPos + y, btnSize, btnSize,
				GUIDE_SPRITE, (button) -> {
			if (ModList.get().isLoaded("patchouli")) {
//				com.mrbysco.forcecraft.compat.patchouli.PatchouliCompat.openBook();
			} else {
				this.inventory.player.sendSystemMessage(Component.translatable("gui.forcecraft.infuser.patchouli"));
			}
		}, Component.translatable("gui.forcecraft.infuser.button.guide")));

		x = 39;
		y = 101;

		buttonInfuse = this.addRenderableWidget(new ImageButton(leftPos + x, topPos + y, btnSize, btnSize,
				CHISEL, (button) -> {
			ItemStack bookStack = menu.getBlockEntity().getBookInSlot();
			if (bookStack.isEmpty()) {
				this.inventory.player.sendSystemMessage(Component.translatable("gui.forcecraft.infuser.nobook"));
			} else {
				this.minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
			}
		}, Component.empty()));
	}

	@Override
	protected void containerTick() {
		super.containerTick();

		boolean flag = getMenu().validRecipe[0] == 1;
		if (buttonInfuse.visible != flag) {
			buttonInfuse.visible = flag;
		}

		boolean flag2 = !menu.getBlockEntity().getBookInSlot().isEmpty();
		if (buttonGuide.visible != flag2) {
			buttonGuide.visible = flag2;
		}
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(graphics, mouseX, mouseY, partialTick);
		extractTooltip(graphics, mouseX, mouseY);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);


		this.extractFluidBar(graphics);
		this.extractEnergyBar(graphics);
		this.extractProgressBar(graphics);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		InfuserBlockEntity blockEntity = menu.getBlockEntity();

		if (isHovering(123, 16, 12, 12, mouseX, mouseY)
				&& blockEntity.getItemHandler(null).getResource(InfuserBlockEntity.SLOT_GEM).isEmpty()) {
			List<Component> text = new ArrayList<>();
			text.add(Component.translatable("gui.forcecraft.infuser.help.tooltip")
					.withStyle(ChatFormatting.GRAY));
			graphics.setComponentTooltipForNextFrame(font, text, mouseX, mouseY);
		}

		if (isHovering(39, 101, 12, 12, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (getMenu().isWorkAllowed()) {
				if (getMenu().validRecipe[0] == 0) {
					text.add(Component.translatable("gui.forcecraft.infuser.missing.recipe")
							.withStyle(ChatFormatting.RED));
				} else {
					text.add(Component.translatable("gui.forcecraft.infuser.start.tooltip")
							.withStyle(ChatFormatting.GRAY));
				}
			} else {
				boolean modifiersEmpty = blockEntity.areAllModifiersEmpty();
				if (!modifiersEmpty && menu.getEnergyStored() < blockEntity.getEnergyCostPer()) {
					text.add(Component.translatable("gui.forcecraft.infuser.missing.rf.tooltip")
							.withStyle(ChatFormatting.RED));
				} else {
					text.add(Component.translatable("gui.forcecraft.infuser.missing.tooltip")
							.withStyle(ChatFormatting.RED));
				}
			}
			graphics.setComponentTooltipForNextFrame(font, text, mouseX, mouseY);
		}

		if (isHovering(156, 8, 12, 112, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			MutableComponent tt = Component.literal(menu.getEnergyStored() + " RF")
					.withStyle(ChatFormatting.GOLD);
			text.add(tt);
			graphics.setComponentTooltipForNextFrame(font, text, mouseX, mouseY);
		}

		if (isHovering(10, 41, 15, 82, mouseX, mouseY)) {
			List<Component> text = new ArrayList<>();
			if (blockEntity.getFluidResource().isEmpty()) {
				text.add(Component.translatable("gui.forcecraft.infuser.empty.tooltip"));
			} else {
				text.add(Component.translatable("fluid.forcecraft.fluid_force_source"));

				text.add(Component.literal(blockEntity.getFluidAmount() + " mb")
						.withStyle(ChatFormatting.YELLOW));
			}

			graphics.setComponentTooltipForNextFrame(font, text, mouseX, mouseY);
		}
	}

	private void extractFluidBar(GuiGraphicsExtractor graphics) {
		InfuserBlockEntity be = menu.getBlockEntity();
		if (be == null || be.getFluidAmount() <= 0 || be.getFluidResource().isEmpty()) {
			return;
		}
		FluidStack fluidStack = be.getFluidResource().toStack(be.getFluidAmount());
		float tankPercentage = RenderHelper.getTankPercentage(be.getFluidAmount(), 50000);
		RenderHelper.extractFluidTankInGUI(graphics, fluidStack, leftPos + 8, topPos + 41, tankPercentage, 82);

		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 8, topPos + 41, 188, 26, 16, 82, 256, 256);
	}

	private void extractEnergyBar(GuiGraphicsExtractor guiGraphics) {
		if (menu.getBlockEntity() == null || menu.getBlockEntity().getEnergyCapacity() <= 0) {
			return;
		}

		float energy = getMenu().getBlockEntity().getEnergyStored();
		float capacity = menu.getBlockEntity().getEnergyCapacity();
		float pct = Math.min(energy / capacity, 1.0F);

		final int height = 107;
		int width = 12;
		int filled = (int) (height * pct);
		int offset = height - filled;

		if (filled > 0) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ENERGY, leftPos + 156, topPos + 13 + offset,
					0, offset,
					width, filled,
					width, filled, 256, 256);
		}
	}

	private void extractProgressBar(GuiGraphicsExtractor guiGraphics) {
		InfuserBlockEntity tile = menu.getBlockEntity();
		if (tile.canWork) {
			this.infuserProgress.setMin(tile.processTime).setMax(tile.maxProcessTime);
			this.infuserProgress.draw(guiGraphics);
		}
	}
}
