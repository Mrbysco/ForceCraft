package mrbysco.forcecraft.capablilities.playermodifier;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;

public class PlayerModifierProvider implements ICapabilitySerializable<INBT>, ICapabilityProvider {
    private LazyOptional<IPlayerModifier> instance;
    private IPlayerModifier playerModifier;

    public PlayerModifierProvider(){
        this.playerModifier = CAPABILITY_PLAYERMOD.getDefaultInstance();
        this.instance = LazyOptional.of(() -> playerModifier);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_PLAYERMOD.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY_PLAYERMOD.writeNBT(playerModifier, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY_PLAYERMOD.readNBT(playerModifier, null, nbt);
    }

    public final Capability<IPlayerModifier> getCapability(){
        return CAPABILITY_PLAYERMOD;
    }
}
