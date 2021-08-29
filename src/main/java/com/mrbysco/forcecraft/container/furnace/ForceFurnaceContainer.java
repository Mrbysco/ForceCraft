package com.mrbysco.forcecraft.container.furnace;

import com.mrbysco.forcecraft.registry.ForceContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.util.IIntArray;

public class ForceFurnaceContainer extends AbstractForceFurnaceContainer {
    public ForceFurnaceContainer(int id, PlayerInventory playerInventoryIn) {
        super(ForceContainers.FORCE_FURNACE.get(), RecipeBookCategory.FURNACE, id, playerInventoryIn);
    }

    public ForceFurnaceContainer(int id, PlayerInventory playerInventoryIn, IInventory furnaceInventoryIn, IIntArray furnaceData) {
        super(ForceContainers.FORCE_FURNACE.get(), RecipeBookCategory.FURNACE, id, playerInventoryIn, furnaceInventoryIn, furnaceData);
    }

    @Override
    public ContainerType<?> getType() {
        return ForceContainers.FORCE_FURNACE.get();
    }
}