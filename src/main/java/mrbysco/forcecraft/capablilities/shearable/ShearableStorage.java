package mrbysco.forcecraft.capablilities.shearable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ShearableStorage implements Capability.IStorage<IShearableMob> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IShearableMob> capability, IShearableMob instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt("timer", instance.getTimer());
        nbt.putBoolean("shearable", instance.canBeSheared());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IShearableMob> capability, IShearableMob instance, Direction side, INBT nbtIn) {
        if(nbtIn instanceof CompoundNBT){
            CompoundNBT nbt = (CompoundNBT) nbtIn;

            instance.setTimer(nbt.getInt("timer"));
        }
    }
}
