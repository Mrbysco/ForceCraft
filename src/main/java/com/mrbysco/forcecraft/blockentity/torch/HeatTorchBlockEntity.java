package com.mrbysco.forcecraft.blockentity.torch;

import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class HeatTorchBlockEntity extends AbstractTorchBlockEntity {
	public HeatTorchBlockEntity(BlockPos pos, BlockState state) {
		super(ForceRegistry.HEAT_TORCH_ENTITY.get(), pos, state);
	}

	@Override
	public void serverTick() {
		if (level.getGameTime() % (level.tickRateManager().tickrate() * 5) == 0) {
			AABB area = new AABB(this.getBlockPos()).inflate(ForceConfig.COMMON.torchRadius.getAsInt());
			for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAlive)) {
				if (target.is(EntityTypeTags.UNDEAD)) {
					target.setRemainingFireTicks(30);
					target.hurtServer((ServerLevel) level, level.damageSources().onFire(), 2.0F);
				}
			}
		}
	}

	@Override
	public void clientTick() {

	}
}
