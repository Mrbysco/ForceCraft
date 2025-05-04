package com.mrbysco.forcecraft.menu.slot;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BeltSlot extends SlotItemHandler {

	public BeltSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		return !(stack.getItem() instanceof ForcePackItem || stack.getItem() instanceof ForceBeltItem) && stack.is(ForceTags.VALID_FORCE_BELT);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public int getMaxStackSize(@NotNull ItemStack stack) {
		return 1;
	}

	@Override
	public void set(@NotNull ItemStack stack) {
		super.set(stack);
	}
}
