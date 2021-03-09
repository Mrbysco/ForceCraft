package mrbysco.forcecraft.client.gui.infuser;

import com.mojang.blaze3d.matrix.MatrixStack;
import mrbysco.forcecraft.Reference;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class GUIGuideScreen extends Screen {

    private ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/guide/guide.png");

    private int xSize;
    private int ySize;

    private int guiLeft;
    private int guiTop;

    public GUIGuideScreen() {
        super(new StringTextComponent("")); //TODO: Guide title?
        this.xSize = 204;
        this.ySize = 176;

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        //this.renderHoveredToolTip(mouseX, mouseY);
    }
}
