package com.mrbysco.forcecraft.client.gui.pack;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.ForcePackContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ForcePackScreen extends ContainerScreen<ForcePackContainer> {

    private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack.png");
    private ResourceLocation TEXTURE_UPGRADE_1 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_1.png");
    private ResourceLocation TEXTURE_UPGRADE_2 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_2.png");
    private ResourceLocation TEXTURE_UPGRADE_3 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_3.png");
    private ResourceLocation TEXTURE_UPGRADE_4 = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/forcepack_upgrade_4.png");

    public ForcePackScreen(ForcePackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.ySize = 136 + (this.container.getUpgrades() * 18);
        this.playerInventoryTitleY = 42 + (this.container.getUpgrades() * 18);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        switch (this.container.getUpgrades()) {
            default:
                this.minecraft.getTextureManager().bindTexture(this.TEXTURE);
                break;
            case 1:
                this.minecraft.getTextureManager().bindTexture(this.TEXTURE_UPGRADE_1);
                break;
            case 2:
                this.minecraft.getTextureManager().bindTexture(this.TEXTURE_UPGRADE_2);
                break;
            case 3:
                this.minecraft.getTextureManager().bindTexture(this.TEXTURE_UPGRADE_3);
                break;
            case 4:
                this.minecraft.getTextureManager().bindTexture(this.TEXTURE_UPGRADE_4);
                break;
        }
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0,0,this.xSize, this.ySize);
    }
}
