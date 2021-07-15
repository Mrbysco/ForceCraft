package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.items.ForceArmorItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class LivingUpdateHandler {

	private static final int SPEED_DURATION = 200;

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
		int speed = 0;
		int damage = 0;
		int heat = 0;

		for (ItemStack slotSelected : armor) {
			if (slotSelected.getItem() instanceof ForceArmorItem) {
				IToolModifier modifierCap = slotSelected.getCapability(CAPABILITY_TOOLMOD).orElse(null);
				if (modifierCap != null) {
					// Speed
					speed += modifierCap.getSpeedLevel();

					// Damage
					damage += modifierCap.getSharpLevel();
					// Heat
					if (modifierCap.hasHeat()) {
						heat++;
					}
				}
			}
		}

		if (speed > 0) {
			EffectInstance speedEffect = new EffectInstance(Effects.SPEED, SPEED_DURATION, speed, false, false);
			if(!player.isPotionActive(Effects.SPEED) || (player.isPotionActive(Effects.SPEED) && player.getActivePotionEffect(Effects.SPEED).getDuration() <= 100)) {
				player.addPotionEffect(speedEffect);
			}
		}
		if (damage > 0) {
			int finalDamage = damage;
			player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> cap.setAttackDamage(1.0F * finalDamage));
		}
		if (heat != 0) {
			int finalHeat = heat;
			player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> cap.setHeatDamage(1.0F * finalHeat));
		}
	}
}
