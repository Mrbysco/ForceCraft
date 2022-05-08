package com.mrbysco.forcecraft.menu.furnace;

import com.mrbysco.forcecraft.menu.furnace.slot.ForceFurnaceFuelSlot;
import com.mrbysco.forcecraft.menu.furnace.slot.ForceFurnaceResultSlot;
import com.mrbysco.forcecraft.menu.furnace.slot.UpgradeSlot;
import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import com.mrbysco.forcecraft.recipe.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.blockentities.AbstractForceFurnaceBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public abstract class AbstractForceFurnaceMenu extends AbstractContainerMenu {
	private AbstractForceFurnaceBlockEntity tile;
	private IItemHandler furnaceInventory;
	private IItemHandler upgradeInventory;
	private ContainerData furnaceData;
	private Level world;

	public AbstractForceFurnaceMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data));
	}

	protected static AbstractForceFurnaceBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final BlockEntity tileAtPos = playerInventory.player.level.getBlockEntity(data.readBlockPos());

		if (tileAtPos instanceof AbstractForceFurnaceBlockEntity) {
			return (AbstractForceFurnaceBlockEntity) tileAtPos;
		}

		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	public AbstractForceFurnaceMenu(int id, Inventory playerInventoryIn, AbstractForceFurnaceBlockEntity te) {
		super(ForceContainers.FORCE_FURNACE.get(), id);
		this.tile = te;
		Player player = playerInventoryIn.player;
		this.world = player.level;
		this.furnaceInventory = tile.handler;
		this.upgradeInventory = tile.upgradeHandler;
		this.furnaceData = tile.getFurnaceData();

		assertFurnaceSize(furnaceInventory, 3);
		assertFurnaceSize(upgradeInventory, 1);
		checkContainerDataCount(furnaceData, 4);

		this.addSlot(new SlotItemHandler(furnaceInventory, 0, 56, 17));
		this.addSlot(new ForceFurnaceFuelSlot(this, furnaceInventory, 1, 56, 53));
		this.addSlot(new ForceFurnaceResultSlot(player, furnaceInventory, 2, 116, 35));
		this.addSlot(new UpgradeSlot(upgradeInventory, 0, 12, 12));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
		}

		this.addDataSlots(furnaceData);
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
	public boolean stillValid(Player playerIn) {
		return this.tile.stillValid(playerIn);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 2) {
				if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (this.hasRecipe(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (this.isFuel(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (isUpgrade(itemstack1) && index == 3) {
					itemstack1.shrink(1);
					playerIn.level.playSound((Player) null, playerIn.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
					return ItemStack.EMPTY;
				} else if (index >= 4 && index < 31) {
					if (!this.moveItemStackTo(itemstack1, 31, 40, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 31 && index < 40 && !this.moveItemStackTo(itemstack1, 4, 31, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
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

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	protected boolean hasRecipe(ItemStack stack) {
		return this.world.getRecipeManager().getRecipeFor((RecipeType) this.getRecipeType(), new SimpleContainer(stack), this.world).isPresent();
	}

	protected RecipeType<? extends AbstractCookingRecipe> getRecipeType() {
		RecipeType<? extends AbstractCookingRecipe> recipeType = RecipeType.SMELTING;
		ItemStack upgrade = upgradeInventory.getStackInSlot(0);
		if (!upgrade.isEmpty()) {
			if (upgrade.getItem() == ForceRegistry.FREEZING_CORE.get()) {
				return ForceRecipes.FREEZING;
			} else if (upgrade.getItem() == ForceRegistry.GRINDING_CORE.get()) {
				return ForceRecipes.GRINDING;
			}
		}
		return recipeType;
	}

	public boolean isFuel(ItemStack stack) {
		return AbstractForceFurnaceBlockEntity.isFuel(stack);
	}

	public static boolean isUpgrade(ItemStack stack) {
		return stack.getItem() instanceof UpgradeCoreItem;
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
		if (clickTypeIn == ClickType.PICKUP_ALL && this.getCarried().getItem() instanceof UpgradeCoreItem) {
			return;
		}
		if (slotId == 3) {
			Slot slot = getSlot(slotId);
			if (slot.hasItem() && clickTypeIn != ClickType.QUICK_MOVE) {
				player.level.playSound((Player) null, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 0.5F, 1.0F);
				return;
			}
		}
		super.clicked(slotId, dragType, clickTypeIn, player);
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