package com.mrbysco.forcecraft.container.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotForceBook extends SlotItemHandler {

    public SlotForceBook(IItemHandler handler, int index, int posX, int posY){
        super(handler, index, posX, posY);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() == Items.BOOK;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 1;
    }
}
