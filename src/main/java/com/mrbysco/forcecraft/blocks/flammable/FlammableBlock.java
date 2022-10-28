package com.mrbysco.forcecraft.blocks.flammable;

import com.mrbysco.forcecraft.blocks.BaseBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import net.minecraft.block.AbstractBlock.Properties;

public class FlammableBlock extends BaseBlock {
	public final int fireSpreadSpeed;
	public final int flammability;

	public FlammableBlock(Properties properties, int spreadSpeed, int flammability) {
		super(properties);
		this.fireSpreadSpeed = spreadSpeed;
		this.flammability = flammability;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return this.fireSpreadSpeed;
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return this.flammability;
	}

	@Override
	public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return true;
	}
}
