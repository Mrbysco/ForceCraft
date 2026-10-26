package com.mrbysco.forcecraft.block.torch;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CamoTorchBlock extends TorchBlock {

	public CamoTorchBlock(SimpleParticleType supered, Properties properties) {
		super(supered, properties);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
}
