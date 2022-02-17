package com.mrbysco.forcecraft.capablilities.playermodifier;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;

public class PlayerModifierCapability implements IPlayerModifier, ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
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
        return writeNBT(this);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        readNBT(this, nbt);
    }

    public static CompoundTag writeNBT(IPlayerModifier instance) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("attackDamage", instance.getAttackDamage());
        tag.putFloat("wingPower", instance.getWingPower());
        tag.putFloat("flightCounter", instance.getFlightTimer());
        tag.putFloat("damage", instance.getDamage());
        tag.putFloat("heatDamage", instance.getHeatDamage());
        tag.putInt("heatPieces", instance.getHeatPieces());
        tag.putInt("luckLevel", instance.getLuckLevel());
        tag.putInt("armorPieces", instance.getArmorPieces());
        tag.putBoolean("bane", instance.hasBane());
        tag.putInt("bleeding", instance.getBleedingLevel());
        return tag;
    }

    public static void readNBT(IPlayerModifier instance, CompoundTag tag) {
        instance.setAttackDamage(tag.getFloat("attackDamage"));
        instance.setWingPower(tag.getFloat("wingPower"));
        instance.setFlightTimer(tag.getFloat("flightCounter"));
        instance.setDamage(tag.getFloat("damage"));
        instance.setHeatDamage(tag.getFloat("heatDamage"));
        instance.setHeatPieces(tag.getInt("heatPieces"));
        instance.setLuckLevel(tag.getInt("luckLevel"));
        instance.setArmorPieces(tag.getInt("armorPieces"));
        instance.setBane(tag.getBoolean("bane"));
        instance.setBleeding(tag.getInt("bleeding"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_PLAYERMOD.orEmpty(cap, LazyOptional.of(() -> this));
    }

    public final Capability<IPlayerModifier> getCapability(){
        return CAPABILITY_PLAYERMOD;
    }
}
