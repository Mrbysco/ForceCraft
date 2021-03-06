package com.mrbysco.forcecraft.capablilities.playermodifier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PlayerModifierStorage implements Capability.IStorage<IPlayerModifier> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IPlayerModifier> capability, IPlayerModifier instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putFloat("attackDamage", instance.getAttackDamage());
        nbt.putFloat("wingPower", instance.getWingPower());
        nbt.putFloat("flightCounter", instance.getFlightTimer());
        nbt.putFloat("damage", instance.getDamage());
        nbt.putFloat("heatDamage", instance.getHeatDamage());
        nbt.putInt("heatPieces", instance.getHeatPieces());
        nbt.putInt("luckLevel", instance.getLuckLevel());
        nbt.putInt("armorPieces", instance.getArmorPieces());
        nbt.putBoolean("bane", instance.hasBane());
        nbt.putInt("bleeding", instance.getBleedingLevel());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IPlayerModifier> capability, IPlayerModifier instance, Direction side, INBT nbtIn) {
        if(nbtIn instanceof CompoundNBT){
            CompoundNBT nbt = ((CompoundNBT) nbtIn);

            instance.setAttackDamage(nbt.getFloat("attackDamage"));
            instance.setWingPower(nbt.getFloat("wingPower"));
            instance.setFlightTimer(nbt.getFloat("flightCounter"));
            instance.setDamage(nbt.getFloat("damage"));
            instance.setHeatDamage(nbt.getFloat("heatDamage"));
            instance.setHeatPieces(nbt.getInt("heatPieces"));
            instance.setLuckLevel(nbt.getInt("luckLevel"));
            instance.setArmorPieces(nbt.getInt("armorPieces"));
            instance.setBane(nbt.getBoolean("bane"));
            instance.setBleeding(nbt.getInt("bleeding"));
        }
    }
}
