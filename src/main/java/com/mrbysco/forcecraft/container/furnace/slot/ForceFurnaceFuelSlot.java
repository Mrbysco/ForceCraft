package com.mrbysco.forcecraft.container.furnace.slot;

import com.mrbysco.forcecraft.container.furnace.AbstractForceFurnaceContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ForceFurnaceFuelSlot extends SlotItemHandler {
	private final AbstractForceFurnaceContainer furnaceContainer;

	public ForceFurnaceFuelSlot(AbstractForceFurnaceContainer furnaceContainer, IItemHandler furnaceInventory, int slotIndex, int xPosition, int yPosition) {
		super(furnaceInventory, slotIndex, xPosition, yPosition);
		this.furnaceContainer = furnaceContainer;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	public boolean mayPlace(ItemStack stack) {
		return this.furnaceContainer.isFuel(stack) || isBucket(stack);
	}

	public int getMaxStackSize(ItemStack stack) {
		return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
	}

	public static boolean isBucket(ItemStack stack) {
		return stack.getItem() == Items.BUCKET;
	}
}
