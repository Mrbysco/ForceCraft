package com.mrbysco.forcecraft.capablilities.magnet;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_MAGNET;

public class MagnetStorage implements Capability.IStorage<IMagnet> {

    public MagnetStorage() {
    }

    public static void attachInformation(ItemStack stack, List<Component> tooltip) {
        stack.getCapability(CAPABILITY_MAGNET).ifPresent(cap -> {
            if(cap.isActivated()) {
                tooltip.add(new TranslatableComponent("forcecraft.magnet_glove.active").withStyle(ChatFormatting.GREEN));
            } else {
                tooltip.add(new TranslatableComponent("forcecraft.magnet_glove.deactivated").withStyle(ChatFormatting.RED));
            }
            tooltip.add(TextComponent.EMPTY);
            tooltip.add(new TranslatableComponent("forcecraft.magnet_glove.change").withStyle(ChatFormatting.BOLD));
        });
    }

    @Nullable
    @Override
    public Tag writeNBT(Capability<IMagnet> capability, IMagnet instance, Direction side) {
        CompoundTag nbt = serializeNBT(instance);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IMagnet> capability, IMagnet instance, Direction side, Tag nbtIn) {
        deserializeNBT(instance, nbtIn);
    }

    public static CompoundTag serializeNBT(IMagnet instance) {
        if (instance == null) {
            return null;
        }
        CompoundTag nbt  = new CompoundTag();
        nbt.putBoolean("activated", instance.isActivated());

        return nbt;
    }

    public static void deserializeNBT(IMagnet instance, Tag nbtIn) {
        if (nbtIn instanceof CompoundTag nbt) {
            instance.setActivation(nbt.getBoolean("activated"));
        }
    }
}
