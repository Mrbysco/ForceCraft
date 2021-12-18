package com.mrbysco.forcecraft.container.furnace.slot;

import com.mrbysco.forcecraft.tiles.AbstractForceFurnaceTile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ForceFurnaceResultSlot extends SlotItemHandler {
	private final Player player;
	private int removeCount;

	public ForceFurnaceResultSlot(Player player, IItemHandler inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.player = player;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
	 */
	public ItemStack remove(int amount) {
		if (this.hasItem()) {
			this.removeCount += Math.min(amount, this.getItem().getCount());
		}

		return super.remove(amount);
	}

	public ItemStack onTake(Player thePlayer, ItemStack stack) {
		this.checkTakeAchievements(stack);
		super.onTake(thePlayer, stack);
		return stack;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
	 * internal count then calls onCrafting(item).
	 */
	protected void onQuickCraft(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.checkTakeAchievements(stack);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
	 */
	protected void checkTakeAchievements(ItemStack stack) {
		stack.onCraftedBy(this.player.level, this.player, this.removeCount);
		if (!this.player.level.isClientSide && this.container instanceof AbstractForceFurnaceTile) {
			((AbstractForceFurnaceTile)this.container).unlockRecipes(this.player);
		}

		this.removeCount = 0;
		net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
	}
}