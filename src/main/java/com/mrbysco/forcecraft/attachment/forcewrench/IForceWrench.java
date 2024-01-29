package com.mrbysco.forcecraft.attachment.forcewrench;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public interface IForceWrench {

	boolean hasBlockStored();

	boolean canStoreBlock();

	CompoundTag getStoredBlockNBT();

	BlockState getStoredBlockState();

	String getStoredName();

	void storeBlockNBT(CompoundTag tag);

	void storeBlockState(BlockState base);

	void setBlockName(String name);

	void clearBlockStorage();
}
