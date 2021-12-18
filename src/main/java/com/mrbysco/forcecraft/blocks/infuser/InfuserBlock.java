package com.mrbysco.forcecraft.blocks.infuser;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class InfuserBlock extends Block {

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(3, 10, 3, 13, 11, 13),
            Block.box(2, 0, 2, 4, 5, 4),
            Block.box(12, 0, 2, 14, 5, 4),
            Block.box(12, 0, 12, 14, 5, 14),
            Block.box(2, 0, 12, 4, 5, 14),
            Block.box(2, 5, 2, 14, 10, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public InfuserBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new InfuserBlockEntity();
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof InfuserBlockEntity) {
            LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getDirection());
            fluidHandler.ifPresent((handler) -> {
                if(playerIn.getItemInHand(handIn).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
                    FluidUtil.interactWithFluidHandler(playerIn, handIn, worldIn, pos, hit.getDirection());
                } else {
                    if (!worldIn.isClientSide) {
                        NetworkHooks.openGui((ServerPlayer) playerIn, (InfuserBlockEntity) tileentity, pos);
                    }
                }
            });

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof InfuserBlockEntity) {
                tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                    for(int i = 0; i < handler.getSlots(); ++i) {
                        Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
                    }
                });
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof InfuserBlockEntity infuserTile) {
            if(infuserTile.processTime > 0) {
                double d0 = (double)pos.getX() + 0.5D;
                double d1 = (double)pos.getY() + 0.5;
                double d2 = (double)pos.getZ() + 0.5D;

                Direction direction = Direction.UP;
                Direction.Axis direction$axis = direction.getAxis();
                for(int i = 0; i < 3; i++) {
                    double d3 = 0.52D;
                    double d4 = rand.nextDouble() * 0.6D - 0.3D;
                    double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
                    double d6 = rand.nextDouble() * 6.0D / 16.0D;
                    double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
                    worldIn.addParticle(ParticleTypes.REVERSE_PORTAL, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}

