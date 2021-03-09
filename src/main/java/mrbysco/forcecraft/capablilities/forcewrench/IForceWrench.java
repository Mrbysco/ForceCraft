package mrbysco.forcecraft.capablilities.forcewrench;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

public interface IForceWrench {

    boolean hasBlockStored();
    boolean canStoreBlock();
    CompoundNBT getStoredBlockNBT();
    BlockState getStoredBlockState();
    String getStoredName();

    void storeBlockNBT(CompoundNBT nbt);
    void storeBlockState(BlockState base);
    void setBlockName(String name);

    void clearBlockStorage();
}
