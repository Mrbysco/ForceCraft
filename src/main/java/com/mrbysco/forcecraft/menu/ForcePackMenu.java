package com.mrbysco.forcecraft.menu;

import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.components.card.RecipeContentsData;
import com.mrbysco.forcecraft.components.storage.PackStackHandler;
import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.items.ItemCardItem;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.util.FindingUtil;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ForcePackMenu extends AbstractContainerMenu {

	private ItemStack heldStack;
	private int upgrades;
	private PackStackHandler inventory;

	@Override
	public boolean stillValid(Player playerIn) {
		return !playerIn.isSpectator();
	}

	public static ForcePackMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
		PackStackHandler handler = new PackStackHandler();
		handler.setUpgrades(data.readInt());
		return new ForcePackMenu(windowId, playerInventory, handler);
	}

	public ForcePackMenu(int id, Inventory playerInventory, PackStackHandler inv) {
		super(ForceMenus.FORCE_PACK.get(), id);
		this.heldStack = FindingUtil.findInstanceStack(playerInventory.player, (stack) -> stack.getItem() instanceof ForcePackItem);
		if (heldStack == null || heldStack.isEmpty()) {
			playerInventory.player.closeContainer();
			return;
		}
		inventory = inv;

		upgrades = 0;

		upgrades = inventory.getUpgrades();
		int numRows = upgrades + 1;

		int xPosC = 17;
		int yPosC = 20;
		//Maxes at 40
		for (int j = 0; j < numRows; ++j) {
			for (int k = 0; k < 8; ++k) {
				this.addSlot(new SlotItemHandler(inventory, k + j * 8, xPosC + k * 18, yPosC + j * 18) {
					@Override
					public boolean mayPlace(@NotNull ItemStack stack) {
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
	}

	/**
	 * Handles inventory management and crafting with ItemCards when the menu is closed by the player.
	 *
	 * @param player the player that closed the menu
	 */
	@Override
	public void removed(Player player) {
		if (inventory != null) {
			if (player.level().isClientSide) return;
			for (int i = 0; i < inventory.getSlots(); i++) {
				ItemStack stack = inventory.getStackInSlot(i);
				if (stack.getItem() instanceof ItemCardItem && stack.has(ForceComponents.RECIPE_CONTENTS)) {
					RecipeContentsData recipeData = stack.get(ForceComponents.RECIPE_CONTENTS);
					NonNullList<ItemStack> ingredientList = NonNullList.create();
					List<ItemStack> mergeList = new ArrayList<>();
					for (int j = 0; j < 9; j++) {
						ItemStack recipeStack = recipeData.recipeItems().get(j);
						if (!recipeStack.isEmpty()) {
							if (ingredientList.stream().noneMatch(s -> ItemStack.isSameItemSameComponents(s, recipeStack))) {
								ingredientList.add(recipeStack.copy());
							} else {
								mergeList.add(recipeStack.copy());
							}
						}
					}

					for (ItemStack recipeStack : mergeList) {
						if (!ingredientList.isEmpty()) {
							List<ItemStack> buffer = new ArrayList<>();
							if (recipeStack.getMaxStackSize() == 1) {
								//We can't merge these, so just add them to the buffer
								ingredientList.add(recipeStack.copy());
								recipeStack.shrink(1);
								continue;
							} else {
								for (ItemStack ingredient : ingredientList) {
									if (ingredient != null && !ingredient.isEmpty() && ingredient.getMaxStackSize() > 1) {
										if (ItemStack.isSameItemSameComponents(ingredient, recipeStack)) {
											int addedCount = ingredient.getCount() + recipeStack.getCount();
											int maxCount = ingredient.getMaxStackSize();
											if (addedCount <= maxCount) {
												recipeStack.shrink(1);
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
							}
							buffer.removeIf(ItemStack::isEmpty);
							if (!buffer.isEmpty()) {
								ingredientList.addAll(buffer);
							}
						}
					}
					mergeList.clear();

					List<ItemStack> restList = new ArrayList<>();
					for (int k = 0; k < inventory.getSlots(); k++) {
						if (k != i) {
							ItemStack restStack = inventory.getStackInSlot(k);
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
							if (ItemStack.isSameItemSameComponents(ingredient, rest)) {
								countPossible += (int) Math.floor((double) rest.getCount() / ingredient.getCount());
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

					ItemStack craftStack = recipeData.resultItem();
					if (canCraft && craftCount > 0) {
						for (int l = 0; l < craftCount; l++) {
							for (ItemStack ingredient : ingredientList) {
								for (ItemStack rest : restList) {
									if (ItemStack.isSameItemSameComponents(ingredient, rest)) {
										if (rest.getCount() >= ingredient.getCount()) {
											if (rest.hasCraftingRemainingItem()) {
												ItemStack remainderStack = rest.getCraftingRemainingItem().copy();
												rest.shrink(1);
												ItemStack insertResult = ItemHandlerHelper.insertItem(inventory,
														remainderStack, false);
												if (!insertResult.isEmpty()) {
													player.drop(insertResult, true);
												}
											} else {
												rest.shrink(ingredient.getCount());
											}
										}
									}
								}
							}

							ItemStack stackCopy = craftStack.copy();
							ItemStack craftedStack = ItemHandlerHelper.insertItem(inventory, stackCopy, false);
							if (!craftedStack.isEmpty()) {
								player.drop(craftedStack, true);
							}
						}
					}
				}
			}

			heldStack.set(ForceComponents.SLOTS_USED, ItemHandlerUtils.getUsedSlots(inventory));
		}

		super.removed(player);
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
		if (slotId >= 0) {
			if (getSlot(slotId).getItem().getItem() instanceof ForcePackItem)
				return;
		}
		if (clickTypeIn == ClickType.SWAP)
			return;

		super.clicked(slotId, dragType, clickTypeIn, player);
	}

	public int getUpgrades() {
		return this.upgrades;
	}

	//Credit to Shadowfacts for this method
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (itemstack.getItem() instanceof ForcePackItem)
				return ItemStack.EMPTY;

			int containerSlots = slots.size() - player.getInventory().items.size();

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
