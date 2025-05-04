package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.capabilities.playermodifier.IPlayerModifier;
import com.mrbysco.forcecraft.capabilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.util.MobUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_BANE;
import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_PLAYERMOD;
import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ToolModifierHandler {

	@SubscribeEvent
	public void onLivingDamageEvent(LivingDamageEvent event) {
		if (event.getSource() == null) {
			return;
		}
		final DamageSource source = event.getSource();

		final LivingEntity target = event.getEntity();
		Entity trueSource = source.getEntity();
		if (trueSource instanceof Player) {
			Player player = (Player) source.getEntity();
			boolean appliedBane = false;

			int bleedLevel = 0;
			IToolModifier toolCap = player.getMainHandItem().getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (toolCap != null) {
				if (toolCap.hasBane()) {
					applyBane(target);
					appliedBane = true;
				}
				if (toolCap.hasBleed()) {
					bleedLevel = toolCap.getBleedLevel();
				}
			}
			MobUtil.addBleedingEffect(bleedLevel, target, player);

			IPlayerModifier playerCap = player.getCapability(CAPABILITY_PLAYERMOD).orElse(null);
			if (playerCap != null) {
				if (playerCap.hasBane() && !appliedBane) {
					applyBane(target);
				}

				float damage = event.getAmount();
				if (playerCap.hasHeatDamage()) {
					if (playerCap.getAttackDamage() == 0.0f) {
						damage += playerCap.getHeatDamage();
					} else {
						damage += playerCap.getAttackDamage();
					}

					target.setRemainingFireTicks((30 * playerCap.getHeatPieces()));
				} else {
					damage += playerCap.getAttackDamage();
				}

				if (playerCap.getAttackDamage() > 0 && player.getMainHandItem().isEmpty()) {
					player.level.playSound((Player) null, target.getX(), target.getY(), target.getZ(), ForceSounds.FORCE_PUNCH.get(), player.getSoundSource(), 1.0F, 1.0F);
					event.setAmount(damage);
				}
			}

		}

		if (target instanceof Player player) {
			int sturdyLevel = 0;
			for (ItemStack armorStack : player.getArmorSlots()) {
				IToolModifier modifierCap = armorStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
				if (modifierCap != null && modifierCap.hasSturdy()) {
					{
						sturdyLevel++;
					}
				}
			}
			if (sturdyLevel > 0) {
				double perArmor = ConfigHandler.COMMON.sturdyDamageReduction.get();
				double percentage = sturdyLevel * (perArmor / 4);
				float oldDamage = event.getAmount();
				float newDamage = (float) (oldDamage - (oldDamage * percentage));
				event.setAmount(Mth.clamp(newDamage, 1.0F, Float.MAX_VALUE));
			}
		}
	}

	private void applyBane(LivingEntity target) {
		if (target instanceof Creeper creeper) {
			creeper.getCapability(CAPABILITY_BANE).ifPresent((entityCap) -> {
				if (entityCap.canExplode()) {
					creeper.setSwellDir(-1);
					creeper.getEntityData().set(Creeper.DATA_IS_IGNITED, false);
					entityCap.setExplodeAbility(false);
					creeper.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof SwellGoal);
//					ForceCraft.LOGGER.info("Added Bane to " + target.getName());
				}
			});
		}
		if (target instanceof EnderMan enderman) {
			enderman.getCapability(CAPABILITY_BANE).ifPresent((entityCap) -> {
				if (entityCap.canTeleport()) {
					entityCap.setTeleportAbility(false);
//					ForceCraft.LOGGER.info("Added Bane to " + target.getName());
				}
			});
		}
	}
}
