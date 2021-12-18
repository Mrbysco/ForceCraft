package com.mrbysco.forcecraft.capablilities.playermodifier;

import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;

public class PlayerModifierProvider implements ICapabilitySerializable<Tag>, ICapabilityProvider {
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
    public Tag serializeNBT() {
        return CAPABILITY_PLAYERMOD.writeNBT(playerModifier, null);
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CAPABILITY_PLAYERMOD.readNBT(playerModifier, null, nbt);
    }

    public final Capability<IPlayerModifier> getCapability(){
        return CAPABILITY_PLAYERMOD;
    }
}
