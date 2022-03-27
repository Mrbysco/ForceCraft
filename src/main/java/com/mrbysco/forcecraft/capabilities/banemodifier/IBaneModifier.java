package com.mrbysco.forcecraft.capabilities.banemodifier;

public interface IBaneModifier {
	boolean canTeleport();

	void setTeleportAbility(boolean canTeleport);

	boolean canExplode();

	void setExplodeAbility(boolean canExplode);
}
