package com.mrbysco.forcecraft.blocks.flammable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class FlammableStairs extends StairBlock {
	public final int fireSpreadSpeed;
	public final int flammability;

	public FlammableStairs(Supplier<BlockState> state, Properties properties, int spreadSpeed, int flammability) {
		super(state, properties);
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
