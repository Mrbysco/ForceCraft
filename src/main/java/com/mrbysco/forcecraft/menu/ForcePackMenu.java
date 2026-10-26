package com.mrbysco.forcecraft.menu;

import com.mrbysco.forcecraft.item.ForceBeltItem;
import com.mrbysco.forcecraft.item.ForcePackItem;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.storage.PackStackHandler;
import com.mrbysco.forcecraft.util.FindingUtil;
import com.mrbysco.forcecraft.util.ResourceHandlerUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

public class ForcePackMenu extends AbstractContainerMenu {

	private ItemStack heldStack;
	private int upgrades;
	private PackStackHandler inventory;

	@Override
	public boolean stillValid(Player playerIn) {
		return !playerIn.isSpectator();
	}

	public static ForcePackMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
		PackStackHandler handler = new PackStackHandler();
		handler.setUpgrades(data.readInt());
		return new ForcePackMenu(windowId, playerInventory, handler);
	}

	public ForcePackMenu(int id, Inventory playerInventory, PackStackHandler inv) {
		super(ForceMenus.FORCE_PACK.get(), id);
		this.heldStack = FindingUtil.findInstanceStack(playerInventory.player, (stack) -> stack.getItem() instanceof ForcePackItem);
		if (heldStack == null || heldStack.isEmpty()) {
			playerInventory.player.closeContainer();
			return;
		}
		inventory = inv;

		upgrades = 0;

		upgrades = inventory.getUpgrades();
		int numRows = upgrades + 1;

		int xPosC = 17;
		int yPosC = 20;
		//Maxes at 40
		for (int j = 0; j < numRows; ++j) {
			for (int k = 0; k < 8; ++k) {
				this.addSlot(new ResourceHandlerSlot(inventory, inventory::set, k + j * 8, xPosC + k * 18, yPosC + j * 18) {
					@Override
					public boolean mayPlace(@NotNull ItemStack stack) {
						return !(stack.getItem() instanceof ForcePackItem || stack.getItem() instanceof ForceBeltItem);
					}
				});
			}
		}

		//Player Inventory
		int xPos = 8;
		int yPos = 36 + (numRows * 18);

		//Slots 9-99
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}

		//Slots 0-8
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
		}
	}

	/**
	 * Handles inventory management and crafting with ItemCards when the menu is closed by the player.
	 *
	 * @param player the player that closed the menu
	 */
	@Override
	public void removed(Player player) {
		if (inventory != null) {
			if (player.level().isClientSide()) return;
			heldStack.set(ForceComponents.SLOTS_USED, ResourceHandlerUtils.getUsedSlots(inventory));
		}

		super.removed(player);
	}

	@Override
	public void clicked(int slotId, int dragType, ContainerInput containerInput, Player player) {
		if (slotId >= 0) {
			if (getSlot(slotId).getItem().getItem() instanceof ForcePackItem)
				return;
		}
		if (containerInput == ContainerInput.SWAP)
			return;

		super.clicked(slotId, dragType, containerInput, player);
	}

	public int getUpgrades() {
		return this.upgrades;
	}

	//Credit to Shadowfacts for this method
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (itemstack.getItem() instanceof ForcePackItem)
				return ItemStack.EMPTY;

			int containerSlots = slots.size() - player.getInventory().getNonEquipmentItems().size();

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
