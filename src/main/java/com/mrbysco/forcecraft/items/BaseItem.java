package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

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

	@Override
	public void onCraftedPostProcess(ItemStack stack, Level level) {
		if (stack.getItem() instanceof IForceChargingTool && !stack.has(ForceComponents.FORCE_INFUSED)) {
			stack.set(ForceComponents.FORCE_INFUSED, false);
		}
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		return super.onItemUseFirst(stack, context);
	}
}
