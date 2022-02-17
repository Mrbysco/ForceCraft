package com.mrbysco.forcecraft.blocks.flammable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FlammableSlab extends SlabBlock {
	public final int fireSpreadSpeed;
	public final int flammability;

	public FlammableSlab(Properties properties, int spreadSpeed, int flammability) {
		super(properties);
		this.fireSpreadSpeed = spreadSpeed;
		this.flammability = flammability;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return this.fireSpreadSpeed;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return this.flammability;
	}

	@Override
	public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return true;
	}
}
