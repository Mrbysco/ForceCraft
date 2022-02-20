package com.mrbysco.forcecraft.menu;

import com.mrbysco.forcecraft.items.ItemCardItem;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class ItemCardMenu extends AbstractContainerMenu {
	private final CraftingContainer craftMatrix = new CraftingContainer(this, 3, 3);
	private final ResultContainer craftResult = new ResultContainer();
	private final ContainerLevelAccess worldPosCallable;
	private final Player player;
	private ItemStack heldStack;

	public ItemCardMenu(int id, Inventory playerInventory) {
		this(id, playerInventory, ContainerLevelAccess.NULL);
	}

	public ItemCardMenu(int id, Inventory playerInventory, ContainerLevelAccess worldPosCallable) {
		super(ForceContainers.ITEM_CARD.get(), id);
		this.worldPosCallable = worldPosCallable;
		this.player = playerInventory.player;
		this.addSlot(new ResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35) {
			@Override
			public boolean mayPickup(Player playerIn) {
				return false;
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18){
					@Override
					public int getMaxStackSize() {
						return 1;
					}

					@Nonnull
					@Override
					public void onTake(final Player player, @Nonnull final ItemStack stack) {

					}

					@Nonnull
					@Override
					public ItemStack remove(final int par1) {
						return ItemStack.EMPTY;
					}

					@Override
					public boolean mayPlace(final ItemStack par1ItemStack) {
						return false;
					}

					@Override
					public boolean mayPickup(final Player par1PlayerEntity) {
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
			CompoundTag tag = heldStack.getOrCreateTag();
			if(tag.contains("RecipeContents")) {
				CompoundTag recipeContents = tag.getCompound("RecipeContents");
				if (!player.level.isClientSide) {
					for(int i = 0; i < craftMatrix.getContainerSize(); i++) {
						craftMatrix.setItem(i, ItemStack.of(recipeContents.getCompound("slot_" + i)));
					}
					craftResult.setItem(0, ItemStack.of(recipeContents.getCompound("result")));
				}
			}
		}
	}

	public static ItemStack getCardStack(Inventory playerInventory) {
		Player player = playerInventory.player;
		if(player.getMainHandItem().getItem() instanceof ItemCardItem) {
			return player.getMainHandItem();
		} else if(player.getOffhandItem().getItem() instanceof ItemCardItem) {
			return player.getOffhandItem();
		}
		return ItemStack.EMPTY;
	}

	protected void updateCraftingResult(Level world, Player player, CraftingContainer inventory, ResultContainer inventoryResult) {
		if (!world.isClientSide) {
			ServerPlayer serverplayerentity = (ServerPlayer) player;
			final Optional<CraftingRecipe> iRecipe = serverplayerentity.server.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventory, world);
			final ItemStack stack;
			if (iRecipe.isPresent() && (iRecipe.get().isSpecial()
					|| !world.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING)
					|| serverplayerentity.getRecipeBook().contains(iRecipe.get())
					|| player.isCreative()))
			{
				stack = iRecipe.get().assemble(this.craftMatrix);
				inventoryResult.setItem(0, stack);
				serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, 0, 0, stack));
			}
			else
			{
				inventoryResult.setItem(0, ItemStack.EMPTY);
				serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, 0, 0, ItemStack.EMPTY));
			}
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void slotsChanged(Container inventoryIn) {
		this.worldPosCallable.execute((world, pos) -> {
			updateCraftingResult(world, this.player, this.craftMatrix, this.craftResult);
		});
	}

	/**
	 * Called when the container is closed.
	 */
	public void removed(Player playerIn) {
		super.removed(playerIn);
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean stillValid(Player playerIn) {
		return true && !heldStack.isEmpty();
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack quickMoveStack(Player playerIn, int index) {
		final int total_crafting_slots = 10;
		if (index <= total_crafting_slots) {
			return ItemStack.EMPTY;
		}

		final int total_slots = 46;

		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			final ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 0) {
				if (!this.moveItemStackTo(itemstack1, total_crafting_slots, total_slots, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index < 32) {
				if (!this.moveItemStackTo(itemstack1, 32, total_slots, false)) {
					return ItemStack.EMPTY;
				}
			} else if ((index < total_slots
					&& !this.moveItemStackTo(itemstack1, total_crafting_slots, 32, false))
					|| !this.moveItemStackTo(itemstack1, total_crafting_slots, total_slots, false))
			{
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
		}
		return itemstack;
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
		if (slotId >= 1 && slotId < 10) {
			// 1 is shift-click
			if (clickTypeIn == ClickType.PICKUP
					|| clickTypeIn == ClickType.PICKUP_ALL
					|| clickTypeIn == ClickType.SWAP)
			{
				final Slot slot = this.slots.get(slotId);
				final ItemStack dropping = this.getCarried();
				handleSlotClick(slot, dropping);
				return;
			}

			return;
		}

		if (clickTypeIn == ClickType.QUICK_MOVE) {
			return;
		}

		super.clicked(slotId, dragType, clickTypeIn, player);
	}

	public ItemStack handleSlotClick(final Slot slotId, final ItemStack stack) {
		if (stack.getCount() > 0) {
			final ItemStack copy = stack.copy();
			copy.setCount(1);
			slotId.set(copy);
		} else if (slotId.getItem().getCount() > 0) {
			slotId.set(ItemStack.EMPTY);
		}

		return slotId.getItem().copy();
	}

	/**
	 * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
	 * null for the initial slot that was double-clicked.
	 */
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
		return slotIn.container != this.craftResult && super.canTakeItemForPickAll(stack, slotIn);
	}

	public CraftingContainer getCraftMatrix() {
		return craftMatrix;
	}

	public ResultContainer getCraftResult() {
		return craftResult;
	}

	public Player getPlayer() {
		return player;
	}

	public void setMatrixContents(Player player, List<ItemStack> stacks) {
		for(int i = 0; i < stacks.size(); i++) {
			handleSlotClick(getSlot(i), stacks.get(i));
		}
	}

	public List<ItemStack> getMatrixContents() {
		return null;
	}
}