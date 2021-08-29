package com.mrbysco.forcecraft.container.slot;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BeltSlot extends SlotItemHandler {
	private final int index;

	public BeltSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.index = index;
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		return !(stack.getItem() instanceof ForcePackItem || stack.getItem() instanceof ForceBeltItem) && stack.getItem().isIn(ForceTags.VALID_FORCE_BELT);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public int getItemStackLimit(@Nonnull ItemStack stack) {
		return 1;
	}

	@Override
	public void putStack(@Nonnull ItemStack stack) {
		super.putStack(stack);
	}
}
