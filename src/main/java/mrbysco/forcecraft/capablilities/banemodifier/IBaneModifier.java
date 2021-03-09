package mrbysco.forcecraft.capablilities.banemodifier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBaneModifier extends INBTSerializable<CompoundNBT> {

    boolean canTeleport();
    void setTeleportAbility(boolean canTeleport);
}
