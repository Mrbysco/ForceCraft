package com.mrbysco.forcecraft.container.engine.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class FuelSlot extends SlotItemHandler {

	public FuelSlot(IItemHandler handler, int index, int posX, int posY){
		super(handler, index, posX, posY);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return getItemHandler().isItemValid(0, stack);
	}

	@Override
	public int getItemStackLimit(@Nonnull ItemStack stack) {
		if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
			if(stack.getMaxStackSize() > 1) {
				return 1;
			}
		}
		return super.getItemStackLimit(stack);
	}
}
