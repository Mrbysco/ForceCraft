package com.mrbysco.forcecraft.tiles;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.furnace.ForceFurnaceContainer;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ForceFurnaceTileEntity extends AbstractForceFurnaceTile {
	public ForceFurnaceTileEntity() {
		super(ForceRegistry.FURNACE_TILE.get());
	}

	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(Reference.MOD_ID + ".container.force_furnace");
	}

	protected Container createMenu(int id, PlayerInventory player) {
		return new ForceFurnaceContainer(id, player, this);
	}
}
