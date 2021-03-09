package mrbysco.forcecraft.container.slot;

import mrbysco.forcecraft.Reference;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class InfuserSlot extends Slot {

    public InfuserSlot(IInventory inventoryIn, int index, int xPosition, int yPosition){
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack item){
        final ResourceLocation gemTag = new ResourceLocation(Reference.MOD_ID, "valid_infuser_modifiers");
        ITag<Item> tag = ItemTags.getCollection().getTagByID(gemTag);
        return item.getItem().isIn(tag);
    }
}
