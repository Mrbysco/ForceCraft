package com.mrbysco.forcecraft.container;

import com.mrbysco.forcecraft.capablilities.pack.PackItemStackHandler;
import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.items.ItemCardItem;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.util.FindingUtil;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForcePackContainer extends Container {

	private ItemStack heldStack;
	private int upgrades;

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return !playerIn.isSpectator();
	}

	public ForcePackContainer(int id, PlayerInventory playerInventory) {
		super(ForceContainers.FORCE_PACK.get(), id);
		this.heldStack = FindingUtil.findInstanceStack(playerInventory.player, (stack) -> stack.getItem() instanceof ForcePackItem);
		if (heldStack == null || heldStack.isEmpty()) {
			playerInventory.player.closeContainer();
			return;
		}

		upgrades = 0;

		IItemHandler itemHandler = heldStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (itemHandler instanceof PackItemStackHandler) {
			upgrades = ((PackItemStackHandler) itemHandler).getUpgrades();
			int numRows = upgrades + 1;
//            ForceCraft.LOGGER.info("Pack upgrades {}",  upgrades);


			int xPosC = 17;
			int yPosC = 20;
			//Maxes at 40
			for (int j = 0; j < numRows; ++j) {
				for (int k = 0; k < 8; ++k) {
					this.addSlot(new SlotItemHandler(itemHandler, k + j * 8, xPosC + k * 18, yPosC + j * 18) {
						@Override
						public boolean mayPlace(@Nonnull ItemStack stack) {
							return !(stack.getItem() instanceof ForcePackItem || stack.getItem() instanceof ForceBeltItem);
						}
					});
				}
			}

			//Player Inventory
			int xPos = 8;
			int yPos = 36 + (numRows * 18);

			//Slots 9-99
			for (int y = 0; y < 3; ++y) {
				for (int x = 0; x < 9; ++x) {
					this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
				}
			}

			//Slots 0-8
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
			}
		} else {
			playerInventory.player.closeContainer();
		}
	}

	@Override
	public void removed(PlayerEntity playerIn) {
		IItemHandler itemHandler = heldStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (itemHandler instanceof PackItemStackHandler) {
			for (int i = 0; i < itemHandler.getSlots(); i++) {
				ItemStack stack = itemHandler.getStackInSlot(i);
				CompoundNBT tag = stack.getTag();
				if (stack.getItem() instanceof ItemCardItem && tag != null && tag.contains("RecipeContents")) {
					CompoundNBT recipeContents = tag.getCompound("RecipeContents");
					NonNullList<ItemStack> ingredientList = NonNullList.create();
					List<ItemStack> mergeList = new ArrayList<>();
					for (int j = 0; j < 9; j++) {
						ItemStack recipeStack = ItemStack.of(recipeContents.getCompound("slot_" + j));
						if (!recipeStack.isEmpty()) {
							if (ingredientList.isEmpty()) {
								ingredientList.add(recipeStack);
							} else {
								mergeList.add(recipeStack);
							}
						}
					}

					for (ItemStack recipeStack : mergeList) {
						if (!ingredientList.isEmpty()) {
							List<ItemStack> buffer = new ArrayList<>();
							for (Iterator<ItemStack> iterator = ingredientList.iterator(); iterator.hasNext(); ) {
								ItemStack ingredient = iterator.next();
								if (ingredient != null && !ingredient.isEmpty()) {
									if (consideredTheSameItem(ingredient, recipeStack)) {
										int addedCount = ingredient.getCount() + recipeStack.getCount();
										int maxCount = ingredient.getMaxStackSize();
										if (addedCount <= maxCount) {
											recipeStack.setCount(0);
											ingredient.setCount(addedCount);
										} else if (recipeStack.getCount() < maxCount) {
											recipeStack.shrink(maxCount - ingredient.getCount());
											ingredient.setCount(maxCount);
										}
									}
								}

								if (!recipeStack.isEmpty()) {
									buffer.add(recipeStack);
								}
							}
							if (!buffer.isEmpty()) {
								ingredientList.addAll(buffer);
							}
						}
					}
					mergeList.clear();

					List<ItemStack> restList = new ArrayList<>();
					for (int k = 0; k < itemHandler.getSlots(); k++) {
						if (k != i) {
							ItemStack restStack = itemHandler.getStackInSlot(k);
							if (!restStack.isEmpty()) {
								restList.add(restStack);
							}
						}
					}
					boolean canCraft = true;
					int craftCount = 64;
					for (ItemStack ingredient : ingredientList) {
						int countPossible = 0;
						for (ItemStack rest : restList) {
							if (ingredient.getItem() == rest.getItem() && ItemStack.tagMatches(ingredient, rest)) {
								countPossible += (double) rest.getCount() / ingredient.getCount();
							}
						}
						if (countPossible == 0) {
							canCraft = false;
							craftCount = 0;
							break;
						}
						if (countPossible < craftCount) {
							craftCount = countPossible;
						}
					}

					ItemStack craftStack = ItemStack.of(recipeContents.getCompound("result"));
					if (canCraft && craftCount > 0) {
						for (int l = 0; l < craftCount; l++) {
							for (ItemStack ingredient : ingredientList) {
								for (ItemStack rest : restList) {
									if (ingredient.getItem() == rest.getItem() && ItemStack.tagMatches(ingredient, rest)) {
										if (rest.getCount() >= ingredient.getCount()) {
											rest.shrink(ingredient.getCount());
										}
									}
								}
							}

							ItemStack stackCopy = craftStack.copy();
							ItemStack craftedStack = ItemHandlerHelper.insertItem(itemHandler, stackCopy, false);
							if (!craftedStack.isEmpty()) {
								playerIn.drop(craftedStack, true);
							}
						}
					}
				}
			}

			CompoundNBT tag = heldStack.getOrCreateTag();
			tag.putInt(ForcePackItem.SLOTS_USED, ItemHandlerUtils.getUsedSlots(itemHandler));
			heldStack.setTag(tag);
		}

		super.removed(playerIn);
	}

	@Override
	public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		if (slotId >= 0) {
			if (getSlot(slotId).getItem().getItem() instanceof ForcePackItem)
				return ItemStack.EMPTY;
		}
		if (clickTypeIn == ClickType.SWAP)
			return ItemStack.EMPTY;

		return super.clicked(slotId, dragType, clickTypeIn, player);
	}

	public int getUpgrades() {
		return this.upgrades;
	}

	//Credit to Shadowfacts for this method
	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (itemstack.getItem() instanceof ForcePackItem)
				return ItemStack.EMPTY;

			int containerSlots = slots.size() - player.inventory.items.size();

			if (index < containerSlots) {
				if (!this.moveItemStackTo(itemstack1, containerSlots, slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false)) {
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

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

}
