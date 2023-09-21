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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

//All Code Heavily inspired by Torcherino. Credit to Moze_Intel, Sci4me and NinjaPhenix
public class TimeTorchBlockEntity extends BlockEntity {

	private final List<BlockPos> positionList = new ArrayList<>();
	private int speed;

	public TimeTorchBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		this.speed = 3;
		initializePositions();
	}

	public TimeTorchBlockEntity(BlockPos pos, BlockState state) {
		this(ForceRegistry.TIME_TORCH_BLOCK_ENTITY.get(), pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, TimeTorchBlockEntity timeTorch) {
		if (level.getGameTime() % 5 == 0) {
			timeTorch.tickNeighbor();
		}
	}

	private void tickNeighbor() {
		positionList.forEach(this::tickBlock);
	}

	private void initializePositions() {
		positionList.clear();
		positionList.addAll(BlockPos.betweenClosedStream(
						worldPosition.offset(-1, -1, -1),
						worldPosition.offset(1, 1, 1))
				.map(BlockPos::immutable).toList());
	}

	@SuppressWarnings("deprecation")
	private void tickBlock(@Nonnull BlockPos pos) {
		if (pos.equals(getBlockPos()) || !level.isAreaLoaded(pos, 1)) return;


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

			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity != null) {
				for (int i = 0; i < this.speed; i++) {
					if (blockEntity.isRemoved()) {
						break;
					}
					BlockEntityTicker<BlockEntity> ticker = blockState.getTicker(level, (BlockEntityType<BlockEntity>) blockEntity.getType());
					if (ticker != null) {
						ticker.tick(level, pos, blockEntity.getBlockState(), blockEntity);
					}
				}
			}
		}
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt("Speed", this.speed);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		if (nbt.contains("Speed"))
			this.speed = nbt.getInt("Speed");
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		this.load(packet.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt);
		return nbt;
	}
}
