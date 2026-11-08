package com.mrbysco.forcecraft.client.state;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class EnderTotRenderState extends HumanoidRenderState {
	public boolean isCreepy;
	public final BlockModelRenderState carriedBlock = new BlockModelRenderState();
}
