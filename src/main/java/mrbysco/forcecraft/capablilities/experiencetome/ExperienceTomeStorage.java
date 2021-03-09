package mrbysco.forcecraft.capablilities.experiencetome;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ExperienceTomeStorage implements Capability.IStorage<IExperienceTome> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IExperienceTome> capability, IExperienceTome instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("experience", instance.getExperienceValue());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IExperienceTome> capability, IExperienceTome instance, Direction side, INBT nbtIn) {
        if(nbtIn instanceof CompoundNBT) {
            CompoundNBT nbt = (CompoundNBT) nbtIn;
            instance.setExperienceValue(instance.getExperienceValue());

        }
    }
}
