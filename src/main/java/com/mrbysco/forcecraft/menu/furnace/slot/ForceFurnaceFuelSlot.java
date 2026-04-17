package com.mrbysco.forcecraft.menu.furnace.slot;

import com.mrbysco.forcecraft.menu.furnace.AbstractForceFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class ForceFurnaceFuelSlot extends ResourceHandlerSlot {
	private final AbstractForceFurnaceMenu furnaceContainer;

	public ForceFurnaceFuelSlot(AbstractForceFurnaceMenu furnaceContainer, ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int slotIndex, int xPosition, int yPosition) {
		super(handler, slotModifier, slotIndex, xPosition, yPosition);
		this.furnaceContainer = furnaceContainer;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	public boolean mayPlace(ItemStack stack) {
		return this.furnaceContainer.isFuel(stack) || isBucket(stack);
	}

	public int getMaxStackSize(ItemStack stack) {
		return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
	}

	public static boolean isBucket(ItemStack stack) {
		return stack.getItem() == Items.BUCKET;
	}
}
