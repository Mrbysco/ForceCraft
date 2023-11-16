package com.mrbysco.forcecraft.tiles;

import com.mrbysco.forcecraft.blocks.torch.TimeTorchBlock;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//All Code Heavily inspired by Torcherino. Credit to Moze_Intel, Sci4me and NinjaPhenix
public class TimeTorchTileEntity extends TileEntity implements ITickableTileEntity {

	private final List<BlockPos> positionList = new ArrayList<>();
	private int speed;

	public TimeTorchTileEntity(TileEntityType<?> tileTypeIn) {
		super(tileTypeIn);
		this.speed = ConfigHandler.COMMON.timeTorchAmount.get();
	}

	public TimeTorchTileEntity() {
		this(ForceRegistry.TIME_TORCH_TILE.get());
	}

	@Override
	public void tick() {
		if (this.level.isClientSide) return;
		int rate = ConfigHandler.COMMON.timeTorchRate.get();
		if (rate > 0 && this.level.getGameTime() % rate == 0) {
			this.tickNeighbor();
		}
	}

	protected int speed(int base) {
		return base;
	}

	private void tickNeighbor() {
		if (this.positionList.isEmpty()) {
			initializePositions();
		}
		positionList.forEach(this::tickBlock);
	}

	private void initializePositions() {
		positionList.clear();
		positionList.addAll(BlockPos.betweenClosedStream(
						getBlockPos().offset(-1, -1, -1),
						getBlockPos().offset(1, 1, 1))
				.map(BlockPos::immutable).collect(Collectors.toList()));
	}

	@SuppressWarnings("deprecation")
	private void tickBlock(@Nonnull BlockPos pos) {
		if (pos.equals(getBlockPos()) || !level.isAreaLoaded(pos, 1)) return;

		BlockState blockState = this.level.getBlockState(pos);
		if (blockState != null) {
			Block block = blockState.getBlock();

			if (block == null || block instanceof FlowingFluidBlock || block instanceof TimeTorchBlock || blockState.isAir())
				return;

			if (block.isRandomlyTicking(blockState) && !level.isClientSide) {
				for (int i = 0; i < this.speed; i++) {
					if (getLevel().getBlockState(pos) != blockState) break;
					if (getLevel().random.nextBoolean())
						block.randomTick(blockState, (ServerWorld) this.level, pos, level.random);
				}
			}
			if (block.hasTileEntity(blockState)) {
				TileEntity tile = this.level.getBlockEntity(pos);

				if (tile == null || !tile.hasLevel() || tile.isRemoved()) return;

				for (int i = 0; i < this.speed; i++) {
					if (tile.isRemoved()) {
						break;
					}
					if (tile instanceof ITickableTileEntity) {
						if (getLevel().random.nextBoolean())
							((ITickableTileEntity) tile).tick();
					}
				}
			}
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		CompoundNBT tag = super.save(compound);
		tag.putInt("Speed", this.speed);
		return tag;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		if (nbt.contains("Speed"))
			this.speed = nbt.getInt("Speed");
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.save(nbt);
		return new SUpdateTileEntityPacket(getBlockPos(), 0, nbt);
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.load(state, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(getBlockState(), pkt.getTag());
	}
}
