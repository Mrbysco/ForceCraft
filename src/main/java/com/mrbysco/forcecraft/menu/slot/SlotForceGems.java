package com.mrbysco.forcecraft.menu.slot;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotForceGems extends SlotItemHandler {

    public SlotForceGems(IItemHandler handler, int index, int posX, int posY){
        super(handler, index, posX, posY);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        final ResourceLocation gemTag = new ResourceLocation("forge", "gems/force");
        Tag<Item> tag = ItemTags.getAllTags().getTagOrEmpty(gemTag);
        return stack.is(tag);
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 64;
    }
}
