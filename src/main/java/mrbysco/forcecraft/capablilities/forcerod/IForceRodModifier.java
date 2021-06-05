package mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IForceRodModifier extends INBTSerializable<CompoundNBT> {

    boolean isRodOfHealing(int level);
    void setRodOfHealing(boolean newVal, int level);

    boolean hasCamoModifier();
    void setCamoModifier(boolean newVal);

    GlobalPos getHomeLocation();
    void setHomeLocation(GlobalPos pos);
    void teleportPlayerToLocation(PlayerEntity player, GlobalPos pos);
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
