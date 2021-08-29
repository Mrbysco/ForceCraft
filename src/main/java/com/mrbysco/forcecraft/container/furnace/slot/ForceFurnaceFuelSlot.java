package com.mrbysco.forcecraft.container.furnace.slot;

import com.mrbysco.forcecraft.container.furnace.AbstractForceFurnaceContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ForceFurnaceFuelSlot extends Slot {
	private final AbstractForceFurnaceContainer furnaceContainer;

	public ForceFurnaceFuelSlot(AbstractForceFurnaceContainer furnaceContainer, IInventory furnaceInventory, int p_i50084_3_, int p_i50084_4_, int p_i50084_5_) {
		super(furnaceInventory, p_i50084_3_, p_i50084_4_, p_i50084_5_);
		this.furnaceContainer = furnaceContainer;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	public boolean isItemValid(ItemStack stack) {
		return this.furnaceContainer.isFuel(stack) || isBucket(stack);
	}

	public int getItemStackLimit(ItemStack stack) {
		return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
	}

	public static boolean isBucket(ItemStack stack) {
		return stack.getItem() == Items.BUCKET;
	}
}
