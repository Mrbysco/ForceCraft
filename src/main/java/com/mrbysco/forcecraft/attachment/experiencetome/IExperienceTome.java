package com.mrbysco.forcecraft.attachment.experiencetome;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IExperienceTome extends INBTSerializable<CompoundTag> {
	float getExperienceValue();

	void addToExperienceValue();

	void subtractFromExperienceValue();

	void setExperienceValue(float newExp);
}
