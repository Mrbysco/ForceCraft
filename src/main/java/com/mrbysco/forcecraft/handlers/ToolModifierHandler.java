package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.attachment.banemodifier.BaneModifierAttachment;
import com.mrbysco.forcecraft.attachment.playermodifier.PlayerModifierAttachment;
import com.mrbysco.forcecraft.attachment.toolmodifier.ToolModifierAttachment;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.BANE_MODIFIER;
import static com.mrbysco.forcecraft.attachment.CapabilityHandler.PLAYER_MOD;
import static com.mrbysco.forcecraft.attachment.CapabilityHandler.TOOL_MODIFIER;

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
			if (player.getMainHandItem().hasData(TOOL_MODIFIER)) {
				ToolModifierAttachment attachment = player.getMainHandItem().getData(TOOL_MODIFIER);
				if (attachment.hasBane()) {
					applyBane(target);
					appliedBane = true;
				}
				if (attachment.hasBleed()) {
					bleedLevel = attachment.getBleedLevel();
				}
			}
			MobUtil.addBleedingEffect(bleedLevel, target, player);

			if (player.hasData(PLAYER_MOD)) {
				PlayerModifierAttachment attachment = player.getData(PLAYER_MOD);
				if (attachment.hasBane() && !appliedBane) {
					applyBane(target);
				}

				float damage = event.getAmount();
				if (attachment.hasHeatDamage()) {
					if (attachment.getAttackDamage() == 0.0f) {
						damage += attachment.getHeatDamage();
					} else {
						damage += attachment.getAttackDamage();
					}

					target.setRemainingFireTicks((30 * attachment.getHeatPieces()));
				} else {
					damage += attachment.getAttackDamage();
				}

				if (attachment.getAttackDamage() > 0 && player.getMainHandItem().isEmpty()) {
					player.level().playSound((Player) null, target.getX(), target.getY(), target.getZ(), ForceSounds.FORCE_PUNCH.get(), player.getSoundSource(), 1.0F, 1.0F);
					event.setAmount(damage);
				}
			}

		}

		if (target instanceof Player player) {
			int sturdyLevel = 0;
			for (ItemStack armorStack : player.getArmorSlots()) {
				if (armorStack.hasData(TOOL_MODIFIER) && armorStack.getData(TOOL_MODIFIER).hasSturdy()) {
					sturdyLevel++;
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
			BaneModifierAttachment attachment = creeper.getData(BANE_MODIFIER);
			if (attachment.canExplode()) {
				creeper.setSwellDir(-1);
				creeper.getEntityData().set(Creeper.DATA_IS_IGNITED, false);
				attachment.setExplodeAbility(false);
				creeper.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof SwellGoal);
//					ForceCraft.LOGGER.info("Added Bane to " + target.getName());
				creeper.setData(BANE_MODIFIER, attachment);
			}
		}
		if (target instanceof EnderMan enderman) {
			BaneModifierAttachment attachment = enderman.getData(BANE_MODIFIER);
			if (attachment.canTeleport()) {
				attachment.setTeleportAbility(false);
//					ForceCraft.LOGGER.info("Added Bane to " + target.getName());
				enderman.setData(BANE_MODIFIER, attachment);
			}
		}
	}
}
