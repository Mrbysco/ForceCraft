package mrbysco.forcecraft.capablilities.magnet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMagnet extends INBTSerializable<CompoundNBT> {

    boolean isActivated();

    void activate();
    void deactivate();

    void setActivation(boolean active);
}
