package mrbysco.forcecraft.client.gui.spoils;

import com.mojang.blaze3d.matrix.MatrixStack;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.SpoilsBagContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SpoilsBagScreen extends ContainerScreen<SpoilsBagContainer> {
    private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/spoilsbag.png");

    public SpoilsBagScreen(SpoilsBagContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.xSize = 176;
        this.ySize = 134;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().bindTexture(this.TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0,0,this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.playerInventoryTitleY = 42;
        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
    }
}
