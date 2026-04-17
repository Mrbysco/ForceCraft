package com.mrbysco.forcecraft.menu;

import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.menu.slot.BeltSlot;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.util.FindingUtil;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class ForceBeltMenu extends AbstractContainerMenu {

	private ItemStack heldStack;
	private ItemStacksResourceHandler itemHandler;

	@Override
	public boolean stillValid(Player playerIn) {
		return !playerIn.isSpectator() && !heldStack.isEmpty();
	}

	public static ForceBeltMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
		return new ForceBeltMenu(windowId, playerInventory, new ItemStacksResourceHandler(8) {
			@Override
			public boolean isValid(int slot, ItemResource stack) {
				return ForceBeltItem.filter(stack);
			}
		});
	}

	public ForceBeltMenu(int id, Inventory playerInventory, ResourceHandler<ItemResource> handler) {
		super(ForceMenus.FORCE_BELT.get(), id);
		this.heldStack = FindingUtil.findInstanceStack(playerInventory.player, (stack) -> stack.getItem() instanceof ForceBeltItem);
		if (heldStack == null || heldStack.isEmpty()) {
			playerInventory.player.closeContainer();
			return;
		}

		int xPosC = 17;
		int yPosC = 20;
		//Maxes at 40

		if (handler instanceof ItemStacksResourceHandler itemStacksHandler) {
			itemHandler = itemStacksHandler;
		}
		if (itemHandler != null) {
			for (int k = 0; k < 8; ++k) {
				this.addSlot(new BeltSlot(itemHandler, itemHandler::set, k, xPosC + k * 18, yPosC));
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
			heldStack.set(ForceComponents.SLOTS_USED, ItemHandlerUtils.getUsedSlots(itemHandler));
			heldStack.set(ForceComponents.SLOTS_TOTAL, itemHandler.size());
		}

		super.removed(playerIn);
	}

	@Override
	public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
		if (slotIndex >= 0) {
			if (getSlot(slotIndex).getItem().getItem() instanceof ForceBeltItem)
				return;
		}
		if (containerInput == containerInput.SWAP)
			return;
		super.clicked(slotIndex, buttonNum, containerInput, player);
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

			int containerSlots = slots.size() - Inventory.INVENTORY_SIZE;

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
