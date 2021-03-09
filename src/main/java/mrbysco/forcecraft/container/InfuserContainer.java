package mrbysco.forcecraft.container;

import mrbysco.forcecraft.container.slot.SlotForceGems;
import mrbysco.forcecraft.registry.ForceContainers;
import mrbysco.forcecraft.tiles.InfuserTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InfuserContainer extends Container {

    InfuserTileEntity te;

    public InfuserContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    private static InfuserTileEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
        Objects.requireNonNull(data, "data cannot be null!");
        final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());

        if (tileAtPos instanceof InfuserTileEntity) {
            return (InfuserTileEntity) tileAtPos;
        }

        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    public InfuserContainer(int id, PlayerInventory playerInventoryIn, InfuserTileEntity te) {
        super(ForceContainers.INFUSER.get(), id);

        //Modifier Slots
        this.addSlot(new SlotItemHandler(te.handler, 0, 80, 20));
        this.addSlot(new SlotItemHandler(te.handler, 1, 104, 32));
        this.addSlot(new SlotItemHandler(te.handler, 2, 116, 57));
        this.addSlot(new SlotItemHandler(te.handler, 3, 104, 81));
        this.addSlot(new SlotItemHandler(te.handler, 4, 80, 93));
        this.addSlot(new SlotItemHandler(te.handler, 5, 56, 81));
        this.addSlot(new SlotItemHandler(te.handler, 6, 44, 57));
        this.addSlot(new SlotItemHandler(te.handler, 7, 56, 32));

        //Tools Slot
        this.addSlot(new SlotItemHandler(te.handler, 8, 80, 57));

        //Force Gem Slot
        this.addSlot(new SlotForceGems(te.handler, 9, 10, 11));

        int xPos = 8;
        int yPos = 127;

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventoryIn, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventoryIn, x, xPos + x * 18, yPos + 58));
        }

        this.te = te;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return !playerIn.isSpectator();
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        boolean hasReturned = false;

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 11) {
                if (!this.mergeItemStack(itemstack1, 11, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                for (int i = 0; i < 11; i++) {
                    if (!hasReturned) {
                        if (((Slot) this.inventorySlots.get(i)).getHasStack() || !((Slot) this.inventorySlots.get(i)).isItemValid(itemstack1)) {
                            hasReturned = true;
                            return ItemStack.EMPTY;
                        }
                    }
                    if (!hasReturned) {
                        if (itemstack1.hasTag() && itemstack1.getCount() == 1) {
                            ((Slot) this.inventorySlots.get(i)).putStack(itemstack1.copy());
                            itemstack1.setCount(0);
                            hasReturned = true;
                        } else if (!itemstack1.isEmpty()) {
                            ((Slot) this.inventorySlots.get(i)).putStack(new ItemStack(itemstack1.getItem(), 1));
                            itemstack1.shrink(1);
                            hasReturned = true;
                        }
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public void setButtonPressed(boolean buttonPressed){
        te.canWork = buttonPressed;
    }

    public void setFluidAmount(int amount){
        te.fluidContained = amount;
    }

    public int getFluidAmount(){
        return te.fluidContained;
    }

    public InfuserTileEntity getTile() {
        return te;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }
}