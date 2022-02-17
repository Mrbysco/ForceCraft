package com.mrbysco.forcecraft.capablilities.banemodifier;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_BANE;

public class BaneModifierCapability implements IBaneModifier, ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
    boolean canTeleport = true;

    @Override
    public boolean canTeleport() {
        return canTeleport;
    }

    @Override
    public void setTeleportAbility(boolean canTeleport) {
        this.canTeleport = canTeleport;
    }

    boolean canExplode = true;

    @Override
    public boolean canExplode() {
        return canExplode;
    }

    @Override
    public void setExplodeAbility(boolean canExplode) {
        this.canExplode = canExplode;
    }

    @Override
    public CompoundTag serializeNBT() {
        return writeNBT(this);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        readNBT(this, nbt);
    }

    public CompoundTag writeNBT(IBaneModifier instance) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("canTeleport", instance.canTeleport());
        tag.putBoolean("canExplode", instance.canExplode());
        return tag;
    }

    public void readNBT(IBaneModifier instance, CompoundTag tag) {
        instance.setTeleportAbility(tag.getBoolean("canTeleport"));
        instance.setExplodeAbility(tag.getBoolean("canExplode"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_BANE.orEmpty(cap, LazyOptional.of(() -> this));
    }

    public Capability<IBaneModifier> getCapability(){
        return CAPABILITY_BANE;
    }
}
