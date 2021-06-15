package mrbysco.forcecraft.container.engine;

import mrbysco.forcecraft.container.engine.slot.FuelSlot;
import mrbysco.forcecraft.container.engine.slot.ThrottleSlot;
import mrbysco.forcecraft.registry.ForceContainers;
import mrbysco.forcecraft.tiles.ForceEngineTile;
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
	public final int[] tankAmount = new int[2];

	private ForceEngineTile tile;
	private PlayerEntity player;

	public ForceEngineContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data));
	}

	private static ForceEngineTile getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());

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
		this.addSlot(new FuelSlot(te.handler, 0, 42, 33));

		//Throttle slot
		this.addSlot(new ThrottleSlot(te.throttleHandler, 0, 118, 33));

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

		this.tankAmount[0] = tile.getFuelAmount();
		this.tankAmount[1] = tile.getThrottleAmount();
		this.trackInt(IntReferenceHolder.create(this.tankAmount, 0));
		this.trackInt(IntReferenceHolder.create(this.tankAmount, 1));
	}

	public ForceEngineTile getTile() {
		return tile;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.tile.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			final int tileSize = 2;

			if(itemstack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
				if(itemstack.getMaxStackSize() > 1) {
					return ItemStack.EMPTY;
				}
			}

			if (index < tileSize) {
				if (!this.mergeItemStack(itemstack1, tileSize, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, tileSize, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		this.tankAmount[0] = tile.getFuelAmount();
		this.tankAmount[1] = tile.getThrottleAmount();
	}
}