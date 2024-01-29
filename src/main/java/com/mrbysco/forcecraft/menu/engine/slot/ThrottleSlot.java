package com.mrbysco.forcecraft.menu.engine.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ThrottleSlot extends SlotItemHandler {

	public ThrottleSlot(IItemHandler handler, int index, int posX, int posY) {
		super(handler, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return getItemHandler().isItemValid(getSlotIndex(), stack);
	}

	@Override
	public int getMaxStackSize(@Nonnull ItemStack stack) {
		if (stack.getCapability(Capabilities.FluidHandler.ITEM) != null) {
			if (stack.getMaxStackSize() > 1) {
				return 1;
			}
		}
		return super.getMaxStackSize(stack);
	}
}
