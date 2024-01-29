package com.mrbysco.forcecraft.attachment.playermodifier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class PlayerModifierAttachment implements IPlayerModifier, INBTSerializable<CompoundTag> {
	private float attackDamage = ((float) Attributes.ATTACK_DAMAGE.getDefaultValue());
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
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putFloat("attackDamage", this.getAttackDamage());
		tag.putFloat("wingPower", this.getWingPower());
		tag.putFloat("flightCounter", this.getFlightTimer());
		tag.putFloat("damage", this.getDamage());
		tag.putFloat("heatDamage", this.getHeatDamage());
		tag.putInt("heatPieces", this.getHeatPieces());
		tag.putInt("luckLevel", this.getLuckLevel());
		tag.putInt("armorPieces", this.getArmorPieces());
		tag.putBoolean("bane", this.hasBane());
		tag.putInt("bleeding", this.getBleedingLevel());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.setAttackDamage(tag.getFloat("attackDamage"));
		this.setWingPower(tag.getFloat("wingPower"));
		this.setFlightTimer(tag.getFloat("flightCounter"));
		this.setDamage(tag.getFloat("damage"));
		this.setHeatDamage(tag.getFloat("heatDamage"));
		this.setHeatPieces(tag.getInt("heatPieces"));
		this.setLuckLevel(tag.getInt("luckLevel"));
		this.setArmorPieces(tag.getInt("armorPieces"));
		this.setBane(tag.getBoolean("bane"));
		this.setBleeding(tag.getInt("bleeding"));
	}
}
