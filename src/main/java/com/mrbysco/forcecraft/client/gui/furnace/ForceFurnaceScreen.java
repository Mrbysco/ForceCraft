package com.mrbysco.forcecraft.client.gui.furnace;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.furnace.ForceFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class ForceFurnaceScreen extends AbstractForceFurnaceScreen<ForceFurnaceMenu> {
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/furnace_gui.png");

	public ForceFurnaceScreen(ForceFurnaceMenu container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title, FURNACE_GUI_TEXTURES);
	}
}