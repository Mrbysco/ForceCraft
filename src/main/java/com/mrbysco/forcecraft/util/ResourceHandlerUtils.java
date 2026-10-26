package com.mrbysco.forcecraft.util;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class ResourceHandlerUtils {

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
}
