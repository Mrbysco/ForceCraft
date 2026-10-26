package com.mrbysco.forcecraft.client.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class ForceEngineRenderState extends BlockEntityRenderState {
	public Direction orientation;
	public boolean isGenerating;
	public float animationTime;
}
