package com.mrbysco.forcecraft.storage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.List;

public class BeltStackHandler extends ItemStacksResourceHandler {
	public BeltStackHandler() {
		super(8);
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return false; //ForceBeltItem.filter(resource);
	}

	@Override
	protected void onContentsChanged(int index, ItemStack previousContents) {
		StorageManager.getBelts().setDirty();
	}

	public List<ItemStack> getStacks() {
		return stacks.stream().map(ItemStack::copy).toList();
	}

	public void setStacks(List<ItemStack> list) {
		for (int i = 0; i < stacks.size(); i++) {
			stacks.set(i, i < list.size() ? list.get(i).copy() : ItemStack.EMPTY);
		}
	}
}
