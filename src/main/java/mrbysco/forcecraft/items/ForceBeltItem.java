package mrbysco.forcecraft.items;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.ForceBeltContainer;
import mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ForceBeltItem extends BaseItem {

    public ForceBeltItem(Item.Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            if(worldIn.isRemote) {
                mrbysco.forcecraft.client.gui.pack.RenameAndRecolorScreen.openScreen(stack, handIn);
            }
        } else {
            if (!worldIn.isRemote) {
                NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(stack), playerIn.getPosition());
            }
        }
        //If it doesn't nothing bad happens
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
        }, stack.hasDisplayName() ? ((TextComponent)stack.getDisplayName()).mergeStyle(TextFormatting.BLACK) : new TranslationTextComponent(Reference.MOD_ID + ".container.belt"));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getOrCreateTag();
        if(tag.contains(ForcePackItem.SLOTS_USED) &&  tag.contains(ForcePackItem.SLOTS_TOTAL)) {
            tooltip.add(new StringTextComponent(String.format("%s/%s Slots", tag.getInt(ForcePackItem.SLOTS_USED), tag.getInt(ForcePackItem.SLOTS_TOTAL))));
        } else {
            tooltip.add(new StringTextComponent("0/8 Slots"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return ((TextComponent)super.getDisplayName(stack)).mergeStyle(TextFormatting.YELLOW);
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
                return !(stack.getItem() instanceof ForceBeltItem) && stack.getItem().isIn(ForceTags.VALID_FORCE_BELT) && super.isItemValid(slot, stack);
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
