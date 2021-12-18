package com.mrbysco.forcecraft.client.gui.pack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.ForcePackContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class ForcePackScreen extends AbstractContainerScreen<ForcePackContainer> {

    private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack.png");
    private ResourceLocation TEXTURE_UPGRADE_1 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_1.png");
    private ResourceLocation TEXTURE_UPGRADE_2 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_2.png");
    private ResourceLocation TEXTURE_UPGRADE_3 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_3.png");
    private ResourceLocation TEXTURE_UPGRADE_4 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_4.png");

    public ForcePackScreen(ForcePackContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);

        this.imageHeight = 136 + (this.menu.getUpgrades() * 18);
        this.inventoryLabelY = 42 + (this.menu.getUpgrades() * 18);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        switch (this.menu.getUpgrades()) {
            default -> this.minecraft.getTextureManager().bind(this.TEXTURE);
            case 1 -> this.minecraft.getTextureManager().bind(this.TEXTURE_UPGRADE_1);
            case 2 -> this.minecraft.getTextureManager().bind(this.TEXTURE_UPGRADE_2);
            case 3 -> this.minecraft.getTextureManager().bind(this.TEXTURE_UPGRADE_3);
            case 4 -> this.minecraft.getTextureManager().bind(this.TEXTURE_UPGRADE_4);
        }
        this.blit(matrixStack, this.leftPos, this.topPos, 0,0,this.imageWidth, this.imageHeight);
    }
}
