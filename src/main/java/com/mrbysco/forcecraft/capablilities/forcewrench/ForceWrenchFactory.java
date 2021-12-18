package com.mrbysco.forcecraft.capablilities.forcewrench;

import net.minecraft.world.level.block.state.BlockState;

import java.util.concurrent.Callable;

public class ForceWrenchFactory implements Callable<IForceWrench> {
    @Override
    public IForceWrench call() throws Exception {
        return new IForceWrench() {

            net.minecraft.nbt.CompoundTag storedBlockNBT = null;
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
        };
    }
}
