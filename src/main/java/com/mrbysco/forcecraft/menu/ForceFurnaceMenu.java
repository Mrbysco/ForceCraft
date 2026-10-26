package com.mrbysco.forcecraft.menu;

import com.mrbysco.forcecraft.blockentity.furnace.ForceFurnaceBlockEntity;
import com.mrbysco.forcecraft.menu.slot.UpgradeSlot;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class ForceFurnaceMenu extends AbstractFurnaceMenu {
	public static final int UPGRADE_SLOT_INDEX = 39;
	private final ForceFurnaceBlockEntity furnace;

	public ForceFurnaceMenu(int containerId, Inventory inventory, FriendlyByteBuf data) {
		this(containerId, inventory, getBlockEntity(inventory, data));
	}

	public ForceFurnaceMenu(int containerId, Inventory inventory, ForceFurnaceBlockEntity furnace) {
		super(ForceMenus.FORCE_FURNACE.get(), RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT,
				RecipeBookType.FURNACE, containerId, inventory, furnace, furnace.getDataAccess());
		this.furnace = furnace;
		this.addSlot(new UpgradeSlot(furnace.getUpgradeHandler(), 0, 12, 12));
	}

	private static ForceFurnaceBlockEntity getBlockEntity(Inventory inventory, FriendlyByteBuf data) {
		Objects.requireNonNull(inventory, "inventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		BlockEntity be = inventory.player.level().getBlockEntity(data.readBlockPos());
		if (be instanceof ForceFurnaceBlockEntity furnace) {
			return furnace;
		}
		throw new IllegalStateException("Block entity is not correct! " + be);
	}

	@Override
	protected boolean canSmelt(ItemStack stack) {
		if (this.level instanceof ServerLevel serverLevel) {
			return furnace.hasRecipeFor(stack, serverLevel);
		}
		return super.canSmelt(stack);
	}

	public static boolean isUpgrade(ItemStack stack) {
		return stack.is(ForceTags.Items.UPGRADE_CORES);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		if (slotIndex == UPGRADE_SLOT_INDEX) {
			return ItemStack.EMPTY; // cores can't be shift-clicked out
		}

		Slot slot = this.slots.get(slotIndex);
		if (slot.hasItem() && isUpgrade(slot.getItem()) && slotIndex != UPGRADE_SLOT_INDEX) {
			ItemStack stack = slot.getItem();
			ItemStack copy = stack.copy();
			if (!this.moveItemStackTo(stack, UPGRADE_SLOT_INDEX, UPGRADE_SLOT_INDEX + 1, false)) {
				return ItemStack.EMPTY;
			}
			if (stack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			slot.onTake(player, stack);
			return copy;
		}

		return super.quickMoveStack(player, slotIndex);
	}

	@Override
	public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
		if (containerInput == ContainerInput.PICKUP_ALL && isUpgrade(this.getCarried())) {
			return;
		}
		super.clicked(slotIndex, buttonNum, containerInput, player);
	}
}
