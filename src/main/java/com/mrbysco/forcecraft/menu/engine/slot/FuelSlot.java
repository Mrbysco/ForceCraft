package com.mrbysco.forcecraft.menu.engine.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class FuelSlot extends SlotItemHandler {

	public FuelSlot(IItemHandler handler, int index, int posX, int posY) {
		super(handler, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return getItemHandler().isItemValid(0, stack);
	}

	@Override
	public int getMaxStackSize(@Nonnull ItemStack stack) {
		if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent()) {
			if (stack.getMaxStackSize() > 1) {
				return 1;
			}
		}
		return super.getMaxStackSize(stack);
	}
}
