package com.mrbysco.forcecraft.client.gui.spoils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.SpoilsBagContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SpoilsBagScreen extends AbstractContainerScreen<SpoilsBagContainer> {
    private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/spoilsbag.png");

    public SpoilsBagScreen(SpoilsBagContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);

        this.imageHeight = 134;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderTexture(0, this.TEXTURE);
        this.blit(poseStack, this.leftPos, this.topPos, 0,0,this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.inventoryLabelY = 42;
        super.renderLabels(poseStack, mouseX, mouseY);
    }
}
