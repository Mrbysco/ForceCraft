package com.mrbysco.forcecraft.menu.slot;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

public class BeltSlot extends ResourceHandlerSlot {

	public BeltSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
		super(handler, slotModifier, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		return !(stack.getItem() instanceof ForcePackItem || stack.getItem() instanceof ForceBeltItem) && stack.is(ForceTags.VALID_FORCE_BELT);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public int getMaxStackSize(@NotNull ItemStack stack) {
		return 1;
	}
}
