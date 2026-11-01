package com.mrbysco.forcecraft.menu;

import com.mrbysco.forcecraft.blockentity.infuser.InfuserBlockEntity;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.upgrade.UpgradeBookTier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InfuserMenu extends AbstractContainerMenu {

	private InfuserBlockEntity blockEntity;
	private Player player;

	public final int[] validRecipe = new int[1];

	public InfuserMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
		this(windowId, playerInventory, getBlockEntity(playerInventory, data));
	}

	private static InfuserBlockEntity getBlockEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());

		if (tileAtPos instanceof InfuserBlockEntity) {
			return (InfuserBlockEntity) tileAtPos;
		}

		throw new IllegalStateException("Block entity is not correct! " + tileAtPos);
	}

	public InfuserMenu(int id, Inventory playerInventoryIn, InfuserBlockEntity be) {
		super(ForceMenus.INFUSER.get(), id);
		this.blockEntity = be;
		this.player = playerInventoryIn.player;

		//Modifier Slots [0, 7] around the outside starting at the top middle going clockwise
		var handler = be.getItemHandler(null);
		this.addSlot(new UnlockableSlot(handler, 0, 80, 20));
		this.addSlot(new UnlockableSlot(handler, 1, 104, 32));
		this.addSlot(new UnlockableSlot(handler, 2, 116, 57));
		this.addSlot(new UnlockableSlot(handler, 3, 104, 81));
		this.addSlot(new UnlockableSlot(handler, 4, 80, 93));
		this.addSlot(new UnlockableSlot(handler, 5, 56, 81));
		this.addSlot(new UnlockableSlot(handler, 6, 44, 57));
		this.addSlot(new UnlockableSlot(handler, 7, 56, 32));

		//Tools Slot in the middle
		this.addSlot(new MatrixUpdatingSlot(handler, InfuserBlockEntity.SLOT_TOOL, 80, 57));

		//Force Gem Slot top left
		this.addSlot(new SlotForceGems(handler, InfuserBlockEntity.SLOT_GEM, 8, 23));

		//Book Upgrade Slot top left
		this.addSlot(new MatrixUpdatingSlot(handler, InfuserBlockEntity.SLOT_BOOK, 8, 5));

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

		this.validRecipe[0] = blockEntity.hasValidRecipe() ? 1 : 0;
		this.addDataSlot(DataSlot.shared(this.validRecipe, 0));

		//set data that will sync server->client WITHOUT needing to call .markDirty()
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return blockEntity.processTime;
			}

			@Override
			public void set(int value) {
				blockEntity.processTime = value;
			}
		});
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return blockEntity.maxProcessTime;
			}

			@Override
			public void set(int value) {
				blockEntity.maxProcessTime = value;
			}
		});
	}

	private void trackFluid() {
		// Dedicated server ints are actually truncated to short so we need to split our integer here (split our 32 bit integer into two 16 bit integers)
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return blockEntity.getFluidAmount() & 0xffff;
			}

			@Override
			public void set(int value) {
			}
		});
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return (blockEntity.getFluidAmount() >> 16) & 0xffff;
			}

			@Override
			public void set(int value) {
			}
		});
	}

	private int energyLow;
	private int energyHigh;

	private void trackPower() {
		// Dedicated server ints are actually truncated to short so we need to split our integer here (split our 32 bit integer into two 16 bit integers)
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return blockEntity.getEnergyStored() & 0xffff;
			}

			@Override
			public void set(int value) {
				energyLow = value & 0xffff;
			}
		});
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return (blockEntity.getEnergyStored() >> 16) & 0xffff;
			}

			@Override
			public void set(int value) {
				energyHigh = value & 0xffff;
			}
		});
	}

	public int getEnergyStored() {
		return (energyHigh << 16) | energyLow;
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return Container.stillValidBlockEntity(blockEntity, playerIn) && !playerIn.isSpectator();
	}

	@Override
	@NotNull
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			final int inventorySize = 11;

			if (index < inventorySize) {
				if (!this.moveItemStackTo(itemstack1, inventorySize, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				// change to fix all the manual moving. and fix books going to book slots, etc
				if (!moveIntoInfuser(itemstack1)) {
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

	private boolean moveIntoInfuser(ItemStack stack) {
		if (stack.is(ForceRegistry.UPGRADE_TOME.get())) {
			return this.moveItemStackTo(stack, InfuserBlockEntity.SLOT_BOOK, InfuserBlockEntity.SLOT_BOOK + 1, false);
		}
		if (stack.is(ForceTags.Items.GEMS_FORCE)) {
			return this.moveItemStackTo(stack, InfuserBlockEntity.SLOT_GEM, InfuserBlockEntity.SLOT_GEM + 1, false);
		}

		if (stack.is(ForceTags.Items.VALID_INFUSER_TOOLS) || stack.is(ForceTags.Items.VALID_INFUSER_CHARGE)) {
			if (this.moveItemStackTo(stack, InfuserBlockEntity.SLOT_TOOL, InfuserBlockEntity.SLOT_TOOL + 1, false)) {
				return true;
			}
		}

		return this.moveItemStackTo(stack, 0, InfuserBlockEntity.SLOT_TOOL, false);
	}

	public boolean isWorkAllowed() {
		return blockEntity.isWorkAllowed();
	}

	public InfuserBlockEntity getBlockEntity() {
		return blockEntity;
	}

	public UpgradeBookTier getTier() {
		return blockEntity.getBook().tier();
	}

	@Override
	public void broadcastChanges() {
		if (!player.level().isClientSide()) {
			this.validRecipe[0] = blockEntity.hasValidRecipe() ? 1 : 0;
		}
		super.broadcastChanges();
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id == 0) {
			getBlockEntity().startWork(player.getAbilities().instabuild);
		}
		return super.clickMenuButton(player, id);
	}

	@Override
	public void slotsChanged(Container inventoryIn) {
		if (inventoryIn != null) {
			super.slotsChanged(inventoryIn);
		}
		getBlockEntity().setChanged();
		this.validRecipe[0] = blockEntity.hasValidRecipe() ? 1 : 0;
	}

	public static class MatrixUpdatingSlot extends ResourceHandlerSlot {
		public MatrixUpdatingSlot(ItemStacksResourceHandler handler, int index, int xPosition, int yPosition) {
			super(handler, handler::set, index, xPosition, yPosition);
		}

//		@Override
//		public void setChanged() {
//			super.setChanged();
//			slotsChanged(null);
//		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}
	}

	public class UnlockableSlot extends MatrixUpdatingSlot {
		public UnlockableSlot(ItemStacksResourceHandler handler, int index, int xPosition, int yPosition) {
			super(handler, index, xPosition, yPosition);
		}

		@Override
		public boolean isActive() {
			return getSlotIndex() <= blockEntity.getBookTier();
		}

		@Override
		public boolean mayPlace(@NotNull ItemStack stack) {
			return getSlotIndex() <= blockEntity.getBookTier() && super.mayPlace(stack);
		}

		@Override
		public int getMaxStackSize(@NotNull ItemStack stack) {
			return 1;
		}
	}

	public static class SlotForceGems extends ResourceHandlerSlot {

		public SlotForceGems(ItemStacksResourceHandler handler, int index, int posX, int posY) {
			super(handler, handler::set, index, posX, posY);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return stack.is(ForceTags.Items.GEMS_FORCE);
		}

		@Override
		public int getMaxStackSize(@NotNull ItemStack stack) {
			return 64;
		}
	}
}