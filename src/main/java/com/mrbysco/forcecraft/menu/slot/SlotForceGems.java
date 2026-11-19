package com.mrbysco.forcecraft.menu.slot;

import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SlotForceGems extends SlotItemHandler {

	public SlotForceGems(IItemHandler handler, int index, int posX, int posY) {
		super(handler, index, posX, posY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.is(ForceTags.Items.GEMS_FORCE);
	}

	@Override
	public int getMaxStackSize(@NotNull ItemStack stack) {
		return 64;
	}
}
