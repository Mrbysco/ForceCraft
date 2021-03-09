package mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ForceRodStorage implements Capability.IStorage<IForceRodModifier> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IForceRodModifier> capability, IForceRodModifier instance, Direction side) {
        CompoundNBT nbt  = new CompoundNBT();

        nbt.putBoolean("healingOne", instance.isRodOfHealing(1));
        nbt.putBoolean("healingTwo", instance.isRodOfHealing(2));
        nbt.putBoolean("healingThree", instance.isRodOfHealing(3));

        nbt.putBoolean("camo", instance.hasCamoModifier());

        nbt.putBoolean("ender", instance.hasEnderModifier());

        nbt.putBoolean("sight", instance.hasSightModifier());

        nbt.putBoolean("light", instance.hasLight());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IForceRodModifier> capability, IForceRodModifier instance, Direction side, INBT nbtIn) {
        if(nbtIn instanceof CompoundNBT){
            CompoundNBT nbt = (CompoundNBT) nbtIn;
            instance.setRodOfHealing(nbt.getBoolean("healingOne"), 1);
            instance.setRodOfHealing(nbt.getBoolean("healingTwo"), 2);
            instance.setRodOfHealing(nbt.getBoolean("healingThree"), 3);

            instance.setCamoModifier(nbt.getBoolean("camo"));

            instance.setEnderModifier(nbt.getBoolean("ender"));

            instance.setSightModifier(nbt.getBoolean("sight"));
            instance.setLight(nbt.getBoolean("light"));
        }
    }
}
