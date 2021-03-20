package mrbysco.forcecraft.container;

import mrbysco.forcecraft.capablilities.pack.PackItemStackHandler;
import mrbysco.forcecraft.items.ForceBeltItem;
import mrbysco.forcecraft.items.ForcePackItem;
import mrbysco.forcecraft.registry.ForceContainers;
import mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ForcePackContainer extends Container {

    private ItemStack heldStack;
    private int upgrades;

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return !playerIn.isSpectator();
    }

    public ForcePackContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, playerInventory.getCurrentItem());
    }

    public ForcePackContainer(int id, PlayerInventory playerInventory, ItemStack forcePack) {
        super(ForceContainers.FORCE_PACK.get(), id);
        this.heldStack = forcePack;
        upgrades = 0;

        IItemHandler itemHandler = forcePack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if(itemHandler instanceof PackItemStackHandler) {
            upgrades = ((PackItemStackHandler)itemHandler).getUpgrades();
            int numRows = upgrades + 1;

            int xPosC = 17;
            int yPosC = 20;
            //Maxes at 40
            for (int j = 0; j < numRows; ++j) {
                for (int k = 0; k < 8; ++k) {
                    this.addSlot(new SlotItemHandler(itemHandler, k + j * 8, xPosC + k * 18, yPosC + j * 18) {
                        @Override
                        public boolean isItemValid(@Nonnull ItemStack stack) {
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
            playerInventory.player.closeScreen();
        }
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        IItemHandler itemHandler = heldStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if(itemHandler instanceof PackItemStackHandler) {
            CompoundNBT tag = heldStack.getOrCreateTag();
            tag.putInt(ForcePackItem.SLOTS_USED, ItemHandlerUtils.getUsedSlots(itemHandler));
            tag.putInt(ForcePackItem.SLOTS_TOTAL, itemHandler.getSlots());
            heldStack.setTag(tag);
        }

        super.onContainerClosed(playerIn);
    }

    public int getUpgrades() {
        return this.upgrades;
    }

    //Credit to Shadowfacts for this method
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(itemstack.getItem() instanceof ForcePackItem)
                return ItemStack.EMPTY;

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
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

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

}
