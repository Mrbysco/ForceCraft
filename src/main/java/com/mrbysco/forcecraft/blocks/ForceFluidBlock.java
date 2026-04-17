package com.mrbysco.forcecraft.blocks;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.entities.IColdMob;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.monster.zombie.Zombie;
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
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		if (entity instanceof LivingEntity livingEntity) {
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
					for (EquipmentSlot equipmentSlot : EquipmentSlotGroup.ARMOR) {
						ItemStack armor = zombie.getItemBySlot(equipmentSlot);
						zombie.spawnAtLocation((ServerLevel) level, armor.copy());
						armor.shrink(armor.getMaxStackSize());
					}
					for (EquipmentSlot equipmentSlot : EquipmentSlotGroup.HAND) {
						ItemStack held = zombie.getItemBySlot(equipmentSlot);
						zombie.spawnAtLocation((ServerLevel) level, held.copy());
						held.shrink(held.getMaxStackSize());
					}
				}
				MobCategory classification = livingEntity.getClassification(false);
				boolean secondPassed = level.getGameTime() % 20 == 0;
				if (classification == MobCategory.MONSTER) {
					if (livingEntity.is(EntityTypeTags.UNDEAD) && level.getGameTime() % 10 == 0) {
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
