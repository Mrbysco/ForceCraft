package com.mrbysco.forcecraft.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class FindingUtil {
	public static ItemStack findInstanceStack(Player player, Predicate<ItemStack> itemPredicate) {
		Inventory playerInventory = player.getInventory();
		if (itemPredicate.test(player.getMainHandItem())) {
			return player.getMainHandItem();
		} else if (itemPredicate.test(player.getOffhandItem())) {
			return player.getOffhandItem();
		}

		for (int i = 0; i <= Inventory.getSelectionSize(); i++) {
			ItemStack stack = playerInventory.getItem(i);
			if (itemPredicate.test(stack))
				return stack;
		}
		return ItemStack.EMPTY;
	}

	public static boolean hasSingleStackInHotbar(Player player, Predicate<ItemStack> itemPredicate) {
		Inventory playerInventory = player.getInventory();
		int amountFound = 0;
		if (itemPredicate.test(player.getOffhandItem())) {
			amountFound++;
		}

		for (int i = 0; i <= Inventory.getSelectionSize(); i++) {
			ItemStack stack = playerInventory.getItem(i);
			if (itemPredicate.test(stack))
				amountFound++;
		}

		return amountFound == 1;
	}
}
