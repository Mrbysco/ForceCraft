package com.mrbysco.forcecraft.capablilities.experiencetome;

import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_EXPTOME;

public class ExperienceTomeProvider implements ICapabilitySerializable<Tag>, ICapabilityProvider {
    private LazyOptional<IExperienceTome> instance;
    private IExperienceTome experienceTome;

    public ExperienceTomeProvider(){
        this.experienceTome = CAPABILITY_EXPTOME.getDefaultInstance();
        this.instance = LazyOptional.of(() -> experienceTome);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_EXPTOME.orEmpty(cap, instance);
    }

    @Override
    public Tag serializeNBT() {
        return CAPABILITY_EXPTOME.writeNBT(experienceTome, null);
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CAPABILITY_EXPTOME.readNBT(experienceTome, null, nbt);
    }

    public final Capability<IExperienceTome> getCapability() {
        return CAPABILITY_EXPTOME;
    }
}
