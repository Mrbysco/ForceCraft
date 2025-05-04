package com.mrbysco.forcecraft.menu.slot;

import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SlotForceGems extends SlotItemHandler {

	public SlotForceGems(IItemHandler handler, int index, int posX, int posY) {
		super(handler, index, posX, posY);
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
