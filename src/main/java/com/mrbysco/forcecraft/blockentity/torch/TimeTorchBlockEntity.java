package com.mrbysco.forcecraft.blockentity.torch;

import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.data.TimeData;
import com.mrbysco.forcecraft.data.TimeMode;
import com.mrbysco.forcecraft.registry.ForceAttachments;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;

public class TimeTorchBlockEntity extends AbstractTorchBlockEntity {
	private final int radius;
	private TimeMode mode = TimeMode.STOP;

	public TimeTorchBlockEntity(BlockPos pos, BlockState state) {
		super(ForceRegistry.TIME_TORCH_ENTITY.get(), pos, state);
		this.radius = ForceConfig.COMMON.timeTorchRadius.getAsInt();
	}

	@Override
	public void onInteract(Player player, BlockHitResult hitResult) {
		this.mode = this.mode.getNext();
		player.sendOverlayMessage(Component.translatable("forcecraft.component.time_mode", this.mode.getName()));
	}

	@Override
	public void serverTick() {
		if (level.getGameTime() % (level.tickRateManager().tickrate() * 5) == 0) {
			float rate = TimeData.rateFor(mode);
			for (BlockPos target : BlockPos.betweenClosed(
					getBlockPos().offset(-radius, -radius, -radius),
					getBlockPos().offset(radius, radius, radius)
			)) {
				BlockEntity blockEntity = level.getBlockEntity(target);
				if (blockEntity == null || !(blockEntity.getBlockState().getBlock() instanceof EntityBlock)) continue;
				blockEntity.setData(ForceAttachments.TIME_ADJUSTMENT, new TimeData(level.getGameTime() + (6 * 20), rate));
			}
		}
	}

	@Override
	public void clientTick() {

	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.store("mode", TimeMode.CODEC, mode);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.mode = input.read("mode", TimeMode.CODEC).orElse(TimeMode.STOP);
	}
}
