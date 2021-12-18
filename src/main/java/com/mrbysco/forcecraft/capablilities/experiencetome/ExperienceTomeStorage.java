package com.mrbysco.forcecraft.capablilities.experiencetome;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ExperienceTomeStorage implements Capability.IStorage<IExperienceTome> {

    @Nullable
    @Override
    public Tag writeNBT(Capability<IExperienceTome> capability, IExperienceTome instance, Direction side) {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("experience", instance.getExperienceValue());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IExperienceTome> capability, IExperienceTome instance, Direction side, Tag nbtIn) {
        if(nbtIn instanceof CompoundTag nbt) {
            instance.setExperienceValue(nbt.getFloat("experience"));
        }
    }
}
