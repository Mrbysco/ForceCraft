package com.mrbysco.forcecraft.attachments.playermodifier;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class PlayerModifierAttachment implements IPlayerModifier, ValueIOSerializable {
	private float attackDamage = 2.0F; //Default of Attributes.ATTACK_DAMAGE
	private float wingPower = 0.0f;
	private float flightCounter = wingPower;
	private float heatDamage = 0.0f;
	private int heatPieces = 0;
	private int armorPieces = 0;
	private float damage = attackDamage + heatDamage;
	private int luck;
	private boolean bane;
	private int bleeding;

	@Override
	public float getAttackDamage() {
		return attackDamage;
	}

	@Override
	public void setAttackDamage(float newDamage) {
		attackDamage = newDamage;
	}

	@Override
	public void addAttackDamage(float newDamage) {
		attackDamage += newDamage;
	}

	@Override
	public float getWingPower() {
		return wingPower;
	}

	@Override
	public void setWingPower(float newWingPower) {
		wingPower = newWingPower;
	}

	@Override
	public float getFlightTimer() {
		return flightCounter;
	}

	@Override
	public void subtractFlightTimer() {
		flightCounter--;
	}

	@Override
	public void setFlightTimer(float newFlightCounter) {
		flightCounter = newFlightCounter;
	}

	@Override
	public float getHeatDamage() {
		return heatDamage;
	}

	@Override
	public void setHeatDamage(float newDamage) {
		heatDamage = newDamage;
	}

	@Override
	public boolean hasHeatDamage() {
		return heatDamage > 0.0F;
	}

	@Override
	public void setHeatPieces(int pieces) {
		this.heatPieces = pieces;
	}

	@Override
	public int getHeatPieces() {
		return heatPieces;
	}

	@Override
	public void addHeatDamage(float newDamage) {
		heatDamage += newDamage;
	}

	@Override
	public float getDamage() {
		return damage;
	}

	@Override
	public void setDamage(float newDamage) {
		damage = newDamage;
	}

	@Override
	public int getLuckLevel() {
		return luck;
	}

	@Override
	public void setLuckLevel(int newLuck) {
		luck = newLuck;
	}

	@Override
	public void incrementLuckLevel(int newLuck) {
		luck += newLuck;
	}

	@Override
	public boolean hasFullSet() {
		return armorPieces == 4;
	}

	@Override
	public int getArmorPieces() {
		return armorPieces;
	}

	@Override
	public void incrementArmorPieces() {
		armorPieces++;
	}

	@Override
	public void setArmorPieces(int value) {
		armorPieces = value;
	}

	@Override
	public boolean hasBane() {
		return bane;
	}

	@Override
	public void setBane(boolean value) {
		bane = value;
	}

	@Override
	public boolean hasBleeding() {
		return bleeding > 0;
	}

	@Override
	public int getBleedingLevel() {
		return bleeding;
	}

	@Override
	public void setBleeding(int value) {
		bleeding = value;
	}

	@Override
	public void serialize(ValueOutput output) {
		output.putFloat("attackDamage", this.getAttackDamage());
		output.putFloat("wingPower", this.getWingPower());
		output.putFloat("flightCounter", this.getFlightTimer());
		output.putFloat("damage", this.getDamage());
		output.putFloat("heatDamage", this.getHeatDamage());
		output.putInt("heatPieces", this.getHeatPieces());
		output.putInt("luckLevel", this.getLuckLevel());
		output.putInt("armorPieces", this.getArmorPieces());
		output.putBoolean("bane", this.hasBane());
		output.putInt("bleeding", this.getBleedingLevel());
	}

	@Override
	public void deserialize(ValueInput input) {
		this.setAttackDamage(input.getFloatOr("attackDamage", 0));
		this.setWingPower(input.getFloatOr("wingPower", 0));
		this.setFlightTimer(input.getFloatOr("flightCounter", 0));
		this.setDamage(input.getFloatOr("damage", 0));
		this.setHeatDamage(input.getFloatOr("heatDamage", 0));
		this.setHeatPieces(input.getIntOr("heatPieces", 0));
		this.setLuckLevel(input.getIntOr("luckLevel", 0));
		this.setArmorPieces(input.getIntOr("armorPieces", 0));
		this.setBane(input.getBooleanOr("bane", false));
		this.setBleeding(input.getIntOr("bleeding", 0));
	}
}
