package com.mrbysco.forcecraft.tiles;

import com.mrbysco.forcecraft.blocks.torch.TimeTorchBlock;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//All Code Heavily inspired by Torcherino. Credit to Moze_Intel, Sci4me and NinjaPhenix
public class TimeTorchTileEntity extends BlockEntity implements TickableBlockEntity {

    private int xMin;
    private int yMin;
    private int zMin;
    private int xMax;
    private int yMax;
    private int zMax;

    private byte speed;

    public TimeTorchTileEntity(BlockEntityType<?> tileTypeIn) {
        super(tileTypeIn);
        this.speed = 3;
    }

    public TimeTorchTileEntity() {
        this(ForceRegistry.TIME_TORCH_TILE.get());
    }

    @Override
    public void tick() {
        if(this.level.isClientSide) return;
        this.updateCachedMode();
        if(this.level.getGameTime() % 20 == 0) {
            this.tickNeighbor();
        }
    }

    protected int speed(int base) { return base; }

    private void tickNeighbor() {
        for(int x = this.xMin; x <= this.xMax; x++) {
            for(int y = this.yMin; y <= this.yMax; y++) {
                for(int z = this.zMin; z <= this.zMax; z++) {
                    this.tickBlock(new BlockPos(x, y, z));
                }
            }
        }
    }

    private void updateCachedMode() {
        this.xMin = this.worldPosition.getX() - 1;
        this.yMin = this.worldPosition.getY() - 1;
        this.zMin = this.worldPosition.getZ() - 1;
        this.xMax = this.worldPosition.getX() + 1;
        this.yMax = this.worldPosition.getY() + 1;
        this.zMax = this.worldPosition.getZ() + 1;
    }

    @SuppressWarnings("deprecation")
    private void tickBlock(@Nonnull BlockPos pos) {
        if(pos.equals(getBlockPos())) return;

        BlockState blockState = this.level.getBlockState(pos);
        if(blockState != null) {
            Block block = blockState.getBlock();

            if(block == null || block instanceof LiquidBlock || block instanceof TimeTorchBlock || block == Blocks.AIR)
                return;

            if(block.isRandomlyTicking(blockState) && !level.isClientSide) {
                for(int i = 0; i < this.speed; i++) {
                    if(getLevel().getBlockState(pos) != blockState) break;
                    if(getLevel().random.nextBoolean())
                    block.randomTick(blockState, (ServerLevel)this.level, pos, level.random);
                }
            }
            if(block.hasTileEntity(blockState)) {
                BlockEntity tile = this.level.getBlockEntity(pos);

                if(tile == null || tile.isRemoved()) return;

                for(int i = 0; i < this.speed; i++) {
                    if(tile.isRemoved()) {
                        break;
                    }
                    if(tile instanceof TickableBlockEntity) {
                        if(getLevel().random.nextBoolean())
                        ((TickableBlockEntity) tile).tick();
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        CompoundTag tag = super.save(compound);
        tag.putByte("Speed", this.speed);
        return tag;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        this.speed = nbt.getByte("Speed");
        super.load(state, nbt);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.load(getBlockState(), pkt.getTag());
    }
}
