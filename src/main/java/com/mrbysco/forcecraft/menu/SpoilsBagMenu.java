package com.mrbysco.forcecraft.menu;

import com.mrbysco.forcecraft.items.SpoilsBagItem;
import com.mrbysco.forcecraft.registry.ForceContainers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SpoilsBagMenu extends AbstractContainerMenu {

	private ItemStack heldStack;

	@Override
	public boolean stillValid(Player playerIn) {
		return !playerIn.isSpectator() && !heldStack.isEmpty();
	}

	public SpoilsBagMenu(int id, Inventory playerInventory) {
		this(id, playerInventory, getSpoilsBag(playerInventory));
	}

	public static ItemStack getSpoilsBag(Inventory playerInventory) {
		Player player = playerInventory.player;
		if (player.getMainHandItem().getItem() instanceof SpoilsBagItem) {
			return player.getMainHandItem();
		} else if (player.getOffhandItem().getItem() instanceof SpoilsBagItem) {
			return player.getOffhandItem();
		}
		return ItemStack.EMPTY;
	}

	public SpoilsBagMenu(int id, Inventory playerInventory, ItemStack forceBelt) {
		super(ForceContainers.SPOILS_BAG.get(), id);
		if (forceBelt == null || forceBelt.isEmpty()) {
			playerInventory.player.closeContainer();
			return;
		}

		this.heldStack = forceBelt;
		int xPosC = 17;
		int yPosC = 20;
		//Maxes at 40

		IItemHandler itemHandler = forceBelt.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (itemHandler != null) {
			for (int k = 0; k < 8; ++k) {
				this.addSlot(new SlotItemHandler(itemHandler, k, xPosC + k * 18, yPosC) {
					@Override
					public boolean mayPlace(@Nonnull ItemStack stack) {
						return false;
					}
				});
			}

			//Player Inventory
			int xPos = 8;
			int yPos = 54;

			for (int y = 0; y < 3; ++y) {
				for (int x = 0; x < 9; ++x) {
					this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
				}
			}

			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
			}
		} else {
			playerInventory.player.closeContainer();
		}
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
	}

	//Credit to Shadowfacts for this method
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;

		if (index <= 8) {
			Slot slot = slots.get(index);

			if (slot != null && slot.hasItem()) {
				ItemStack itemstack1 = slot.getItem();
				itemstack = itemstack1.copy();

				if (itemstack.getItem() instanceof SpoilsBagItem)
					return ItemStack.EMPTY;

				int containerSlots = slots.size() - player.getInventory().items.size();

				if (index < containerSlots) {
					if (!this.moveItemStackTo(itemstack1, containerSlots, slots.size(), true)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false)) {
					return ItemStack.EMPTY;
				}

				if (itemstack1.getCount() == 0) {
					slot.set(ItemStack.EMPTY);
				} else {
					slot.setChanged();
				}

				if (itemstack1.getCount() == itemstack.getCount()) {
					return ItemStack.EMPTY;
				}

				slot.onTake(player, itemstack1);
			}
		}

		return itemstack;
	}

}
