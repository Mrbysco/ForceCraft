package mrbysco.forcecraft.capablilities.forcewrench;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEWRENCH;

public class ForceWrenchProvider implements ICapabilitySerializable<INBT>, ICapabilityProvider {
    private LazyOptional<IForceWrench> instance;
    private IForceWrench forceWrench;

    public ForceWrenchProvider(){
        this.forceWrench = CAPABILITY_FORCEWRENCH.getDefaultInstance();
        this.instance = LazyOptional.of(() -> forceWrench);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_FORCEWRENCH.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY_FORCEWRENCH.writeNBT(forceWrench, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY_FORCEWRENCH.readNBT(forceWrench, null, nbt);
    }

    public final Capability<IForceWrench> getCapability(){
        return CAPABILITY_FORCEWRENCH;
    }
}
