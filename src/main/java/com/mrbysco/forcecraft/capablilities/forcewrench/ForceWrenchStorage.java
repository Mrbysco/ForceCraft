package com.mrbysco.forcecraft.capablilities.forcewrench;

import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEWRENCH;

public class ForceWrenchStorage implements Capability.IStorage<IForceWrench> {

    public ForceWrenchStorage() {
    }

    public static void attachInformation(ItemStack stack, List<ITextComponent> tooltip) {
        ForceToolData fd = new ForceToolData(stack);
        fd.attachInformation(tooltip);
        stack.getCapability(CAPABILITY_FORCEWRENCH).ifPresent((cap) -> {
            if(cap.getStoredName() != null && !cap.getStoredName().isEmpty()){ // idk what this is
                tooltip.add(new StringTextComponent("Stored: ").withStyle(TextFormatting.GOLD)
                        .append(new TranslationTextComponent(cap.getStoredName()).withStyle(TextFormatting.GRAY)));
            }
        });
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IForceWrench> capability, IForceWrench instance, Direction side) {
        CompoundNBT nbt = serializeNBT(instance);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IForceWrench> capability, IForceWrench instance, Direction side, INBT nbtIn) {
        deserializeNBT(instance, nbtIn);
    }

    public static CompoundNBT serializeNBT(IForceWrench instance) {
        if (instance == null) {
            return null;
        }
        CompoundNBT nbt  = new CompoundNBT();
        if(instance.getStoredBlockNBT() != null) {
            nbt.put("storedNBT", instance.getStoredBlockNBT());
        }

        if(instance.getStoredBlockState() != null) {
            nbt.put("storedBlockState", NBTUtil.writeBlockState(instance.getStoredBlockState()));
        }
        if(!instance.getStoredName().isEmpty()) {
            nbt.putString("name", instance.getStoredName());
        }

        return nbt;
    }

    public static void deserializeNBT(IForceWrench instance, INBT nbtIn) {
        if (nbtIn instanceof CompoundNBT) {
            CompoundNBT nbt = (CompoundNBT) nbtIn;
            instance.storeBlockNBT(nbt.getCompound("storedNBT"));
            if(nbt.contains("storedBlockState")) {
                instance.storeBlockState(NBTUtil.readBlockState(nbt.getCompound("storedBlockState")));
            }
            instance.setBlockName(nbt.getString("name"));
        }
    }
}
