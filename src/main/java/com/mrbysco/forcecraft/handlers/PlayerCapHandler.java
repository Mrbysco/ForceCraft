package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.attachment.playermodifier.PlayerModifierAttachment;
import com.mrbysco.forcecraft.attachment.toolmodifier.ToolModifierAttachment;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.items.ForceArmorItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.PLAYER_MOD;
import static com.mrbysco.forcecraft.attachment.CapabilityHandler.TOOL_MODIFIER;

public class PlayerCapHandler {

	private static final int SPEED_DURATION = 200;

	@SubscribeEvent
	public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
			Player player = event.player;

			Iterable<ItemStack> armor = player.getArmorSlots();
			int speed = 0;
			for (ItemStack slotSelected : armor) {
				if (slotSelected.getItem() instanceof ForceArmorItem) {
					ToolModifierAttachment attachment = slotSelected.getData(TOOL_MODIFIER);
					// Speed
					speed += attachment.getSpeedLevel();
				}
			}

			if (speed > 0) {
				MobEffectInstance speedEffect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, SPEED_DURATION, speed - 1, false, false);
				if (!player.hasEffect(MobEffects.MOVEMENT_SPEED) || (player.hasEffect(MobEffects.MOVEMENT_SPEED) && player.getEffect(MobEffects.MOVEMENT_SPEED).getDuration() <= 100)) {
					player.addEffect(speedEffect);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent event) {
		//Sync on login
		updateArmorProperties(event.getEntity());
	}

	@SubscribeEvent
	public void equipmentChangeEvent(LivingEquipmentChangeEvent event) {
		if (event.getEntity() instanceof Player player) {
			updateArmorProperties(player);
		}
	}

	public static void updateArmorProperties(Player player) {
		Iterable<ItemStack> armor = player.getArmorSlots();
		int armorPieces = 0;
		int damage = 0;
		int heat = 0;
		int luck = 0;
		int bane = 0;
		int bleed = 0;

		for (ItemStack slotSelected : armor) {
			if (slotSelected.getItem() instanceof ForceArmorItem) {
				ToolModifierAttachment attachment = slotSelected.getData(TOOL_MODIFIER);
				// Pieces
				armorPieces++;

				// Damage
				damage += (int) (attachment.getSharpLevel() * ConfigHandler.COMMON.forcePunchDamage.get());
				// Heat
				if (attachment.hasHeat()) {
					heat++;
				}
				// Luck
				if (attachment.hasLuck()) {
					luck++;
				}
				// Bane
				if (attachment.hasBane()) {
					bane++;
				}
				// Bleed
				if (attachment.hasBleed()) {
					bleed += attachment.getBleedLevel();
				}
			}
		}

		PlayerModifierAttachment attachment = player.getData(PLAYER_MOD);
		int finalArmorPieces = armorPieces;
		attachment.setArmorPieces(finalArmorPieces);

		int finalDamage = damage;
		attachment.setAttackDamage(1.0F * finalDamage);

		int finalHeat = heat;
		attachment.setHeatPieces(finalHeat);
		attachment.setHeatDamage(2.0F * finalHeat);

		int finalLuck = luck;
		attachment.setLuckLevel(finalLuck);

		int finalBane = bane;
		attachment.setBane(finalBane > 0);

		int finalBleed = bleed;
		attachment.setBleeding(finalBleed);

		//Save the attachment data
		player.setData(PLAYER_MOD, attachment);
	}

	@SubscribeEvent
	public void harvestCheckEvent(PlayerEvent.HarvestCheck event) {
		final Player player = event.getEntity();
		if (player.hasData(PLAYER_MOD)) {
			PlayerModifierAttachment attachment = player.getData(PLAYER_MOD);
			if (attachment.hasFullSet() && player.getMainHandItem().isEmpty()) {
				if (event.getTargetBlock().getBlock().getExplosionResistance() <= 2) {
					event.setCanHarvest(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void breakSpeedEvent(PlayerEvent.BreakSpeed event) {
		final Player player = event.getEntity();
		if (player.hasData(PLAYER_MOD)) {
			PlayerModifierAttachment attachment = player.getData(PLAYER_MOD);
			if (attachment.hasFullSet() && player.getMainHandItem().isEmpty()) {
				if (event.getOriginalSpeed() < 6) {
					event.setNewSpeed(6);
				}
			}
		}
	}
}
