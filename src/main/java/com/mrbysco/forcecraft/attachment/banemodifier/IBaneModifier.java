package com.mrbysco.forcecraft.attachment.banemodifier;

public interface IBaneModifier {
	boolean canTeleport();

	void setTeleportAbility(boolean canTeleport);

	boolean canExplode();

	void setExplodeAbility(boolean canExplode);
}
