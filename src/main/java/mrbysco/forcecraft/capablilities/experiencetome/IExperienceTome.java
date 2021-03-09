package mrbysco.forcecraft.capablilities.experiencetome;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IExperienceTome extends INBTSerializable<CompoundNBT> {

    float getExperienceValue();
    void addToExperienceValue();
    void subtractFromExperienceValue();
    void setExperienceValue(float newExp);

}
