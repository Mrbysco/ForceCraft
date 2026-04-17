package com.mrbysco.forcecraft.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

public class SlotForceBook extends ResourceHandlerSlot {

	public SlotForceBook(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int posX, int posY) {
		super(handler, slotModifier, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() == Items.BOOK;
	}

	@Override
	public int getMaxStackSize(@NotNull ItemStack stack) {
		return 1;
	}
}
