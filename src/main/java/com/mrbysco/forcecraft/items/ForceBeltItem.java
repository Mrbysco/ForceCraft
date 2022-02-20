package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.ForceBeltMenu;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ForceBeltItem extends BaseItem {

    public ForceBeltItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if(playerIn.isShiftKeyDown()) {
            if(level.isClientSide) {
                com.mrbysco.forcecraft.client.gui.pack.RenameAndRecolorScreen.openScreen(stack, handIn);
            }
        } else {
            if (!level.isClientSide) {
                NetworkHooks.openGui((ServerPlayer) playerIn, getContainer(stack), playerIn.blockPosition());
            }
        }
        //If it doesn't nothing bad happens
        return super.use(level, playerIn, handIn);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if(player != null && !context.getLevel().isClientSide && stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
            player.openMenu(getContainer(stack));
            return InteractionResult.PASS;
        }
        //If it doesn't nothing bad happens
        return super.useOn(context);
    }

    @Nullable
    public MenuProvider getContainer(ItemStack stack) {
        return new SimpleMenuProvider((id, inventory, player) -> {
            return new ForceBeltMenu(id, inventory);
        }, stack.hasCustomHoverName() ? ((BaseComponent)stack.getHoverName()).withStyle(ChatFormatting.BLACK) : new TranslatableComponent(Reference.MOD_ID + ".container.belt"));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains(ForcePackItem.SLOTS_USED) &&  tag.contains(ForcePackItem.SLOTS_TOTAL)) {
            tooltip.add(new TextComponent(String.format("%s/%s Slots", tag.getInt(ForcePackItem.SLOTS_USED), tag.getInt(ForcePackItem.SLOTS_TOTAL))));
        } else {
            tooltip.add(new TextComponent("0/8 Slots"));
        }
        super.appendHoverText(stack, level, tooltip, flagIn);
    }

    @Override
    public Component getName(ItemStack stack) {
        return ((BaseComponent)super.getName(stack)).withStyle(ChatFormatting.YELLOW);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ForceBeltItem.InventoryProvider();
    }

    private static class InventoryProvider implements ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(8) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                //Make sure there's no ForceBelt-ception
                return !(stack.getItem() instanceof ForceBeltItem) && stack.is(ForceTags.VALID_FORCE_BELT) && super.isItemValid(slot, stack);
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
        public CompoundTag serializeNBT() {
            if (inventory.isPresent()) {
                return inventory.resolve().get().serializeNBT();
            }
            return new CompoundTag();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            inventory.ifPresent(h -> h.deserializeNBT(nbt));
        }
    }
}
