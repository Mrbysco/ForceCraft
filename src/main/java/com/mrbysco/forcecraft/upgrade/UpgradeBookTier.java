package com.mrbysco.forcecraft.upgrade;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum UpgradeBookTier implements StringRepresentable {
	ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN;

	public static final Codec<UpgradeBookTier> CODEC = StringRepresentable.fromEnum(UpgradeBookTier::values);

	public int pointsForLevelup() {
		return 96 + ordinal() * 4;
	}

	public boolean isFinal() {
		return this == SEVEN;
	}

	public UpgradeBookTier next() {
		return isFinal() ? this : values()[ordinal() + 1];
	}

	public int asInt() {
		return ordinal() + 1;
	}

	@Override
	public String getSerializedName() {
		return String.valueOf(ordinal());
	}
}
