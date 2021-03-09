package mrbysco.forcecraft.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaseItem extends Item {

    public BaseItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
