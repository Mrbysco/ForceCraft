package mrbysco.forcecraft.blocks.flammable;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.function.Supplier;

public class FlammableStairs extends StairsBlock {
	public final int fireSpreadSpeed;
	public final int flammability;

	public FlammableStairs(Supplier<BlockState> state, Properties properties, int spreadSpeed, int flammability) {
		super(state, properties);
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
