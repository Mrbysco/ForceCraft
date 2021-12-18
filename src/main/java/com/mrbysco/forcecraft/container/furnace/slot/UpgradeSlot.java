package com.mrbysco.forcecraft.container.furnace.slot;

import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class UpgradeSlot extends SlotItemHandler {
	private final int slotIndex;
	public UpgradeSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.slotIndex = index;
	}

	public boolean mayPlace(ItemStack stack) {
		return isUpgrade(stack) && container.getItem(slotIndex).isEmpty();
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}

	@Override
	public boolean mayPickup(PlayerEntity playerIn) {
		return true;
	}

	public static boolean isUpgrade(ItemStack stack) {
		return stack.getItem() instanceof UpgradeCoreItem;
	}
}
