package com.mrbysco.forcecraft.capablilities.forcewrench;

import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEWRENCH;

public class ForceWrenchCapability implements IForceWrench, ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
    CompoundTag storedBlockNBT = null;
    BlockState storedBlockState = null;
    String name = "";

    @Override
    public boolean hasBlockStored() {
        return storedBlockState != null;
    }

    @Override
    public boolean canStoreBlock() {
        return hasBlockStored();
    }

    @Override
    public net.minecraft.nbt.CompoundTag getStoredBlockNBT() {
        return storedBlockNBT;
    }

    @Override
    public BlockState getStoredBlockState() {
        return storedBlockState;
    }

    @Override
    public String getStoredName() {
        return name;
    }

    @Override
    public void storeBlockNBT(net.minecraft.nbt.CompoundTag nbt) {
        storedBlockNBT = nbt;
    }

    @Override
    public void storeBlockState(BlockState base) {
        storedBlockState = base;
    }

    @Override
    public void setBlockName(String name) {
        this.name = name;
    }

    @Override
    public void clearBlockStorage() {
        storedBlockState = null;
        storedBlockNBT = null;
        name = "";
    }

    public static void attachInformation(ItemStack stack, List<Component> tooltip) {
        ForceToolData fd = new ForceToolData(stack);
        fd.attachInformation(tooltip);
        stack.getCapability(CAPABILITY_FORCEWRENCH).ifPresent((cap) -> {
            if(cap.getStoredName() != null && !cap.getStoredName().isEmpty()){ // idk what this is
                tooltip.add(new TextComponent("Stored: ").withStyle(ChatFormatting.GOLD)
                        .append(new TranslatableComponent(cap.getStoredName()).withStyle(ChatFormatting.GRAY)));
            }
        });
    }

    @Override
    public CompoundTag serializeNBT() {
        return writeNBT(this);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        readNBT(this, nbt);
    }


    public static CompoundTag writeNBT(IForceWrench instance) {
        if (instance == null) {
            return null;
        }
        CompoundTag tag  = new CompoundTag();
        if(instance.getStoredBlockNBT() != null) {
            tag.put("storedNBT", instance.getStoredBlockNBT());
        }

        if(instance.getStoredBlockState() != null) {
            tag.put("storedBlockState", NbtUtils.writeBlockState(instance.getStoredBlockState()));
        }
        if(!instance.getStoredName().isEmpty()) {
            tag.putString("name", instance.getStoredName());
        }

        return tag;
    }

    public static void readNBT(IForceWrench instance, CompoundTag tag) {
        instance.storeBlockNBT(tag.getCompound("storedNBT"));
        if(tag.contains("storedBlockState")) {
            instance.storeBlockState(NbtUtils.readBlockState(tag.getCompound("storedBlockState")));
        }
        instance.setBlockName(tag.getString("name"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_FORCEWRENCH.orEmpty(cap, LazyOptional.of(() -> this));
    }

    public final Capability<IForceWrench> getCapability(){
        return CAPABILITY_FORCEWRENCH;
    }
}
