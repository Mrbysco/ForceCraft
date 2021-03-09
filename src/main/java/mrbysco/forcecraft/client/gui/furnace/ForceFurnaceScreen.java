package mrbysco.forcecraft.client.gui.furnace;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.ForceFurnaceContainer;
import net.minecraft.client.gui.recipebook.FurnaceRecipeGui;
import net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ForceFurnaceScreen extends AbstractFurnaceScreen<ForceFurnaceContainer> {
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/furnace_gui.png");

	public ForceFurnaceScreen(ForceFurnaceContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, new FurnaceRecipeGui(), playerInventory, title, FURNACE_GUI_TEXTURES);
	}
}