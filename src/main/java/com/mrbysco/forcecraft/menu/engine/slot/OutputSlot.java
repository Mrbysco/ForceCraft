package com.mrbysco.forcecraft.menu.engine.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class OutputSlot extends ResourceHandlerSlot {

	public OutputSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int posX, int posY) {
		super(handler, slotModifier, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return getResourceHandler().isValid(1, ItemResource.of(stack));
	}
}
