package com.mrbysco.forcecraft.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class FindingUtil {
	public static ItemStack findInstanceStack(PlayerEntity player, Predicate<ItemStack> itemPredicate) {
		PlayerInventory playerInventory = player.inventory;
		if(itemPredicate.test(player.getHeldItemMainhand())) {
			return player.getHeldItemMainhand();
		} else if(itemPredicate.test(player.getHeldItemOffhand())) {
			return player.getHeldItemOffhand();
		}

		for (int i = 0; i <= PlayerInventory.getHotbarSize(); i++) {
			ItemStack stack = playerInventory.getStackInSlot(i);
			if (itemPredicate.test(stack))
				return stack;
		}
		return ItemStack.EMPTY;
	}

	public static boolean hasSingleStackInHotbar(PlayerEntity player, Predicate<ItemStack> itemPredicate) {
		PlayerInventory playerInventory = player.inventory;
		int amountFound = 0;
		if(itemPredicate.test(player.getHeldItemOffhand())) {
			amountFound++;
		}

		for (int i = 0; i <= PlayerInventory.getHotbarSize(); i++) {
			ItemStack stack = playerInventory.getStackInSlot(i);
			if (itemPredicate.test(stack))
				amountFound++;
		}

		return amountFound == 1;
	}
}
