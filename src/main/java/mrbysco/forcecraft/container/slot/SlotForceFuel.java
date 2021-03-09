package mrbysco.forcecraft.container.slot;

import mrbysco.forcecraft.Reference;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class SlotForceFuel extends Slot {

    public SlotForceFuel(IInventory inventoryIn, int slotIndex, int xPos, int yPos) {
        super(inventoryIn, slotIndex, xPos, yPos);
    }

    public boolean isItemValid(ItemStack stack) {
        final ResourceLocation gemTag = new ResourceLocation(Reference.MOD_ID, "force_fuel");
        ITag<Item> tag = ItemTags.getCollection().getTagByID(gemTag);
        return stack.getItem().isIn(tag);
    }

    public int getItemStackLimit(ItemStack stack)
    {
        return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }

    public static boolean isBucket(ItemStack stack)
    {
        return stack.getItem() == Items.BUCKET;
    }

}
