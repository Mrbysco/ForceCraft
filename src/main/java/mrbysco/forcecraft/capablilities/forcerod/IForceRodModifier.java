package mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IForceRodModifier extends INBTSerializable<CompoundNBT> {

    boolean isRodOfHealing(int level);
    void setRodOfHealing(boolean newVal, int level);

    boolean hasCamoModifier();
    void setCamoModifier(boolean newVal);

    BlockPos getHomeLocation();
    void setHomeLocation(BlockPos pos);
    void teleportPlayerToLocation(PlayerEntity player, BlockPos pos);
    boolean hasEnderModifier();
    void setEnderModifier(boolean newVal);
    boolean isRodofEnder();

    boolean hasSightModifier();
    void setSightModifier(boolean newVal);

    boolean hasLight();
    void setLight(boolean val);
}
