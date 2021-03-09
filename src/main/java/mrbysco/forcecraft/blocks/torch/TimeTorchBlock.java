package mrbysco.forcecraft.blocks.torch;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.config.ConfigHandler;
import mrbysco.forcecraft.tiles.TimeTorchTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TimeTorchBlock extends TorchBlock {

    public TimeTorchBlock(AbstractBlock.Properties properties, IParticleData particleData) {
        super(properties, particleData);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return ConfigHandler.COMMON.timeTorchEnabled.get();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TimeTorchTileEntity();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if(ConfigHandler.COMMON.timeTorchLogging.get()) {
            if(placer != null) {
                ForceCraft.LOGGER.info("A Time Torch has been placed at {} by {}", pos, placer.getDisplayName().getString());
            } else {
                ForceCraft.LOGGER.info("A Time Torch has been placed at {} by a non entity", pos);
            }
        }
    }
}