package mrbysco.forcecraft.container.engine.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OutputSlot extends SlotItemHandler {

	public OutputSlot(IItemHandler handler, int index, int posX, int posY){
		super(handler, index, posX, posY);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return getItemHandler().isItemValid(1, stack);
	}
}
