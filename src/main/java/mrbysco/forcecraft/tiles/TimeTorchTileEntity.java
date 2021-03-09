package mrbysco.forcecraft.tiles;

import mrbysco.forcecraft.blocks.torch.TimeTorchBlock;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

//All Code Heavily inspired by Torcherino. Credit to Moze_Intel, Sci4me and NinjaPhenix
public class TimeTorchTileEntity extends TileEntity implements ITickableTileEntity {

    private int xMin;
    private int yMin;
    private int zMin;
    private int xMax;
    private int yMax;
    private int zMax;

    private byte speed;

    private Random rand;

    public TimeTorchTileEntity(TileEntityType<?> tileTypeIn) {
        super(tileTypeIn);
        this.speed = 3;
        this.rand = new Random();
    }

    public TimeTorchTileEntity() {
        this(ForceRegistry.TIME_TORCH_TILE.get());
    }

    @Override
    public void tick() {
        if(this.world.isRemote) return;
        //if(this.mode == 0 || this.speed == 0) return;
        this.updateCachedMode();
        this.tickNeighbor();
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
        this.xMin = this.pos.getX() - 1;
        this.yMin = this.pos.getY() - 1;
        this.zMin = this.pos.getZ() - 1;
        this.xMax = this.pos.getX() + 1;
        this.yMax = this.pos.getY() + 1;
        this.zMax = this.pos.getZ() + 1;
    }


    private void tickBlock(BlockPos pos) {
        if(pos.equals(getPos())) return;

        BlockState blockState = this.world.getBlockState(pos);
        Block block = blockState.getBlock();

        if(block == null || block instanceof FlowingFluidBlock || block instanceof TimeTorchBlock || block == Blocks.AIR)
            return;

        if(block.ticksRandomly(blockState) && !world.isRemote) {
            for(int i = 0; i < this.speed; i++) {
                if(getWorld().getBlockState(pos) != blockState) break;
                block.tick(blockState, (ServerWorld)this.world, pos, this.rand);
            }
        }
        if(block.hasTileEntity(blockState)) {
            TileEntity tile = this.world.getTileEntity(pos);

            if(tile == null || tile.isRemoved()) return;

            for(int i = 0; i < this.speed; i++) {
                if(tile.isRemoved()) {
                    break;
                }
                if(tile instanceof ITickableTileEntity) {
                    ((ITickableTileEntity) tile).tick();
                }
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT tag = super.write(compound);
        tag.putByte("Speed", this.speed);
        return tag;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.speed = nbt.getByte("Speed");
        super.read(state, nbt);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return new SUpdateTileEntityPacket(getPos(), 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        this.read(getBlockState(), pkt.getNbtCompound());
    }

    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }
}
