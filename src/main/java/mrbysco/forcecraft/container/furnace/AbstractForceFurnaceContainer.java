package mrbysco.forcecraft.container.furnace;

import mrbysco.forcecraft.container.furnace.slot.ForceFurnaceFuelSlot;
import mrbysco.forcecraft.container.furnace.slot.ForceFurnaceResultSlot;
import mrbysco.forcecraft.container.furnace.slot.UpgradeSlot;
import mrbysco.forcecraft.items.UpgradeCoreItem;
import mrbysco.forcecraft.recipe.ForceRecipes;
import mrbysco.forcecraft.registry.ForceRegistry;
import mrbysco.forcecraft.tiles.AbstractForceFurnaceTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.ServerRecipePlacerFurnace;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractForceFurnaceContainer extends RecipeBookContainer<IInventory> {
	private final IInventory furnaceInventory;
	private final IIntArray furnaceData;
	protected final World world;
	private final RecipeBookCategory field_242384_g;

	protected AbstractForceFurnaceContainer(ContainerType<?> containerType, RecipeBookCategory recipeBookCategory, int containerID, PlayerInventory playerInventory) {
		this(containerType, recipeBookCategory, containerID, playerInventory, new Inventory(4), new IntArray(4));
	}

	public int getBurn() {
		return furnaceData.get(0);
	}

	protected AbstractForceFurnaceContainer(ContainerType<?> containerType, RecipeBookCategory recipeBookCategory, int containerID, PlayerInventory playerInventory, IInventory inventory, IIntArray furnaceData) {
		super(containerType, containerID);
		this.field_242384_g = recipeBookCategory;
		assertInventorySize(inventory, 3);
		assertIntArraySize(furnaceData, 4);
		this.furnaceInventory = inventory;
		this.furnaceData = furnaceData;
		this.world = playerInventory.player.world;
		this.addSlot(new Slot(inventory, 0, 56, 17));
		this.addSlot(new ForceFurnaceFuelSlot(this, inventory, 1, 56, 53));
		this.addSlot(new ForceFurnaceResultSlot(playerInventory.player, inventory, 2, 116, 35));
		this.addSlot(new UpgradeSlot(inventory, 3, 12, 12));

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}

		this.trackIntArray(furnaceData);
	}

	public void fillStackedContents(RecipeItemHelper itemHelperIn) {
		if (this.furnaceInventory instanceof IRecipeHelperPopulator) {
			((IRecipeHelperPopulator)this.furnaceInventory).fillStackedContents(itemHelperIn);
		}

	}

	public void clear() {
		this.furnaceInventory.clear();
	}

	public void func_217056_a(boolean p_217056_1_, IRecipe<?> recipeIn, ServerPlayerEntity player) {
		(new ServerRecipePlacerFurnace<>(this)).place(player, (IRecipe<IInventory>) recipeIn, p_217056_1_);
	}

	public boolean matches(IRecipe<? super IInventory> recipeIn) {
		return recipeIn.matches(this.furnaceInventory, this.world);
	}

	public int getOutputSlot() {
		return 2;
	}

	public int getWidth() {
		return 1;
	}

	public int getHeight() {
		return 1;
	}

	@OnlyIn(Dist.CLIENT)
	public int getSize() {
		return 3;
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.furnaceInventory.isUsableByPlayer(playerIn);
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
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
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
				} else if (this.isUpgrade(itemstack1) && index == 3) {
					itemstack1.shrink(1);
					playerIn.world.playSound((PlayerEntity) null, playerIn.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
					return ItemStack.EMPTY;
				} else if (index >= 4 && index < 30) {
					if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
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
		ItemStack upgrade = furnaceInventory.getStackInSlot(3);;
		if(!upgrade.isEmpty()) {
			if(upgrade.getItem() == ForceRegistry.FREEZING_CORE.get()) {
				return ForceRecipes.FREEZING; //TODO FREEZING RECIPES
			} else if(upgrade.getItem() == ForceRegistry.GRINDING_CORE.get()) {
				return ForceRecipes.GRINDING; //TODO GRINDING RECIPES
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

	@OnlyIn(Dist.CLIENT)
	public RecipeBookCategory func_241850_m() {
		return this.field_242384_g;
	}
}