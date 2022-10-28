package com.mrbysco.forcecraft.container.engine;

import com.mrbysco.forcecraft.container.engine.slot.FuelSlot;
import com.mrbysco.forcecraft.container.engine.slot.OutputSlot;
import com.mrbysco.forcecraft.container.engine.slot.ThrottleSlot;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.tiles.ForceEngineTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.Objects;

public class ForceEngineContainer extends Container {
	private ForceEngineTile tile;
	private PlayerEntity player;

	public ForceEngineContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data));
	}

	private static ForceEngineTile getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final TileEntity tileAtPos = playerInventory.player.level.getBlockEntity(data.readBlockPos());

		if (tileAtPos instanceof ForceEngineTile) {
			return (ForceEngineTile) tileAtPos;
		}

		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	public ForceEngineContainer(int id, PlayerInventory playerInventoryIn, ForceEngineTile te) {
		super(ForceContainers.FORCE_ENGINE.get(), id);
		this.tile = te;
		this.player = playerInventoryIn.player;

		//Fuel slot
		this.addSlot(new FuelSlot(te.inputHandler, 0, 42, 23));
		this.addSlot(new ThrottleSlot(te.inputHandler, 1, 118, 23));
		//Output slot
		this.addSlot(new OutputSlot(te.outputHandler, 0, 42, 42));
		this.addSlot(new OutputSlot(te.outputHandler, 1, 118, 42));

		//player inventory here
		int xPos = 8;
		int yPos = 79;
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInventoryIn, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}

		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInventoryIn, x, xPos + x * 18, yPos + 58));
		}

		trackFluids();
	}

	private void trackFluids() {
		// Dedicated server ints are actually truncated to short so we need to split our integer here (split our 32 bit integer into two 16 bit integers)
		//Fuel Tank
		addDataSlot(new IntReferenceHolder() {
			@Override
			public int get() {
				return tile.getFuelAmount() & 0xffff;
			}

			@Override
			public void set(int value) {
				int fluidStored = tile.getFuelAmount() & 0xffff0000;
				tile.setFuelAmount(fluidStored + (value & 0xffff));
			}
		});
		addDataSlot(new IntReferenceHolder() {
			@Override
			public int get() {
				return (tile.getFuelAmount() >> 16) & 0xffff;
			}

			@Override
			public void set(int value) {
				int fluidStored = tile.getFuelAmount() & 0x0000ffff;
				tile.setFuelAmount(fluidStored | (value << 16));
			}
		});

		//Throttle Tank
		addDataSlot(new IntReferenceHolder() {
			@Override
			public int get() {
				return tile.getThrottleAmount() & 0xffff;
			}

			@Override
			public void set(int value) {
				int fluidStored = tile.getThrottleAmount() & 0xffff0000;
				tile.setThrottleAmount(fluidStored + (value & 0xffff));
			}
		});
		addDataSlot(new IntReferenceHolder() {
			@Override
			public int get() {
				return (tile.getThrottleAmount() >> 16) & 0xffff;
			}

			@Override
			public void set(int value) {
				int fluidStored = tile.getThrottleAmount() & 0x0000ffff;
				tile.setThrottleAmount(fluidStored | (value << 16));
			}
		});
	}

	public ForceEngineTile getTile() {
		return tile;
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return this.tile.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			final int tileSize = 2;

			if (itemstack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
				if (itemstack.getMaxStackSize() > 1) {
					return ItemStack.EMPTY;
				}
			}

			if (index < tileSize) {
				if (!this.moveItemStackTo(itemstack1, tileSize, slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, tileSize, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
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
	public void broadcastChanges() {
		super.broadcastChanges();
	}
}
