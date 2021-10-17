package com.mrbysco.forcecraft.container.furnace;

import com.mrbysco.forcecraft.container.furnace.slot.ForceFurnaceFuelSlot;
import com.mrbysco.forcecraft.container.furnace.slot.ForceFurnaceResultSlot;
import com.mrbysco.forcecraft.container.furnace.slot.UpgradeSlot;
import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import com.mrbysco.forcecraft.recipe.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.tiles.AbstractForceFurnaceTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public abstract class AbstractForceFurnaceContainer extends Container {
	private AbstractForceFurnaceTile tile;
	private PlayerEntity player;
	private IItemHandler furnaceInventory;
	private IItemHandler upgradeInventory;
	private IIntArray furnaceData;
	private World world;

	public AbstractForceFurnaceContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data));
	}

	protected static AbstractForceFurnaceTile getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());

		if (tileAtPos instanceof AbstractForceFurnaceTile) {
			return (AbstractForceFurnaceTile) tileAtPos;
		}

		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	public AbstractForceFurnaceContainer(int id, PlayerInventory playerInventoryIn, AbstractForceFurnaceTile te) {
		super(ForceContainers.FORCE_FURNACE.get(), id);
		this.tile = te;
		this.player = playerInventoryIn.player;
		this.world = player.world;
		this.furnaceInventory = tile.handler;
		this.upgradeInventory = tile.upgradeHandler;
		this.furnaceData = tile.getFurnaceData();

		assertFurnaceSize(furnaceInventory, 3);
		assertFurnaceSize(upgradeInventory, 1);
		assertIntArraySize(furnaceData, 4);

		this.addSlot(new SlotItemHandler(furnaceInventory, 0, 56, 17));
		this.addSlot(new ForceFurnaceFuelSlot(this, furnaceInventory, 1, 56, 53));
		this.addSlot(new ForceFurnaceResultSlot(player, furnaceInventory, 2, 116, 35));
		this.addSlot(new UpgradeSlot(upgradeInventory, 0, 12, 12));

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
		}

		this.trackIntArray(furnaceData);
	}

//	protected AbstractForceFurnaceContainer(ContainerType<?> containerType, RecipeBookCategory recipeBookCategory, int containerID, PlayerInventory playerInventory) {
//		this(containerType, recipeBookCategory, containerID, playerInventory, null, new IntArray(4));
//	}

	public int getBurn() {
		return furnaceData.get(0);
	}

	protected void assertFurnaceSize(IItemHandler inventoryIn, int minSize) {
		int i = inventoryIn.getSlots();
		if (i < minSize) {
			throw new IllegalArgumentException("Container size " + i + " is smaller than expected " + minSize);
		}
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.tile.isUsableByPlayer(playerIn);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 2) {
				if (!this.mergeItemStack(itemstack1, 4, 40, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (this.hasRecipe(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (this.isFuel(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (isUpgrade(itemstack1) && index == 3) {
					itemstack1.shrink(1);
					playerIn.world.playSound((PlayerEntity) null, playerIn.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
					return ItemStack.EMPTY;
				} else if (index >= 4 && index < 31) {
					if (!this.mergeItemStack(itemstack1, 31, 40, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 31 && index < 40 && !this.mergeItemStack(itemstack1, 4, 31, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 4, 40, false)) {
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

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	protected boolean hasRecipe(ItemStack stack) {
		return this.world.getRecipeManager().getRecipe((IRecipeType)this.getRecipeType(), new Inventory(stack), this.world).isPresent();
	}

	protected IRecipeType<? extends AbstractCookingRecipe> getRecipeType() {
		IRecipeType<? extends AbstractCookingRecipe> recipeType = IRecipeType.SMELTING;
		ItemStack upgrade = upgradeInventory.getStackInSlot(0);
		if(!upgrade.isEmpty()) {
			if(upgrade.getItem() == ForceRegistry.FREEZING_CORE.get()) {
				return ForceRecipes.FREEZING;
			} else if(upgrade.getItem() == ForceRegistry.GRINDING_CORE.get()) {
				return ForceRecipes.GRINDING;
			}
		}
		return recipeType;
	}

	public boolean isFuel(ItemStack stack) {
		return AbstractForceFurnaceTile.isFuel(stack);
	}

	public static boolean isUpgrade(ItemStack stack) {
		return stack.getItem() instanceof UpgradeCoreItem;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		if(clickTypeIn == ClickType.PICKUP_ALL && player.inventory.getItemStack().getItem() instanceof UpgradeCoreItem) {
			return ItemStack.EMPTY;
		}
		if (slotId == 3) {
			Slot slot = getSlot(slotId);
			if (slot.getHasStack() && clickTypeIn != ClickType.QUICK_MOVE) {
				player.world.playSound((PlayerEntity) null, player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1.0F, 1.0F);
				return ItemStack.EMPTY;
			}
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	@OnlyIn(Dist.CLIENT)
	public int getCookProgressionScaled() {
		int i = this.furnaceData.get(2);
		int j = this.furnaceData.get(3);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	@OnlyIn(Dist.CLIENT)
	public int getBurnLeftScaled() {
		int i = this.furnaceData.get(1);
		if (i == 0) {
			i = 200;
		}

		return this.furnaceData.get(0) * 13 / i;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isBurning() {
		return this.furnaceData.get(0) > 0;
	}
}