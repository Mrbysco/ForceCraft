package mrbysco.forcecraft.capablilities.forcewrench;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ForceWrenchStorage implements Capability.IStorage<IForceWrench> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IForceWrench> capability, IForceWrench instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();

        if(instance.getStoredBlockNBT() != null)
            nbt.put("storedNBT", instance.getStoredBlockNBT());
        if(instance.getStoredName() != null)
            nbt.putString("name", instance.getStoredName());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IForceWrench> capability, IForceWrench instance, Direction side, INBT nbtIn) {
        if(nbtIn instanceof CompoundNBT){
            CompoundNBT nbt = ((CompoundNBT) nbtIn);

            instance.storeBlockNBT(nbt.getCompound("storedNBT"));
            instance.setBlockName(nbt.getString("name"));
        }
    }
}
