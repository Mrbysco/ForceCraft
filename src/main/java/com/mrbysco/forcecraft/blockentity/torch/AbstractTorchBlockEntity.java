package com.mrbysco.forcecraft.blockentity.torch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractTorchBlockEntity extends BlockEntity {
	public AbstractTorchBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
		super(type, worldPosition, blockState);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractTorchBlockEntity torchBlockEntity) {
		torchBlockEntity.serverTick();
	}

	public abstract void serverTick();

	public static void clientTick(Level level, BlockPos pos, BlockState state, AbstractTorchBlockEntity torchBlockEntity) {
		torchBlockEntity.clientTick();
	}

	public abstract void clientTick();

	public void onInteract(Player player, BlockHitResult hitResult) {

	}
}
