package mrbysco.forcecraft.capablilities.forcewrench;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

import java.util.concurrent.Callable;

public class ForceWrenchFactory implements Callable<IForceWrench> {
    @Override
    public IForceWrench call() throws Exception {
        return new IForceWrench() {

            CompoundNBT storedBlockNBT = null;
            BlockState storedBlockState = null;
            String name = null;


            @Override
            public boolean hasBlockStored() {
                if(storedBlockState == null)
                    return false;
                else
                    return true;
            }

            @Override
            public boolean canStoreBlock() {
                return hasBlockStored();
            }

            @Override
            public CompoundNBT getStoredBlockNBT() {
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
            public void storeBlockNBT(CompoundNBT nbt) {
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
