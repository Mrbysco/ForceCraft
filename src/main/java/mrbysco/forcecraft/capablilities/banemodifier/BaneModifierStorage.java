package mrbysco.forcecraft.capablilities.banemodifier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BaneModifierStorage implements Capability.IStorage<IBaneModifier> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IBaneModifier> capability, IBaneModifier instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        
        nbt.putBoolean("canTeleport", instance.canTeleport());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IBaneModifier> capability, IBaneModifier instance, Direction side, INBT nbtIn) {
        if(nbtIn instanceof CompoundNBT){
            CompoundNBT nbt = ((CompoundNBT) nbtIn);

            instance.setTeleportAbility(nbt.getBoolean("canTeleport"));
        }
    }
}
