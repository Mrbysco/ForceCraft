package com.mrbysco.forcecraft.container.furnace;

import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.blockentities.AbstractForceFurnaceBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.FriendlyByteBuf;

public class ForceFurnaceContainer extends AbstractForceFurnaceContainer {
    public ForceFurnaceContainer(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        super(windowId, playerInventory, data);
    }

    public ForceFurnaceContainer(int id, Inventory playerInventoryIn, AbstractForceFurnaceBlockEntity te) {
        super(id, playerInventoryIn, te);
    }

    @Override
    public MenuType<?> getType() {
        return ForceContainers.FORCE_FURNACE.get();
    }
}