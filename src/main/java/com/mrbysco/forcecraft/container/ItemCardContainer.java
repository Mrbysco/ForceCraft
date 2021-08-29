package com.mrbysco.forcecraft.container;

import com.mrbysco.forcecraft.items.ItemCardItem;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class ItemCardContainer extends Container {
	private final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
	private final CraftResultInventory craftResult = new CraftResultInventory();
	private final IWorldPosCallable worldPosCallable;
	private final PlayerEntity player;
	private ItemStack heldStack;

	public ItemCardContainer(int id, PlayerInventory playerInventory) {
		this(id, playerInventory, IWorldPosCallable.DUMMY);
	}

	public ItemCardContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
		super(ForceContainers.ITEM_CARD.get(), id);
		this.worldPosCallable = worldPosCallable;
		this.player = playerInventory.player;
		this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35) {
			@Override
			public boolean canTakeStack(PlayerEntity playerIn) {
				return false;
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18){
					@Override
					public int getSlotStackLimit() {
						return 1;
					}

					@Nonnull
					@Override
					public ItemStack onTake(final PlayerEntity player, @Nonnull final ItemStack stack) {
						return ItemStack.EMPTY;
					}

					@Nonnull
					@Override
					public ItemStack decrStackSize(final int par1) {
						return ItemStack.EMPTY;
					}

					@Override
					public boolean isItemValid(final ItemStack par1ItemStack) {
						return false;
					}

					@Override
					public boolean canTakeStack(final PlayerEntity par1PlayerEntity) {
						return false;
					}
				});
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
		}

		heldStack = getCardStack(playerInventory);
		if(heldStack.getItem() == ForceRegistry.ITEM_CARD.get()) {
			CompoundNBT tag = heldStack.getOrCreateTag();
			if(tag.contains("RecipeContents")) {
				CompoundNBT recipeContents = tag.getCompound("RecipeContents");
				if (!player.world.isRemote) {
					for(int i = 0; i < craftMatrix.getSizeInventory(); i++) {
						craftMatrix.setInventorySlotContents(i, ItemStack.read(recipeContents.getCompound("slot_" + i)));
					}
					craftResult.setInventorySlotContents(0, ItemStack.read(recipeContents.getCompound("result")));
				}
			}
		}
	}

	public static ItemStack getCardStack(PlayerInventory playerInventory) {
		PlayerEntity player = playerInventory.player;
		if(player.getHeldItemMainhand().getItem() instanceof ItemCardItem) {
			return player.getHeldItemMainhand();
		} else if(player.getHeldItemOffhand().getItem() instanceof ItemCardItem) {
			return player.getHeldItemOffhand();
		}
		return ItemStack.EMPTY;
	}

	protected void updateCraftingResult(World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory inventoryResult) {
		if (!world.isRemote) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
			final Optional<ICraftingRecipe> iRecipe = serverplayerentity.server.getRecipeManager().getRecipe(IRecipeType.CRAFTING, inventory, world);
			final ItemStack stack;
			if (iRecipe.isPresent() && (iRecipe.get().isDynamic()
					|| !world.getGameRules().getBoolean(GameRules.DO_LIMITED_CRAFTING)
					|| serverplayerentity.getRecipeBook().isUnlocked(iRecipe.get())
					|| player.isCreative()))
			{
				stack = iRecipe.get().getCraftingResult(this.craftMatrix);
				inventoryResult.setInventorySlotContents(0, stack);
				serverplayerentity.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, stack));
			}
			else
			{
				inventoryResult.setInventorySlotContents(0, ItemStack.EMPTY);
				serverplayerentity.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, ItemStack.EMPTY));
			}
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		this.worldPosCallable.consume((world, pos) -> {
			updateCraftingResult(world, this.player, this.craftMatrix, this.craftResult);
		});
	}

	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true && !heldStack.isEmpty();
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		final int total_crafting_slots = 10;
		if (index <= total_crafting_slots) {
			return ItemStack.EMPTY;
		}

		final int total_slots = 46;

		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 0) {
				if (!this.mergeItemStack(itemstack1, total_crafting_slots, total_slots, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			} else if (index < 32) {
				if (!this.mergeItemStack(itemstack1, 32, total_slots, false)) {
					return ItemStack.EMPTY;
				}
			} else if ((index < total_slots
					&& !this.mergeItemStack(itemstack1, total_crafting_slots, 32, false))
					|| !this.mergeItemStack(itemstack1, total_crafting_slots, total_slots, false))
			{
				return ItemStack.EMPTY;
			}
			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
		}
		return itemstack;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		if (slotId >= 1 && slotId < 10) {
			// 1 is shift-click
			if (clickTypeIn == ClickType.PICKUP
					|| clickTypeIn == ClickType.PICKUP_ALL
					|| clickTypeIn == ClickType.SWAP)
			{
				final Slot slot = this.inventorySlots.get(slotId);
				final ItemStack dropping = player.inventory.getItemStack();
				return handleSlotClick(slot, dropping);
			}

			return ItemStack.EMPTY;
		}

		if (clickTypeIn == ClickType.QUICK_MOVE) {
			return ItemStack.EMPTY;
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public ItemStack handleSlotClick(final Slot slotId, final ItemStack stack) {
		if (stack.getCount() > 0) {
			final ItemStack copy = stack.copy();
			copy.setCount(1);
			slotId.putStack(copy);
		} else if (slotId.getStack().getCount() > 0) {
			slotId.putStack(ItemStack.EMPTY);
		}

		return slotId.getStack().copy();
	}

	/**
	 * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
	 * null for the initial slot that was double-clicked.
	 */
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
	}

	public CraftingInventory getCraftMatrix() {
		return craftMatrix;
	}

	public CraftResultInventory getCraftResult() {
		return craftResult;
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public void setMatrixContents(PlayerEntity player, List<ItemStack> stacks) {
		for(int i = 0; i < stacks.size(); i++) {
			handleSlotClick(getSlot(i), stacks.get(i));
		}
	}

	public List<ItemStack> getMatrixContents() {
		return null;
	}
}