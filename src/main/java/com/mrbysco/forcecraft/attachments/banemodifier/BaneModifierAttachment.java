package com.mrbysco.forcecraft.attachments.banemodifier;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class BaneModifierAttachment implements IBaneModifier, ValueIOSerializable {
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
	public void deserialize(ValueInput input) {
		this.setTeleportAbility(input.getBooleanOr("canTeleport", false));
		this.setExplodeAbility(input.getBooleanOr("canExplode", false));
	}

	@Override
	public void serialize(ValueOutput output) {
		output.putBoolean("canTeleport", this.canTeleport());
		output.putBoolean("canExplode", this.canExplode());
	}
}
