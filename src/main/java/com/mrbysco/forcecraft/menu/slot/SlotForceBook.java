package com.mrbysco.forcecraft.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SlotForceBook extends SlotItemHandler {

	public SlotForceBook(IItemHandler handler, int index, int posX, int posY) {
		super(handler, index, posX, posY);
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
