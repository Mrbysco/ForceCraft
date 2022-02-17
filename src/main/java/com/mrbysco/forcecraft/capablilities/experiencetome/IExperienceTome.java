package com.mrbysco.forcecraft.capablilities.experiencetome;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IExperienceTome extends INBTSerializable<CompoundTag> {
    float getExperienceValue();
    void addToExperienceValue();
    void subtractFromExperienceValue();
    void setExperienceValue(float newExp);
}
