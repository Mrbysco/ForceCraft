package com.mrbysco.forcecraft.capablilities.banemodifier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BaneModifierStorage implements Capability.IStorage<IBaneModifier> {
    @Nullable
    @Override
    public Tag writeNBT(Capability<IBaneModifier> capability, IBaneModifier instance, Direction side) {
        CompoundTag nbt = new CompoundTag();
        
        nbt.putBoolean("canTeleport", instance.canTeleport());
        nbt.putBoolean("canExplode", instance.canExplode());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IBaneModifier> capability, IBaneModifier instance, Direction side, Tag nbtIn) {
        if(nbtIn instanceof CompoundTag nbt){

            instance.setTeleportAbility(nbt.getBoolean("canTeleport"));
            instance.setExplodeAbility(nbt.getBoolean("canExplode"));
        }
    }
}
