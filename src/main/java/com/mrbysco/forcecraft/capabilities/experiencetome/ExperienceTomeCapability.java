package com.mrbysco.forcecraft.capabilities.experiencetome;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_EXPTOME;

public class ExperienceTomeCapability implements IExperienceTome, ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
    private float experienceStored = 0.0F;

    @Override
    public float getExperienceValue() {
        return experienceStored;
    }

    @Override
    public void addToExperienceValue() {
        //UNUSED
    }

    @Override
    public void subtractFromExperienceValue() {
        //UNUSED
    }

    @Override
    public void setExperienceValue(float newExp) {
        experienceStored = newExp;
    }

    @Override
    public CompoundTag serializeNBT() {
        return writeNBT(this);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        readNBT(this, nbt);
    }

    public CompoundTag writeNBT(IExperienceTome instance) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("experience", instance.getExperienceValue());
        return tag;
    }

    public void readNBT(IExperienceTome instance, CompoundTag tag) {
        instance.setExperienceValue(tag.getFloat("experience"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_EXPTOME.orEmpty(cap, LazyOptional.of(() -> this));
    }

    public final Capability<IExperienceTome> getCapability() {
        return CAPABILITY_EXPTOME;
    }
}
