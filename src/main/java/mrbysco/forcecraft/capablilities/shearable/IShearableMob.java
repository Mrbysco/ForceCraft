package mrbysco.forcecraft.capablilities.shearable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IShearableMob extends INBTSerializable<CompoundNBT> {

    boolean canBeSheared();
    void update();
    int getTimer();
    void setTimer(int newTimer);
    void setShearable(boolean newVal);
    void setSheared(boolean wasSheared);
}
