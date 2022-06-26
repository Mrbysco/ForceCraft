package com.mrbysco.forcecraft.menu;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.menu.slot.BeltSlot;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.util.FindingUtil;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ForceBeltMenu extends AbstractContainerMenu {

	private ItemStack heldStack;
	private IItemHandler itemHandler;

	@Override
	public boolean stillValid(Player playerIn) {
		return !playerIn.isSpectator() && !heldStack.isEmpty();
	}

	public static ForceBeltMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
		return new ForceBeltMenu(windowId, playerInventory, new ItemStackHandler(8) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return ForceBeltItem.filter(stack);
			}
		});
	}

	public ForceBeltMenu(int id, Inventory playerInventory, IItemHandler handler) {
		super(ForceContainers.FORCE_BELT.get(), id);
		this.heldStack = FindingUtil.findInstanceStack(playerInventory.player, (stack) -> stack.getItem() instanceof ForceBeltItem);
		if (heldStack == null || heldStack.isEmpty()) {
			playerInventory.player.closeContainer();
			return;
		}

		int xPosC = 17;
		int yPosC = 20;
		//Maxes at 40

		itemHandler = handler;
		if (itemHandler != null) {
			for (int k = 0; k < 8; ++k) {
				this.addSlot(new BeltSlot(itemHandler, k, xPosC + k * 18, yPosC));
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
		if (itemHandler != null) {
			CompoundTag tag = heldStack.getOrCreateTag();
			tag.putInt(ForcePackItem.SLOTS_USED, ItemHandlerUtils.getUsedSlots(itemHandler));
			tag.putInt(ForcePackItem.SLOTS_TOTAL, itemHandler.getSlots());
			heldStack.setTag(tag);
		}

		super.removed(playerIn);
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
		if (slotId >= 0) {
			if (getSlot(slotId).getItem().getItem() instanceof ForceBeltItem)
				return;
		}
		if (clickTypeIn == ClickType.SWAP)
			return;
		super.clicked(slotId, dragType, clickTypeIn, player);
	}

	//Credit to Shadowfacts for this method
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (itemstack.getItem() instanceof ForceBeltItem)
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

		return itemstack;
	}

}
