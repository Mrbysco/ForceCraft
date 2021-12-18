package com.mrbysco.forcecraft.blocks.torch;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.tiles.TimeTorchTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class WallTimeTorchBlock extends WallTorchBlock {

    public WallTimeTorchBlock(Properties properties, ParticleOptions particleData) {
        super(properties, particleData);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return ConfigHandler.COMMON.timeTorchEnabled.get();
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new TimeTorchTileEntity();
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if(ConfigHandler.COMMON.timeTorchLogging.get()) {
            if(placer != null) {
                ForceCraft.LOGGER.info("A Time Torch has been placed at {} by {}", pos, placer.getDisplayName().getString());
            } else {
                ForceCraft.LOGGER.info("A Time Torch has been placed at {} by a non entity", pos);
            }
        }
    }
}