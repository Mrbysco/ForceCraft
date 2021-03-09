package mrbysco.forcecraft.items;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.ForceBeltContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ForceBeltItem extends BaseItem {

    public ForceBeltItem(Item.Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote && stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
            playerIn.openContainer(this.getContainer(stack));
            return new ActionResult<ItemStack>(ActionResultType.PASS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItem();
        if(player != null && !context.getWorld().isRemote && stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
            player.openContainer(getContainer(stack));
            return ActionResultType.PASS;
        }
        //If it doesn't nothing bad happens
        return super.onItemUse(context);
    }

    @Nullable
    public INamedContainerProvider getContainer(ItemStack stack) {
        return new SimpleNamedContainerProvider((id, inventory, player) -> {
            return new ForceBeltContainer(id, inventory, stack);
        }, new TranslationTextComponent(Reference.MOD_ID + ".container.belt"));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ForceBeltItem.InventoryProvider();
    }

    private static class InventoryProvider implements ICapabilitySerializable<CompoundNBT> {
        private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(8) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                //Make sure there's no ForceBelt-ception
                return !(stack.getItem() instanceof ForceBeltItem) && super.isItemValid(slot, stack);
            }
        });

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                return inventory.cast();
            else return LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            if (inventory.isPresent()) {
                return inventory.resolve().get().serializeNBT();
            }
            return new CompoundNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            inventory.ifPresent(h -> h.deserializeNBT(nbt));
        }
    }
}
