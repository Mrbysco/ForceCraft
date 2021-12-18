package com.mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEROD;

public class ForceRodProvider implements ICapabilitySerializable<Tag>, ICapabilityProvider {
    private LazyOptional<IForceRodModifier> instance;
    private IForceRodModifier forceRod;

    public ForceRodProvider(){
        this.forceRod = CAPABILITY_FORCEROD.getDefaultInstance();
        this.instance = LazyOptional.of(() -> forceRod);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_FORCEROD.orEmpty(cap, instance);
    }

    @Override
    public Tag serializeNBT() {
        return CAPABILITY_FORCEROD.writeNBT(forceRod, null);
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CAPABILITY_FORCEROD.readNBT(forceRod, null, nbt);
    }

    public final Capability<IForceRodModifier> getCapability(){
        return CAPABILITY_FORCEROD;
    }
}
