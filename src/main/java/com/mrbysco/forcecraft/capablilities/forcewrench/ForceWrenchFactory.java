package com.mrbysco.forcecraft.capablilities.forcewrench;

import net.minecraft.block.BlockState;

import java.util.concurrent.Callable;

public class ForceWrenchFactory implements Callable<IForceWrench> {
    @Override
    public IForceWrench call() throws Exception {
        return new IForceWrench() {

            net.minecraft.nbt.CompoundNBT storedBlockNBT = null;
            BlockState storedBlockState = null;
            String name = null;

            @Override
            public boolean hasBlockStored() {
                return storedBlockState != null;
            }

            @Override
            public boolean canStoreBlock() {
                return hasBlockStored();
            }

            @Override
            public net.minecraft.nbt.CompoundNBT getStoredBlockNBT() {
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
            public void storeBlockNBT(net.minecraft.nbt.CompoundNBT nbt) {
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
                name = null;
            }
        };
    }
}
