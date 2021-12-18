package com.mrbysco.forcecraft.container.slot;

import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotForceTools extends SlotItemHandler {
    public SlotForceTools(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem().is(ForceTags.VALID_INFUSER_TOOLS);
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 1;
    }
}
