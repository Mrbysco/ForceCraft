package com.mrbysco.forcecraft.blocks.torch;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.blockentities.TimeTorchBlockEntity;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class WallTimeTorchBlock extends WallTorchBlock implements EntityBlock {

    public WallTimeTorchBlock(Properties properties, ParticleOptions particleData) {
        super(properties, particleData);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ConfigHandler.COMMON.timeTorchEnabled.get() ? new TimeTorchBlockEntity(pos, state) : null;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTimeTorchTicker(level, blockEntityType, ForceRegistry.TIME_TORCH_BLOCK_ENTITY.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTimeTorchTicker(Level level, BlockEntityType<T> p_151989_, BlockEntityType<? extends TimeTorchBlockEntity> timeTorchBlockEntityType) {
        return level.isClientSide ? null : createTickerHelper(p_151989_, timeTorchBlockEntityType, TimeTorchBlockEntity::serverTick);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
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