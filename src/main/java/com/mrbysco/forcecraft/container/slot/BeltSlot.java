package com.mrbysco.forcecraft.container.slot;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.ItemStack;
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
	public boolean mayPlace(@Nonnull ItemStack stack) {
		return !(stack.getItem() instanceof ForcePackItem || stack.getItem() instanceof ForceBeltItem) && stack.is(ForceTags.VALID_FORCE_BELT);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public int getMaxStackSize(@Nonnull ItemStack stack) {
		return 1;
	}

	@Override
	public void set(@Nonnull ItemStack stack) {
		super.set(stack);
	}
}
