package com.mrbysco.forcecraft.tiles;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.furnace.ForceFurnaceContainer;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ForceFurnaceTileEntity extends AbstractForceFurnaceTile {
	public ForceFurnaceTileEntity() {
		super(ForceRegistry.FURNACE_TILE.get());
	}

	protected Component getDefaultName() {
		return new TranslatableComponent(Reference.MOD_ID + ".container.force_furnace");
	}

	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return new ForceFurnaceContainer(id, player, this);
	}
}
