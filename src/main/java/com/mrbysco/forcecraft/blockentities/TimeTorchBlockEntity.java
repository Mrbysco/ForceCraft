package com.mrbysco.forcecraft.blockentities;

import com.mrbysco.forcecraft.blocks.torch.TimeTorchBlock;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//All Code Heavily inspired by Torcherino. Credit to Moze_Intel, Sci4me and NinjaPhenix
public class TimeTorchBlockEntity extends BlockEntity {

	private int xMin;
	private int yMin;
	private int zMin;
	private int xMax;
	private int yMax;
	private int zMax;

	private byte speed;

	public TimeTorchBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		this.speed = 3;
	}

	public TimeTorchBlockEntity(BlockPos pos, BlockState state) {
		this(ForceRegistry.TIME_TORCH_BLOCK_ENTITY.get(), pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, TimeTorchBlockEntity timeTorch) {
		timeTorch.updateCachedMode();
		if (level.getGameTime() % 20 == 0) {
			timeTorch.tickNeighbor();
		}
	}

	protected int speed(int base) {
		return base;
	}

	private void tickNeighbor() {
		for (int x = this.xMin; x <= this.xMax; x++) {
			for (int y = this.yMin; y <= this.yMax; y++) {
				for (int z = this.zMin; z <= this.zMax; z++) {
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
		if (pos.equals(getBlockPos())) return;

		BlockState blockState = this.level.getBlockState(pos);
		if (blockState != null) {
			Block block = blockState.getBlock();

			if (block == null || block instanceof LiquidBlock || block instanceof TimeTorchBlock || block == Blocks.AIR)
				return;

			if (block.isRandomlyTicking(blockState) && !level.isClientSide) {
				for (int i = 0; i < this.speed; i++) {
					if (getLevel().getBlockState(pos) != blockState) break;
					if (getLevel().random.nextBoolean())
						block.randomTick(blockState, (ServerLevel) this.level, pos, level.random);
				}
			}
//            if(block.hasTileEntity(blockState)) { TODO: Fix Time Torch ticking Block Entities
//                BlockEntity tile = this.level.getBlockEntity(pos);
//
//                if(tile == null || tile.isRemoved()) return;
//
//                for(int i = 0; i < this.speed; i++) {
//                    if(tile.isRemoved()) {
//                        break;
//                    }
//                    if(tile instanceof TickableBlockEntity) {
//                        if(getLevel().random.nextBoolean())
//                        ((TickableBlockEntity) tile).tick();
//                    }
//                }
//            }
		}
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putByte("Speed", this.speed);
	}

	@Override
	public void load(CompoundTag nbt) {
		this.speed = nbt.getByte("Speed");
		super.load(nbt);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt);
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}
}
