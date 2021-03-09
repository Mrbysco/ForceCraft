package mrbysco.forcecraft.capablilities.magnet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class MagnetStorage implements Capability.IStorage<IMagnet> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IMagnet> capability, IMagnet instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putBoolean("activated", instance.isActivated());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IMagnet> capability, IMagnet instance, Direction side, INBT nbtIn) {
        if(nbtIn instanceof CompoundNBT){
            CompoundNBT nbt = (CompoundNBT) nbtIn;
            instance.setActivation(nbt.getBoolean("activated"));
        }
    }
}
