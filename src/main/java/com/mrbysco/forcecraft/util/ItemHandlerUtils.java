package com.mrbysco.forcecraft.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
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

	public static boolean isEmpty(IItemHandler itemHandler) {
		if(itemHandler == null) return true;

		for(int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if(!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static int getUsedSlots(IItemHandler itemHandler) {
		if(itemHandler == null) return 0;

		int usedSlots = 0;
		for(int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if(!stack.isEmpty()) {
				usedSlots++;
			}
		}
		return usedSlots;
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



	public static ItemStack getAndSplit(IItemHandler itemhandler, int index, int amount) {
		return index >= 0 && index < itemhandler.getSlots() && !itemhandler.getStackInSlot(index).isEmpty() && amount > 0 ? itemhandler.getStackInSlot(index).split(amount) : ItemStack.EMPTY;
	}

	public static ItemStack getAndRemove(IItemHandler itemhandler, int index) {
		if(index >= 0 && index < itemhandler.getSlots() && itemhandler instanceof IItemHandlerModifiable) {
			IItemHandlerModifiable modifiable = ((IItemHandlerModifiable)itemhandler);
			modifiable.setStackInSlot(index, ItemStack.EMPTY);
			return modifiable.getStackInSlot(index);
		} else {
			return ItemStack.EMPTY;
		}
	}
}
