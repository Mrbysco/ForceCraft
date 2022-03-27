package com.mrbysco.forcecraft.menu.slot;

import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotForceGems extends SlotItemHandler {

	public SlotForceGems(IItemHandler handler, int index, int posX, int posY) {
		super(handler, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.is(ForceTags.FORGE_GEM);
	}

	@Override
	public int getMaxStackSize(@Nonnull ItemStack stack) {
		return 64;
	}
}
