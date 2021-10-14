package com.mrbysco.forcecraft.client.gui.furnace;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.furnace.ForceFurnaceContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ForceFurnaceScreen extends AbstractForceFurnaceScreen<ForceFurnaceContainer> {
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/furnace_gui.png");

	public ForceFurnaceScreen(ForceFurnaceContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, FURNACE_GUI_TEXTURES);
	}
}