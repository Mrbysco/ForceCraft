package com.mrbysco.forcecraft.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BaseItem extends Item {

	public BaseItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
}
