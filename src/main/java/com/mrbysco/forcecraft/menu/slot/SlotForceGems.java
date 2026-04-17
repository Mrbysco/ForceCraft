package com.mrbysco.forcecraft.menu.slot;

import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

public class SlotForceGems extends ResourceHandlerSlot {

	public SlotForceGems(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int posX, int posY) {
		super(handler, slotModifier, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.is(ForceTags.FORCE_GEM);
	}

	@Override
	public int getMaxStackSize(@NotNull ItemStack stack) {
		return 64;
	}
}
