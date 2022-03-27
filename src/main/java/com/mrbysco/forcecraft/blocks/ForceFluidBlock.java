package com.mrbysco.forcecraft.blocks;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.entities.IColdMob;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.common.IForgeShearable;

import java.util.function.Supplier;

public class ForceFluidBlock extends LiquidBlock {

	public ForceFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
		super(supplier, properties);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof LivingEntity livingEntity) {

			if (livingEntity instanceof Player) {
				((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10, 0, false, false));
			} else {
				MobType creatureAttribute = livingEntity.getMobType();
				MobCategory classification = livingEntity.getClassification(false);
				boolean secondPassed = level.getGameTime() % 20 == 0;
				boolean damageEntity = creatureAttribute == MobType.UNDEAD || creatureAttribute == MobType.UNDEFINED || creatureAttribute == MobType.ARTHROPOD;
				if (classification == MobCategory.MONSTER && damageEntity) {
					if (level.getGameTime() % 10 == 0) {
						livingEntity.hurt(ForceCraft.LIQUID_FORCE_DAMAGE, 1.0F);
					}
				} else {
					if (level.getGameTime() % 10 == 0) {
						livingEntity.heal(0.5F);
					}
					if (livingEntity instanceof IForgeShearable && secondPassed) {
						if (RANDOM.nextInt(10) <= 3) {
							if (livingEntity instanceof Sheep) {
								((Sheep) livingEntity).setSheared(false);
							} else if (livingEntity instanceof IColdMob coldMob) {
								coldMob.transformMob(livingEntity, level);
							}
						}
					}
				}
			}
		}
	}
}
