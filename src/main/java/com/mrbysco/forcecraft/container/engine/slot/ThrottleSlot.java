package com.mrbysco.forcecraft.container.engine.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ThrottleSlot extends SlotItemHandler {

	public ThrottleSlot(IItemHandler handler, int index, int posX, int posY){
		super(handler, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return getItemHandler().isItemValid(getSlotIndex(), stack);
	}

	@Override
	public int getMaxStackSize(@Nonnull ItemStack stack) {
		if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
			if(stack.getMaxStackSize() > 1) {
				return 1;
			}
		}
		return super.getMaxStackSize(stack);
	}
}
