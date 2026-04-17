package com.mrbysco.forcecraft.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.function.Predicate;

public class ItemHandlerUtils {
	public static boolean hasItems(ResourceHandler<ItemResource> itemHandler) {
		if (itemHandler == null) return false;

		for (int i = 0; i < itemHandler.size(); i++) {
			ItemResource stack = itemHandler.getResource(i);
			if (!stack.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFull(ResourceHandler<ItemResource> itemHandler) {
		if (itemHandler == null) return true;

		for (int i = 0; i < itemHandler.size(); i++) {
			ItemResource stack = itemHandler.getResource(i);
			if (stack.isEmpty() || (itemHandler.getAmountAsInt(i) < stack.getMaxStackSize())) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(ResourceHandler<ItemResource> itemHandler) {
		if (itemHandler == null) return true;

		for (int i = 0; i < itemHandler.size(); i++) {
			ItemResource stack = itemHandler.getResource(i);
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static int getUsedSlots(ResourceHandler<ItemResource> itemHandler) {
		if (itemHandler == null) return 0;

		int usedSlots = 0;
		for (int i = 0; i < itemHandler.size(); i++) {
			ItemResource stack = itemHandler.getResource(i);
			if (!stack.isEmpty()) {
				usedSlots++;
			}
		}
		return usedSlots;
	}

	public static boolean extractStackFromPlayer(Inventory inventory, ResourceHandler<ItemResource> targetHandler, Predicate<ItemStack> stackPredicate) {
		if (targetHandler != null) {
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				ItemStack stack = inventory.getItem(i);
				if (stackPredicate.test(stack)) {
					try (Transaction tx = Transaction.openRoot()) {
						if (targetHandler.insert(ItemResource.of(stack), stack.getCount(), tx) != stack.getCount())
							continue;

						tx.commit();
						inventory.setItem(i, ItemStack.EMPTY);
					}
					return true;
				}
			}
		}
		return false;
	}


//	public static ItemStack getAndSplit(ResourceHandler<ItemResource> itemhandler, int index, int amount) {
//		return index >= 0 && index < itemhandler.size() && !itemhandler.getResource(index).isEmpty() &&
//				amount > 0 ? itemhandler.getResource(index).toStack(amount) : ItemStack.EMPTY;
//	}

//	public static ItemStack getAndRemove(ResourceHandler<ItemResource> itemhandler, int index) {
//		if (index >= 0 && index < itemhandler.size() && itemhandler instanceof IItemHandlerModifiable modifiable) {
//			modifiable.setStackInSlot(index, ItemStack.EMPTY);
//			return modifiable.getStackInSlot(index);
//		} else {
//			return ItemStack.EMPTY;
//		}
//	}
}
