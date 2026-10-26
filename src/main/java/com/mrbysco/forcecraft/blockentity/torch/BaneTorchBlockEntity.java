package com.mrbysco.forcecraft.blockentity.torch;

import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BaneTorchBlockEntity extends AbstractTorchBlockEntity {
	public BaneTorchBlockEntity(BlockPos pos, BlockState state) {
		super(ForceRegistry.BANE_TORCH_ENTITY.get(), pos, state);
	}

	@Override
	public void serverTick() {
		if (level.getGameTime() % (level.tickRateManager().tickrate() * 9) == 0) {
			AABB area = new AABB(this.getBlockPos()).inflate(ForceConfig.COMMON.torchRadius.getAsInt());
			for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAlive)) {
				if (target instanceof Monster monster) {
					monster.kill((ServerLevel) level);
					level.playSound(null, target.blockPosition(), ForceSounds.FORCE_MAGIC.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
					((ServerLevel) level).sendParticles(
							ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.opaque(0xfff755)),
							target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5,
							4, 0.25, 0.25, 0.25, 0.0);
				}
			}
		}
	}

	@Override
	public void clientTick() {

	}
}
