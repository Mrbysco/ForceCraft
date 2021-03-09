package mrbysco.forcecraft.capablilities.toolmodifier;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ToolModProvider implements ICapabilitySerializable<INBT>, ICapabilityProvider {
    private LazyOptional<IToolModifier> instance;
    private IToolModifier tool;

    public ToolModProvider(){
        this.tool = CAPABILITY_TOOLMOD.getDefaultInstance();
        this.instance = LazyOptional.of(() -> tool);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_TOOLMOD.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY_TOOLMOD.writeNBT(tool, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY_TOOLMOD.readNBT(tool, null, nbt);
    }

    public final Capability<IToolModifier> getCapability(){
        return CAPABILITY_TOOLMOD;
    }
}
