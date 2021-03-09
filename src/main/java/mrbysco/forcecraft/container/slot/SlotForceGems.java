package mrbysco.forcecraft.container.slot;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotForceGems extends SlotItemHandler {

    public SlotForceGems(IItemHandler handler, int index, int posX, int posY){
        super(handler, index, posX, posY);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        final ResourceLocation gemTag = new ResourceLocation("forge", "gems/force");
        ITag<Item> tag = ItemTags.getCollection().getTagByID(gemTag);
        return stack.getItem().isIn(tag);
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return 64;
    }
}
