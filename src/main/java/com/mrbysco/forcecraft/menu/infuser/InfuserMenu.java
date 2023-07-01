package com.mrbysco.forcecraft.menu.infuser;

import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.menu.slot.SlotForceGems;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.util.AdvancementUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InfuserMenu extends AbstractContainerMenu {

	private InfuserBlockEntity tile;
	private Player player;

	public final int[] validRecipe = new int[1];

	public InfuserMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data));
	}

	private static InfuserBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final BlockEntity tileAtPos = playerInventory.player.level.getBlockEntity(data.readBlockPos());

		if (tileAtPos instanceof InfuserBlockEntity) {
			return (InfuserBlockEntity) tileAtPos;
		}

		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	public InfuserMenu(int id, Inventory playerInventoryIn, InfuserBlockEntity te) {
		super(ForceMenus.INFUSER.get(), id);
		this.tile = te;
		this.player = playerInventoryIn.player;

		//Modifier Slots [0, 7] around the outside starting at the top middle going clockwise
		this.addSlot(new UnlockableSlot(te.handler, 0, 80, 20));
		this.addSlot(new UnlockableSlot(te.handler, 1, 104, 32));
		this.addSlot(new UnlockableSlot(te.handler, 2, 116, 57));
		this.addSlot(new UnlockableSlot(te.handler, 3, 104, 81));
		this.addSlot(new UnlockableSlot(te.handler, 4, 80, 93));
		this.addSlot(new UnlockableSlot(te.handler, 5, 56, 81));
		this.addSlot(new UnlockableSlot(te.handler, 6, 44, 57));
		this.addSlot(new UnlockableSlot(te.handler, 7, 56, 32));

		//Tools Slot in the middle
		this.addSlot(new MatrixUpdatingSlot(te.handler, InfuserBlockEntity.SLOT_TOOL, 80, 57));

		//Force Gem Slot top left
		this.addSlot(new SlotForceGems(te.handler, InfuserBlockEntity.SLOT_GEM, 8, 23));

		//Book Upgrade Slot top left
		this.addSlot(new MatrixUpdatingSlot(te.handler, InfuserBlockEntity.SLOT_BOOK, 8, 5));

		//player inventory here
		int xPos = 8;
		int yPos = 127;
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInventoryIn, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}

		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInventoryIn, x, xPos + x * 18, yPos + 58));
		}

		trackPower();
		trackFluid();

		this.validRecipe[0] = tile.hasValidRecipe() ? 1 : 0;
		this.addDataSlot(DataSlot.shared(this.validRecipe, 0));

		//set data that will sync server->client WITHOUT needing to call .markDirty()
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return tile.processTime;
			}

			@Override
			public void set(int value) {
				tile.processTime = value;
			}
		});
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return tile.maxProcessTime;
			}

			@Override
			public void set(int value) {
				tile.maxProcessTime = value;
			}
		});
		AdvancementUtil.unlockTierAdvancements(player, tile.getBookTier());
	}

	private void trackFluid() {
		// Dedicated server ints are actually truncated to short so we need to split our integer here (split our 32 bit integer into two 16 bit integers)
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return tile.getFluidAmount() & 0xffff;
			}

			@Override
			public void set(int value) {
				int fluidStored = tile.getFluidAmount() & 0xffff0000;
				tile.setFluidAmount(fluidStored + (value & 0xffff));
			}
		});
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return (tile.getFluidAmount() >> 16) & 0xffff;
			}

			@Override
			public void set(int value) {
				int fluidStored = tile.getFluidAmount() & 0x0000ffff;
				tile.setFluidAmount(fluidStored | (value << 16));
			}
		});
	}

	private void trackPower() {
		// Dedicated server ints are actually truncated to short so we need to split our integer here (split our 32 bit integer into two 16 bit integers)
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return tile.getEnergyStored() & 0xffff;
			}

			@Override
			public void set(int value) {
				int energyStored = tile.getEnergyStored() & 0xffff0000;
				tile.setEnergyStored(energyStored + (value & 0xffff));
			}
		});
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return (tile.getEnergyStored() >> 16) & 0xffff;
			}

			@Override
			public void set(int value) {
				int energyStored = tile.getEnergyStored() & 0x0000ffff;
				tile.setEnergyStored(energyStored | (value << 16));
			}
		});
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return this.tile.stillValid(playerIn) && !playerIn.isSpectator();
	}

	@Override
	@Nonnull
	public ItemStack quickMoveStack(Player playerIn, int index) {

		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			final int tileSize = 11;

			if (index < tileSize) {
				if (!this.moveItemStackTo(itemstack1, tileSize, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				// change to fix all the manual moving. and fix books going to book slots, etc
				if (!this.moveItemStackTo(itemstack1, 0, tileSize, true)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		this.slotsChanged(null);

		return itemstack;
	}

	public boolean isWorkAllowed() {
		return tile.isWorkAllowed();
	}

	public InfuserBlockEntity getTile() {
		return tile;
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if(id == 0) {
			getTile().startWork();
		}
		return super.clickMenuButton(player, id);
	}

	@Override
	public void slotsChanged(Container inventoryIn) {
		if (inventoryIn != null) {
			super.slotsChanged(inventoryIn);
		}
		getTile().setChanged();
		if (!player.level.isClientSide) {
			this.validRecipe[0] = tile.updateValidRecipe() ? 1 : 0;
		}
		AdvancementUtil.unlockTierAdvancements(player, tile.getBookTier());
	}

	public class MatrixUpdatingSlot extends SlotItemHandler {
		public MatrixUpdatingSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public void setChanged() {
			super.setChanged();
			slotsChanged(null);
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}
	}

	public class UnlockableSlot extends MatrixUpdatingSlot {
		public UnlockableSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean isActive() {
			return slot <= tile.getBookTier();
		}

		@Override
		public boolean mayPlace(@Nonnull ItemStack stack) {
			return slot <= tile.getBookTier() && super.mayPlace(stack);
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}
	}
}