package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.config.ConfigHandler;
import mrbysco.forcecraft.items.ForceArmorItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class PlayerCapHandler {

	private static final int SPEED_DURATION = 200;

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = ((PlayerEntity) event.getEntityLiving());

			Iterable<ItemStack> armor = player.getArmorInventoryList();
			int speed = 0;
			for (ItemStack slotSelected : armor) {
				if (slotSelected.getItem() instanceof ForceArmorItem) {
					IToolModifier modifierCap = slotSelected.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						// Speed
						speed += modifierCap.getSpeedLevel();
					}
				}
			}

			if (speed > 0) {
				EffectInstance speedEffect = new EffectInstance(Effects.SPEED, SPEED_DURATION, speed, false, false);
				if(!player.isPotionActive(Effects.SPEED) || (player.isPotionActive(Effects.SPEED) && player.getActivePotionEffect(Effects.SPEED).getDuration() <= 100)) {
					player.addPotionEffect(speedEffect);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			//Sync on login
			PlayerEntity player = ((PlayerEntity) event.getEntityLiving());
			updateArmorProperties(player);
		}
	}

	@SubscribeEvent
	public void equipmentChangeEvent(LivingEquipmentChangeEvent event) {
		if(event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			updateArmorProperties(player);
		}
	}

	public static void updateArmorProperties(PlayerEntity player) {
		Iterable<ItemStack> armor = player.getArmorInventoryList();
		int armorPieces = 0;
		int damage = 0;
		int heat = 0;
		int luck = 0;
		int bane = 0;

		for (ItemStack slotSelected : armor) {
			if (slotSelected.getItem() instanceof ForceArmorItem) {
				IToolModifier modifierCap = slotSelected.getCapability(CAPABILITY_TOOLMOD).orElse(null);
				if (modifierCap != null) {
					// Pieces
					armorPieces++;

					// Damage
					damage += (int)(modifierCap.getSharpLevel() * ConfigHandler.COMMON.forcePunchDamage.get());
					// Heat
					if (modifierCap.hasHeat()) {
						heat++;
					}
					// Luck
					if (modifierCap.hasLuck()) {
						luck++;
					}
					// Bane
					if (modifierCap.hasBane()) {
						bane++;
					}
				}
			}
		}

		int finalArmorPieces = armorPieces;
		player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> cap.setArmorPieces(finalArmorPieces));

		int finalDamage = damage;
		player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> cap.setAttackDamage(1.0F * finalDamage));

		int finalHeat = heat;
		player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> {
			cap.setHeatPieces(finalHeat);
			cap.setHeatDamage(2.0F * finalHeat);
		});

		int finalLuck = luck;
		player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> cap.setLuckLevel(finalLuck));

		int finalBane = bane;
		player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> cap.setBane(finalBane > 0));
	}

	@SubscribeEvent
	public void harvestCheckEvent(HarvestCheck event) {
		PlayerEntity player = event.getPlayer();
		player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> {
			if(player.getHeldItemMainhand().isEmpty()) {
				if(cap.hasFullSet() && event.getTargetBlock().getBlock().getHarvestLevel(event.getTargetBlock()) <= 2) {
					event.setCanHarvest(true);
				}
			}
		});
	}

	@SubscribeEvent
	public void breakSpeedEvent(BreakSpeed event) {
		PlayerEntity player = event.getPlayer();
		player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> {
			if(player.getHeldItemMainhand().isEmpty()) {
				if(event.getOriginalSpeed() < 6) {
					event.setNewSpeed(6);
				}
			}
		});
	}
}