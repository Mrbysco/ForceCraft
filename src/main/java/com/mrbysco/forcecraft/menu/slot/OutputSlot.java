package com.mrbysco.forcecraft.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class OutputSlot extends ResourceHandlerSlot {

	public OutputSlot(ItemStacksResourceHandler handler, int index, int posX, int posY) {
		super(handler, handler::set, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return getResourceHandler().isValid(1, ItemResource.of(stack));
	}
}
