package com.mrbysco.forcecraft.block.torch;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WallCamoTorchBlock extends WallTorchBlock {

	public WallCamoTorchBlock(SimpleParticleType supered, Properties properties) {
		super(supered, properties);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
}
