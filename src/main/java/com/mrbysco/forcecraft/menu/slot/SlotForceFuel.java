package com.mrbysco.forcecraft.menu.slot;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

public class SlotForceFuel extends Slot {

    public SlotForceFuel(Container inventoryIn, int slotIndex, int xPos, int yPos) {
        super(inventoryIn, slotIndex, xPos, yPos);
    }

    public boolean mayPlace(ItemStack stack) {
        final ResourceLocation gemTag = new ResourceLocation(Reference.MOD_ID, "force_fuel");
        Tag<Item> tag = ItemTags.getAllTags().getTagOrEmpty(gemTag);
        return stack.is(tag);
    }

    public int getMaxStackSize(ItemStack stack)
    {
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isBucket(ItemStack stack)
    {
        return stack.getItem() == Items.BUCKET;
    }

}
