package com.mrbysco.forcecraft.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class ForceBrickBlock extends BaseBlock {

    public ForceBrickBlock(BlockBehaviour.Properties properties) {
        super(properties.strength(50.0F, 200.0F));
    }
}
