package mrbysco.forcecraft.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

public class BaseBlock extends Block {
    public BaseBlock(AbstractBlock.Properties properties) {
        super(properties.hardnessAndResistance(2.0F));
    }
}
