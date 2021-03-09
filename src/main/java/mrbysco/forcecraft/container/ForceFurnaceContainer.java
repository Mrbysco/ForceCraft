package mrbysco.forcecraft.container;

import mrbysco.forcecraft.registry.ForceContainers;
import mrbysco.forcecraft.tiles.ForceFurnaceTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.util.IIntArray;

public class ForceFurnaceContainer extends AbstractFurnaceContainer {
    public ForceFurnaceContainer(int id, PlayerInventory playerInventoryIn) {
        super(ForceContainers.FORCE_FURNACE.get(), IRecipeType.SMELTING, RecipeBookCategory.FURNACE, id, playerInventoryIn);
    }

    public ForceFurnaceContainer(int id, PlayerInventory playerInventoryIn, IInventory furnaceInventoryIn, IIntArray p_i50083_4_) {
        super(ForceContainers.FORCE_FURNACE.get(), IRecipeType.SMELTING, RecipeBookCategory.FURNACE, id, playerInventoryIn, furnaceInventoryIn, p_i50083_4_);
    }

    @Override
    protected boolean isFuel(ItemStack stack) {
        return ForceFurnaceTileEntity.isFuel(stack);
    }
}