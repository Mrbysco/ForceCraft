package com.mrbysco.forcecraft.blocks.infuser;

import com.mrbysco.forcecraft.container.slot.SlotForceGems;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.util.AdvancementUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InfuserContainer extends Container {

    private InfuserTileEntity tile;
    private PlayerEntity player;

    public final int[] validRecipe = new int[1];

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
        this.player = playerInventoryIn.player;

        //Modifier Slots [0, 7] around the outside starting at the top middle going clockwise
        this.addSlot(new UnlockableSlot(te.handler, 0, 80, 20));
        this.addSlot(new UnlockableSlot(te.handler, 1, 104, 32));
        this.addSlot(new UnlockableSlot(te.handler, 2, 116, 57));
        this.addSlot(new UnlockableSlot(te.handler, 3, 104, 81));
        this.addSlot(new UnlockableSlot(te.handler, 4, 80, 93));
        this.addSlot(new UnlockableSlot(te.handler, 5, 56, 81));
        this.addSlot(new UnlockableSlot(te.handler, 6, 44, 57));
        this.addSlot(new UnlockableSlot(te.handler, 7, 56, 32));

        //Tools Slot in the middle
        this.addSlot(new MatrixUpdatingSlot(te.handler, InfuserTileEntity.SLOT_TOOL, 80, 57));

        //Force Gem Slot top left
        this.addSlot(new SlotForceGems(te.handler, InfuserTileEntity.SLOT_GEM, 8, 23));

        //Book Upgrade Slot top left
        this.addSlot(new MatrixUpdatingSlot(te.handler, InfuserTileEntity.SLOT_BOOK, 8, 5));

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

        trackPower();
        trackFluid();

        this.validRecipe[0] = tile.hasValidRecipe() ? 1 : 0;
        this.trackInt(IntReferenceHolder.create(this.validRecipe, 0));

        //set data that will sync server->client WITHOUT needing to call .markDirty()
		trackInt(new IntReferenceHolder() {
			@Override
			public int get() {
				return tile.processTime;
			}

			@Override
			public void set(int value) {
				tile.processTime = value;
			}
		});
		trackInt(new IntReferenceHolder() {
			@Override
			public int get() {
				return tile.maxProcessTime;
			}

			@Override
			public void set(int value) {
				tile.maxProcessTime = value;
			}
		});
        AdvancementUtil.unlockTierAdvancements(player, tile.getBookTier());
    }

    private void trackFluid() {
        // Dedicated server ints are actually truncated to short so we need to split our integer here (split our 32 bit integer into two 16 bit integers)
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.getFluidAmount() & 0xffff;
            }

            @Override
            public void set(int value) {
                int fluidStored = tile.getFluidAmount() & 0xffff0000;
                tile.setFluidAmount(fluidStored + (value & 0xffff));
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (tile.getFluidAmount() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int fluidStored = tile.getFluidAmount() & 0x0000ffff;
                tile.setFluidAmount(fluidStored | (value << 16));
            }
        });
    }

    private void trackPower() {
        // Dedicated server ints are actually truncated to short so we need to split our integer here (split our 32 bit integer into two 16 bit integers)
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.getEnergyStored() & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.getEnergyStored() & 0xffff0000;
                tile.setEnergyStored(energyStored + (value & 0xffff));
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (tile.getEnergyStored() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.getEnergyStored() & 0x0000ffff;
                tile.setEnergyStored(energyStored | (value << 16));
            }
        });
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tile.isUsableByPlayer(playerIn) && !playerIn.isSpectator();
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
        this.onCraftMatrixChanged(null);

        return itemstack;
    }

    public boolean isWorkAllowed() {
        return tile.isWorkAllowed();
    }

    public InfuserTileEntity getTile() {
        return tile;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        if(inventoryIn != null) {
            super.onCraftMatrixChanged(inventoryIn);
        }

        if(!player.world.isRemote) {
            this.validRecipe[0] = tile.updateValidRecipe() ? 1 : 0;
        }
        AdvancementUtil.unlockTierAdvancements(player, tile.getBookTier());
    }

    public class MatrixUpdatingSlot extends SlotItemHandler {
        public MatrixUpdatingSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();
            onCraftMatrixChanged(null);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }

    public class UnlockableSlot extends MatrixUpdatingSlot {
        public UnlockableSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isEnabled() {
            return slotNumber <= tile.getBookTier();
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return slotNumber <= tile.getBookTier() && super.isItemValid(stack);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}