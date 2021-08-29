package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capablilities.magnet.IMagnet;
import com.mrbysco.forcecraft.capablilities.magnet.MagnetProvider;
import com.mrbysco.forcecraft.capablilities.magnet.MagnetStorage;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_MAGNET;

public class MagnetGloveItem extends BaseItem {

    public MagnetGloveItem(Item.Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(CAPABILITY_MAGNET == null) {
            return null;
        }
        return new MagnetProvider();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(playerIn.isSneaking()) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            stack.getCapability(CAPABILITY_MAGNET).ifPresent((cap) -> {
                boolean state = cap.isActivated();
                cap.setActivation(!state);
                worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            });
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof PlayerEntity && !(entityIn instanceof FakePlayer)) {
            if(itemSlot >= 0 && itemSlot <= PlayerInventory.getHotbarSize()) {
                IMagnet magnetCap = stack.getCapability(CAPABILITY_MAGNET).orElse(null);
                if (magnetCap != null && magnetCap.isActivated()) {
                    ((PlayerEntity)entityIn).addPotionEffect(new EffectInstance(ForceEffects.MAGNET.get(), 20, 1, true, false));
                }
            }
        }
    }

    // ShareTag for server->client capability data sync
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT nbt = super.getShareTag(stack);

        IMagnet cap = stack.getCapability(CAPABILITY_MAGNET).orElse(null);
        if(cap != null) {
            CompoundNBT shareTag = MagnetStorage.serializeNBT(cap);
            if(nbt == null) {
                nbt = new CompoundNBT();
            }
            nbt.put(Reference.MOD_ID, shareTag);
        }
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(nbt == null || !nbt.contains(Reference.MOD_ID)) {
            return;
        }

        IMagnet cap = stack.getCapability(CAPABILITY_MAGNET).orElse(null);
        if(cap != null) {
            INBT shareTag = nbt.get(Reference.MOD_ID);
            MagnetStorage.deserializeNBT(cap, shareTag);
        }
        super.readShareTag(stack, nbt);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
        MagnetStorage.attachInformation(stack, lores);
        super.addInformation(stack, worldIn, lores, flagIn);
    }
}
