package mrbysco.forcecraft.capablilities.banemodifier;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_BANE;

public class BaneProvider implements ICapabilitySerializable<INBT>, ICapabilityProvider {
    private LazyOptional<IBaneModifier> instance;
    private IBaneModifier bane;

    public BaneProvider(){
        this.bane = CAPABILITY_BANE.getDefaultInstance();
        this.instance = LazyOptional.of(() -> bane);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_BANE.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY_BANE.writeNBT(bane, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY_BANE.readNBT(bane, null, nbt);
    }

    public Capability<IBaneModifier> getCapability(){
        return CAPABILITY_BANE;
    }
}
