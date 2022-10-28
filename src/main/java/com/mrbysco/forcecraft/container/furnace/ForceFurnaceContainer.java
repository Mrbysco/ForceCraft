package com.mrbysco.forcecraft.container.furnace;

import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.tiles.AbstractForceFurnaceTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

public class ForceFurnaceContainer extends AbstractForceFurnaceContainer {
	public ForceFurnaceContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		super(windowId, playerInventory, data);
	}

	public ForceFurnaceContainer(int id, PlayerInventory playerInventoryIn, AbstractForceFurnaceTile te) {
		super(id, playerInventoryIn, te);
	}

	@Override
	public ContainerType<?> getType() {
		return ForceContainers.FORCE_FURNACE.get();
	}
}