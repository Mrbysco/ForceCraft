package mrbysco.forcecraft.capablilities.shearable;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_SHEARABLE;

public class ShearableProvider implements ICapabilitySerializable<INBT>, ICapabilityProvider {
    private LazyOptional<IShearableMob> instance;
    private IShearableMob shearable;

    public ShearableProvider(){
        this.shearable = CAPABILITY_SHEARABLE.getDefaultInstance();
        this.instance = LazyOptional.of(() -> shearable);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_SHEARABLE.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY_SHEARABLE.writeNBT(shearable, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY_SHEARABLE.readNBT(shearable, null, nbt);
    }

    public final Capability<IShearableMob> getCapability(){
        return CAPABILITY_SHEARABLE;
    }
}
