package com.mrbysco.forcecraft.menu.slot;

import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SlotForceFuel extends Slot {

	public SlotForceFuel(Container inventoryIn, int slotIndex, int xPos, int yPos) {
		super(inventoryIn, slotIndex, xPos, yPos);
	}

	public boolean mayPlace(ItemStack stack) {
		return stack.is(ForceTags.FORCE_FUELS);
	}

	public int getMaxStackSize(ItemStack stack) {
		return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
	}

	public static boolean isBucket(ItemStack stack) {
		return stack.getItem() == Items.BUCKET;
	}

}
