package com.mrbysco.forcecraft.components.storage;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.NotNull;

public class BeltStackHandler extends ItemStacksResourceHandler {
	public BeltStackHandler() {
		super(8);
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return ForceBeltItem.filter(resource);
	}

	@Override
	protected void onContentsChanged(int index, ItemStack previousContents) {
		StorageManager.getBelts().setDirty();
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		return ForceBeltItem.filter(stack);
	}

	@Override
	protected void onContentsChanged(int slot) {
		StorageManager.getBelts().setDirty();
	}
}
