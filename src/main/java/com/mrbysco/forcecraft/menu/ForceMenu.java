package com.mrbysco.forcecraft.menu;

import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.storage.ForceStackHandler;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForceMenu extends AbstractContainerMenu {
	private ItemStack heldStack;

	public ForceMenu(int id, @NotNull Inventory playerInventory) {
		this(id, playerInventory, getForceInventory(playerInventory));
	}

	public static ItemStack getForceInventory(@NotNull Inventory playerInventory) {
		Player player = playerInventory.player;
		if (player.getMainHandItem().has(ForceComponents.FORCE_SLOT)) {
			return player.getMainHandItem();
		} else if (player.getOffhandItem().has(ForceComponents.FORCE_SLOT)) {
			return player.getOffhandItem();
		}
		return ItemStack.EMPTY;
	}

	public ForceMenu(int id, @NotNull Inventory playerInventory, @NotNull ItemStack forceStack) {
		this(id, playerInventory, forceStack, forceStack.isEmpty() ? null : ItemAccess.forStack(forceStack).getCapability(Capabilities.Item.ITEM));
	}

	public ForceMenu(int id, @NotNull Inventory playerInventory, @NotNull ItemStack forceStack,
	                 @Nullable ResourceHandler<ItemResource> forceInventory) {
		super(ForceMenus.FORCE.get(), id);
		this.heldStack = forceStack;
		if (heldStack.isEmpty()) {
			playerInventory.player.closeContainer();
			return;
		}

		if (forceInventory instanceof ForceStackHandler accessItemHandler) {
			this.addSlot(new ResourceHandlerSlot(accessItemHandler, accessItemHandler.indexModifier(heldStack), 0, 98, 40));
		} else {
			ForceStackHandler tempInv = new ForceStackHandler(ItemAccess.forStack(heldStack), heldStack);
			this.addSlot(new ResourceHandlerSlot(tempInv, tempInv.indexModifier(heldStack), 0, 98, 40));
		}

		//Player Inventory
		int xPos = 8;
		int yPos = 97;

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}

		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
		}
	}

	public int getStoredForce() {
		return this.heldStack.getOrDefault(ForceComponents.FORCE_STORED, 0);
	}

	@NotNull
	@Override
	public ItemStack quickMoveStack(@NotNull Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;

		Slot slot = slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (itemstack.has(ForceComponents.FORCE_SLOT))
				return ItemStack.EMPTY;

			int containerSlots = slots.size() - player.getInventory().getNonEquipmentItems().size();

			if (index < containerSlots) {
				if (!this.moveItemStackTo(itemstack1, containerSlots, slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false))
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

	@Override
	public boolean stillValid(Player player) {
		return !heldStack.isEmpty() && !player.isSpectator();
	}
}
