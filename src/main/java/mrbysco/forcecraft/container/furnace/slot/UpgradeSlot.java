package mrbysco.forcecraft.container.furnace.slot;

import mrbysco.forcecraft.items.UpgradeCoreItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class UpgradeSlot extends Slot {
	private final int slotIndex;
	public UpgradeSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.slotIndex = index;
	}

	public boolean isItemValid(ItemStack stack) {
		return isUpgrade(stack) && inventory.getStackInSlot(slotIndex).isEmpty();
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	public ItemStack onTake(PlayerEntity playerIn, ItemStack stack) {
		inventory.setInventorySlotContents(slotIndex, stack.copy());
		stack.shrink(1);
		playerIn.world.playSound((PlayerEntity) null, playerIn.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1.0F, 1.0F);
		return super.onTake(playerIn, stack);
	}

	@Override
	public boolean canTakeStack(PlayerEntity playerIn) {
		return true;
	}

	public static boolean isUpgrade(ItemStack stack) {
		return stack.getItem() instanceof UpgradeCoreItem;
	}
}
