package com.mrbysco.forcecraft.capablilities.experiencetome;

import net.minecraft.nbt.CompoundTag;

import java.util.concurrent.Callable;

public class ExperienceTomeFactory implements Callable<IExperienceTome> {
    @Override
    public IExperienceTome call() throws Exception {
        return new IExperienceTome() {

            private float experienceStored = 0.0F;

            @Override
            public float getExperienceValue() {
                return experienceStored;
            }

            @Override
            public void addToExperienceValue() {

            }

            @Override
            public void subtractFromExperienceValue() {

            }

            @Override
            public void setExperienceValue(float newExp) {
                experienceStored = newExp;
            }

            @Override
            public CompoundTag serializeNBT() {
                return null;
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {

            }
        };
    }
}
