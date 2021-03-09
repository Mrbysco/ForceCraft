package mrbysco.forcecraft.container.slot;

import mrbysco.forcecraft.items.UpgradeCoreItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotUpgradeCore extends Slot {

    public SlotUpgradeCore(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof UpgradeCoreItem;
    }
}
