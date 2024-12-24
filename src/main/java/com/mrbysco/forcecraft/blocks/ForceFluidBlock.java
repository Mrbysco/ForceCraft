package com.mrbysco.forcecraft.blocks;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.entities.IColdMob;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.common.IShearable;

public class ForceFluidBlock extends LiquidBlock {

	public ForceFluidBlock(FlowingFluid fluid, Properties properties) {
		super(fluid, properties);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof LivingEntity livingEntity) {
			if (livingEntity instanceof Player player) {
				if (player.getHealth() < player.getMaxHealth()) {
					MobEffectInstance effectInstance = player.getEffect(MobEffects.REGENERATION);
					MobEffectInstance newInstance = new MobEffectInstance(MobEffects.REGENERATION, 50, ConfigHandler.COMMON.liquidRegenLevel.get(), false, false);
					if (effectInstance != null) {
						effectInstance.update(newInstance);
					} else {
						player.addEffect(newInstance);
					}
					if (ConfigHandler.COMMON.enableForceShake.get()) {
						player.addEffect(new MobEffectInstance(ForceEffects.SHAKING, 50, 0, false, false));
					}
				}
			} else {
				if (livingEntity instanceof Zombie zombie && !zombie.isBaby()) {
					zombie.setBaby(true);
					for (ItemStack armor : zombie.getArmorSlots()) {
						zombie.spawnAtLocation(armor.copy());
						armor.shrink(armor.getMaxStackSize());
					}
					for (ItemStack held : zombie.getHandSlots()) {
						zombie.spawnAtLocation(held.copy());
						held.shrink(held.getMaxStackSize());
					}
				}
				MobCategory classification = livingEntity.getClassification(false);
				boolean secondPassed = level.getGameTime() % 20 == 0;
				if (classification == MobCategory.MONSTER) {
					if (livingEntity.getType().is(EntityTypeTags.UNDEAD) && level.getGameTime() % 10 == 0) {
						livingEntity.hurt(Reference.causeLiquidForceDamage(livingEntity), 1.0F);
					}
				} else {
					if (level.getGameTime() % 10 == 0) {
						livingEntity.heal(0.5F);
					}
					if (livingEntity instanceof IShearable && secondPassed) {
						if (level.getRandom().nextInt(10) <= 3) {
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
