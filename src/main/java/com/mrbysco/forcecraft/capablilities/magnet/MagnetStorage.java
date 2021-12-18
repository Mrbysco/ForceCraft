package com.mrbysco.forcecraft.capablilities.magnet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_MAGNET;

public class MagnetStorage implements Capability.IStorage<IMagnet> {

    public MagnetStorage() {
    }

    public static void attachInformation(ItemStack stack, List<ITextComponent> tooltip) {
        stack.getCapability(CAPABILITY_MAGNET).ifPresent(cap -> {
            if(cap.isActivated()) {
                tooltip.add(new TranslationTextComponent("forcecraft.magnet_glove.active").withStyle(TextFormatting.GREEN));
            } else {
                tooltip.add(new TranslationTextComponent("forcecraft.magnet_glove.deactivated").withStyle(TextFormatting.RED));
            }
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add(new TranslationTextComponent("forcecraft.magnet_glove.change").withStyle(TextFormatting.BOLD));
        });
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IMagnet> capability, IMagnet instance, Direction side) {
        CompoundNBT nbt = serializeNBT(instance);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IMagnet> capability, IMagnet instance, Direction side, INBT nbtIn) {
        deserializeNBT(instance, nbtIn);
    }

    public static CompoundNBT serializeNBT(IMagnet instance) {
        if (instance == null) {
            return null;
        }
        CompoundNBT nbt  = new CompoundNBT();
        nbt.putBoolean("activated", instance.isActivated());

        return nbt;
    }

    public static void deserializeNBT(IMagnet instance, INBT nbtIn) {
        if (nbtIn instanceof CompoundNBT) {
            CompoundNBT nbt = (CompoundNBT) nbtIn;
            instance.setActivation(nbt.getBoolean("activated"));
        }
    }
}
