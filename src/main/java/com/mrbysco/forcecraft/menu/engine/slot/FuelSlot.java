package com.mrbysco.forcecraft.menu.engine.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

public class FuelSlot extends ResourceHandlerSlot {

	public FuelSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int posX, int posY) {
		super(handler, slotModifier, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return getResourceHandler().isValid(0, ItemResource.of(stack));
	}

	@Override
	public int getMaxStackSize(@NotNull ItemStack stack) {
		if (ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM) != null) {
			if (stack.getMaxStackSize() > 1) {
				return 1;
			}
		}
		return super.getMaxStackSize(stack);
	}
}
