package com.mrbysco.forcecraft.attachment.playermodifier;

public interface IPlayerModifier {

	//Damage
	float getAttackDamage();

	void setAttackDamage(float newDamage);

	void addAttackDamage(float newDamage);

	//Wing
	float getWingPower();

	void setWingPower(float newWingPower);

	float getFlightTimer();

	void subtractFlightTimer();

	void setFlightTimer(float newFlightCounter);

	//Heat
	float getHeatDamage();

	void setHeatDamage(float newDamage);

	void addHeatDamage(float newDamage);

	boolean hasHeatDamage();

	void setHeatPieces(int pieces);

	int getHeatPieces();

	float getDamage();

	void setDamage(float newDamage);

	//Luck
	int getLuckLevel();

	void setLuckLevel(int newLuck);

	void incrementLuckLevel(int newLuck);

	//SetBonus
	boolean hasFullSet();

	int getArmorPieces();

	void incrementArmorPieces();

	void setArmorPieces(int value);

	//Bane
	boolean hasBane();

	void setBane(boolean value);

	//Bleeding
	boolean hasBleeding();

	int getBleedingLevel();

	void setBleeding(int value);
}
