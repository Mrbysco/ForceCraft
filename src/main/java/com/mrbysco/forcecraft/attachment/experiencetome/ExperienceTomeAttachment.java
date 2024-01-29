package com.mrbysco.forcecraft.attachment.experiencetome;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class ExperienceTomeAttachment implements IExperienceTome, INBTSerializable<CompoundTag> {
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
		CompoundTag tag = new CompoundTag();
		tag.putFloat("experience", this.getExperienceValue());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.setExperienceValue(tag.getFloat("experience"));
	}
}
