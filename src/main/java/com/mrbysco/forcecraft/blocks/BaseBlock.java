package com.mrbysco.forcecraft.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class BaseBlock extends Block {
    public BaseBlock(BlockBehaviour.Properties properties) {
        super(properties.strength(2.0F));
    }
}
