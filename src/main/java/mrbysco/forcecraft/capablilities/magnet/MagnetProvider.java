package mrbysco.forcecraft.capablilities.magnet;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_MAGNET;

public class MagnetProvider implements ICapabilitySerializable<INBT>, ICapabilityProvider {
    private LazyOptional<IMagnet> instance;
    private IMagnet wrench;

    public MagnetProvider(){
        this.wrench = CAPABILITY_MAGNET.getDefaultInstance();
        this.instance = LazyOptional.of(() -> wrench);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_MAGNET.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY_MAGNET.writeNBT(wrench, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY_MAGNET.readNBT(wrench, null, nbt);
    }

    public final Capability<IMagnet> getCapability(){
        return CAPABILITY_MAGNET;
    }
}
