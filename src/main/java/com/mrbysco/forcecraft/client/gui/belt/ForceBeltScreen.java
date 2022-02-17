package com.mrbysco.forcecraft.client.gui.belt;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.ForceBeltContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ForceBeltScreen extends AbstractContainerScreen<ForceBeltContainer> {
    private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcebelt.png");

    public ForceBeltScreen(ForceBeltContainer screenContainer, Inventory inv, Component titleIn) {
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
