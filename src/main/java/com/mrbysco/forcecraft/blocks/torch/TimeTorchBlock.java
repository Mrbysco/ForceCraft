package com.mrbysco.forcecraft.blocks.torch;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.blockentities.TimeTorchBlockEntity;
import com.mrbysco.forcecraft.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TimeTorchBlock extends TorchBlock implements EntityBlock {

    public TimeTorchBlock(BlockBehaviour.Properties properties, ParticleOptions particleData) {
        super(properties, particleData);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ConfigHandler.COMMON.timeTorchEnabled.get() ? new TimeTorchBlockEntity(pos, state) : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if(ConfigHandler.COMMON.timeTorchLogging.get()) {
            if(placer != null) {
                ForceCraft.LOGGER.info("A Time Torch has been placed at {} by {}", pos, placer.getDisplayName().getString());
            } else {
                ForceCraft.LOGGER.info("A Time Torch has been placed at {} by a non entity", pos);
            }
        }
    }
}