package com.mrbysco.forcecraft.attachment.storage;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BeltStackHandler extends ItemStackHandler {
	public BeltStackHandler() {
		super(8);
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
