package com.mrbysco.forcecraft.client.gui.spoils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.SpoilsBagContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class SpoilsBagScreen extends AbstractContainerScreen<SpoilsBagContainer> {
    private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/spoilsbag.png");

    public SpoilsBagScreen(SpoilsBagContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);

        this.imageHeight = 134;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().bind(this.TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0,0,this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.inventoryLabelY = 42;
        super.renderLabels(matrixStack, mouseX, mouseY);
    }
}
