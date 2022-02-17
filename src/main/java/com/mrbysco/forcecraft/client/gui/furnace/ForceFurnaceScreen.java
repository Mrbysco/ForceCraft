package com.mrbysco.forcecraft.client.gui.furnace;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.furnace.ForceFurnaceContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ForceFurnaceScreen extends AbstractForceFurnaceScreen<ForceFurnaceContainer> {
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/furnace_gui.png");

	public ForceFurnaceScreen(ForceFurnaceContainer container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title, FURNACE_GUI_TEXTURES);
	}
}