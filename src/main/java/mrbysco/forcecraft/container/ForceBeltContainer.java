package mrbysco.forcecraft.container;

import mrbysco.forcecraft.container.slot.BeltSlot;
import mrbysco.forcecraft.items.ForceBeltItem;
import mrbysco.forcecraft.items.ForcePackItem;
import mrbysco.forcecraft.registry.ForceContainers;
import mrbysco.forcecraft.registry.ForceTags;
import mrbysco.forcecraft.util.FindingUtil;
import mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ForceBeltContainer extends Container {

    private ItemStack heldStack;

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return !playerIn.isSpectator() && !heldStack.isEmpty();
    }

    public ForceBeltContainer(int id, PlayerInventory playerInventory) {
        super(ForceContainers.FORCE_BELT.get(), id);
        this.heldStack = FindingUtil.findInstanceStack(playerInventory.player, (stack) -> stack.getItem() instanceof ForceBeltItem);
        if (heldStack == null || heldStack.isEmpty()) {
            playerInventory.player.closeScreen();
            return;
        }

        int xPosC = 17;
        int yPosC = 20;
        //Maxes at 40

        IItemHandler itemHandler = heldStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if(itemHandler != null) {
            for (int k = 0; k < 8; ++k) {
                this.addSlot(new BeltSlot(itemHandler, k, xPosC + k * 18, yPosC));
            }

            //Player Inventory
            int xPos = 8;
            int yPos = 54;

            for(int y = 0; y < 3; ++y) {
                for(int x = 0; x < 9; ++x) {
                    this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
                }
            }

            for(int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
            }
        } else {
            playerInventory.player.closeScreen();
        }
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        IItemHandler itemHandler = heldStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if(itemHandler != null) {
            CompoundNBT tag = heldStack.getOrCreateTag();
            tag.putInt(ForcePackItem.SLOTS_USED, ItemHandlerUtils.getUsedSlots(itemHandler));
            tag.putInt(ForcePackItem.SLOTS_TOTAL, itemHandler.getSlots());
            heldStack.setTag(tag);
        }

        super.onContainerClosed(playerIn);
    }

    //Credit to Shadowfacts for this method
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(itemstack.getItem() instanceof ForceBeltItem)
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
