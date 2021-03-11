package mrbysco.forcecraft.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.function.Predicate;

public class ItemHandlerUtils {
	public static ItemStack getFirstItem(IItemHandler itemHandler) {
		if(itemHandler == null) return ItemStack.EMPTY;

		for(int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if(!stack.isEmpty()) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	public static boolean hasItems(IItemHandler itemHandler) {
		if(itemHandler == null) return false;

		for(int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if(!stack.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFull(IItemHandler itemHandler) {
		if(itemHandler == null) return true;

		for(int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if(stack.isEmpty() || (stack.getCount() < stack.getMaxStackSize())) {
				return false;
			}
		}
		return true;
	}

	public static boolean extractStackFromPlayer(PlayerInventory inventory, IItemHandler targetHandler, Predicate<ItemStack> stackPredicate) {
		if(targetHandler != null) {
			for(int i = 0; i < inventory.getSizeInventory(); i++) {
				ItemStack stack = inventory.getStackInSlot(i);
				if(stackPredicate.test(stack)) {
					ItemStack restStack = ItemHandlerHelper.insertItem(targetHandler, stack, false);
					inventory.setInventorySlotContents(i, restStack);
					return true;
				}
			}
		}
		return false;
	}
}
