package mrbysco.forcecraft.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraftforge.common.ToolType;

public class ForceBrickBlock extends BaseBlock {

    public ForceBrickBlock(AbstractBlock.Properties properties) {
        super(properties.hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3));
    }
}
