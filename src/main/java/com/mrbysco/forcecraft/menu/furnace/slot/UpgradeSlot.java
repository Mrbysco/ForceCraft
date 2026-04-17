package com.mrbysco.forcecraft.menu.furnace.slot;

import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class UpgradeSlot extends ResourceHandlerSlot {
	private final int slotIndex;

	public UpgradeSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
		super(handler, slotModifier, index, xPosition, yPosition);
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
	public boolean mayPickup(Player playerIn) {
		return true;
	}

	public static boolean isUpgrade(ItemStack stack) {
		return stack.getItem() instanceof UpgradeCoreItem;
	}
}
