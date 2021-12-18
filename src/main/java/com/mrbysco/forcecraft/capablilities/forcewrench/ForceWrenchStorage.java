package com.mrbysco.forcecraft.capablilities.forcewrench;

import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEWRENCH;

public class ForceWrenchStorage implements Capability.IStorage<IForceWrench> {

    public ForceWrenchStorage() {
    }

    public static void attachInformation(ItemStack stack, List<Component> tooltip) {
        ForceToolData fd = new ForceToolData(stack);
        fd.attachInformation(tooltip);
        stack.getCapability(CAPABILITY_FORCEWRENCH).ifPresent((cap) -> {
            if(cap.getStoredName() != null && !cap.getStoredName().isEmpty()){ // idk what this is
                tooltip.add(new TextComponent("Stored: ").withStyle(ChatFormatting.GOLD)
                        .append(new TranslatableComponent(cap.getStoredName()).withStyle(ChatFormatting.GRAY)));
            }
        });
    }

    @Nullable
    @Override
    public Tag writeNBT(Capability<IForceWrench> capability, IForceWrench instance, Direction side) {
        CompoundTag nbt = serializeNBT(instance);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IForceWrench> capability, IForceWrench instance, Direction side, Tag nbtIn) {
        deserializeNBT(instance, nbtIn);
    }

    public static CompoundTag serializeNBT(IForceWrench instance) {
        if (instance == null) {
            return null;
        }
        CompoundTag nbt  = new CompoundTag();
        if(instance.getStoredBlockNBT() != null) {
            nbt.put("storedNBT", instance.getStoredBlockNBT());
        }

        if(instance.getStoredBlockState() != null) {
            nbt.put("storedBlockState", NbtUtils.writeBlockState(instance.getStoredBlockState()));
        }
        if(!instance.getStoredName().isEmpty()) {
            nbt.putString("name", instance.getStoredName());
        }

        return nbt;
    }

    public static void deserializeNBT(IForceWrench instance, Tag nbtIn) {
        if (nbtIn instanceof CompoundTag nbt) {
            instance.storeBlockNBT(nbt.getCompound("storedNBT"));
            if(nbt.contains("storedBlockState")) {
                instance.storeBlockState(NbtUtils.readBlockState(nbt.getCompound("storedBlockState")));
            }
            instance.setBlockName(nbt.getString("name"));
        }
    }
}
