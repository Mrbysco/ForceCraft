package mrbysco.forcecraft.blocks.infuser;

import mrbysco.forcecraft.container.slot.SlotForceGems;
import mrbysco.forcecraft.container.slot.SlotForceTools;
import mrbysco.forcecraft.registry.ForceContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InfuserContainer extends Container {

    InfuserTileEntity tile;

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
        this.tile = te;

        //Modifier Slots [0, 7] around the outside starting at the top middle going clockwise
        this.addSlot(new SlotItemHandler(te.handler, 0, 80, 20));
        this.addSlot(new SlotItemHandler(te.handler, 1, 104, 32));
        this.addSlot(new SlotItemHandler(te.handler, 2, 116, 57));
        this.addSlot(new SlotItemHandler(te.handler, 3, 104, 81));
        this.addSlot(new SlotItemHandler(te.handler, 4, 80, 93));
        this.addSlot(new SlotItemHandler(te.handler, 5, 56, 81));
        this.addSlot(new SlotItemHandler(te.handler, 6, 44, 57));
        this.addSlot(new SlotItemHandler(te.handler, 7, 56, 32));

        //Tools Slot in the middle
        this.addSlot(new SlotForceTools(te.handler, InfuserTileEntity.SLOT_TOOL, 80, 57));

        //Force Gem Slot top left
        this.addSlot(new SlotForceGems(te.handler, InfuserTileEntity.SLOT_GEM, 8, 23));

        //Book Upgrade Slot top left
        this.addSlot(new SlotItemHandler(te.handler, InfuserTileEntity.SLOT_BOOK, 8, 5));
        
        //player inventory here
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

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            final int tileSize = 11;
            
            if (index < tileSize) {
                if (!this.mergeItemStack(itemstack1, tileSize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
            	// change to fix all the manual moving. and fix books going to book slots, etc
                if (!this.mergeItemStack(itemstack1, 0, tileSize, true)) {
                    return ItemStack.EMPTY;
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

    public void setFluidAmount(int amount) {
        tile.fluidContained = amount;
    }

    public int getFluidAmount(){
        return tile.fluidContained;
    }

    public InfuserTileEntity getTile() {
        return tile;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }
}