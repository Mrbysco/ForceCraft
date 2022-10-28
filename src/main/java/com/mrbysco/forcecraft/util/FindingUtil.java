package com.mrbysco.forcecraft.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class FindingUtil {
	public static ItemStack findInstanceStack(PlayerEntity player, Predicate<ItemStack> itemPredicate) {
		PlayerInventory playerInventory = player.inventory;
		if (itemPredicate.test(player.getMainHandItem())) {
			return player.getMainHandItem();
		} else if (itemPredicate.test(player.getOffhandItem())) {
			return player.getOffhandItem();
		}

		for (int i = 0; i <= PlayerInventory.getSelectionSize(); i++) {
			ItemStack stack = playerInventory.getItem(i);
			if (itemPredicate.test(stack))
				return stack;
		}
		return ItemStack.EMPTY;
	}

	public static boolean hasSingleStackInHotbar(PlayerEntity player, Predicate<ItemStack> itemPredicate) {
		PlayerInventory playerInventory = player.inventory;
		int amountFound = 0;
		if (itemPredicate.test(player.getOffhandItem())) {
			amountFound++;
		}

		for (int i = 0; i <= PlayerInventory.getSelectionSize(); i++) {
			ItemStack stack = playerInventory.getItem(i);
			if (itemPredicate.test(stack))
				amountFound++;
		}

		return amountFound == 1;
	}
}
