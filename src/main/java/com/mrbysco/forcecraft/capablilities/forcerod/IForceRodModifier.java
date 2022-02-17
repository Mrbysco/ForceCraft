package com.mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface IForceRodModifier extends INBTSerializable<CompoundTag> {

    /**
     * Modifier: Healing
     * Items: Ghast Tear
     * Levels: 3
     * Effect: Allows the Force Rod to give Healing depending on the level set
     */

    int getHealingLevel();
    boolean hasHealing();
    void incrementHealing();
    void setHealing(int healing);

    boolean hasCamoModifier();
    void setCamoModifier(boolean newVal);

    GlobalPos getHomeLocation();
    void setHomeLocation(GlobalPos pos);
    void teleportPlayerToLocation(Player player, GlobalPos pos);
    boolean hasEnderModifier();
    void setEnderModifier(boolean newVal);
    boolean isRodofEnder();

    boolean hasSightModifier();
    void setSightModifier(boolean newVal);

    boolean hasLight();
    void setLight(boolean val);

    /**
     * Modifier: Speed
     * Levels: 3
     * Effect: when using the rod give speed to player
     */
    int getSpeedLevel();
    void incrementSpeed();
    void setSpeed(int newSpeed);
}
