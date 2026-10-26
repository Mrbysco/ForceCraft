package com.mrbysco.forcecraft.menu.slot;

import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class UpgradeSlot extends ResourceHandlerSlot {

	public UpgradeSlot(ItemStacksResourceHandler handler, int index, int posX, int posY) {
		super(handler, handler::set, index, posX, posY);
	}

	public boolean mayPlace(ItemStack stack) {
		return isUpgrade(stack) && getItem().isEmpty();
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}

	@Override
	public boolean mayPickup(Player player) {
		return player.isShiftKeyDown();
	}

	public static boolean isUpgrade(ItemStack stack) {
		return stack.is(ForceTags.Items.UPGRADE_CORES);
	}
}
