package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.items.ForceArmorItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class LivingUpdateHandler {

	private static final int SPEED_DURATION = 200;
	private static final int INVISIBILITY_DURATION = 200;
	public static List<PlayerEntity> flightList = new ArrayList<PlayerEntity>();

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = ((PlayerEntity) event.getEntityLiving());
			Iterable<ItemStack> armor = player.getArmorInventoryList();
			int wings = 0;
			int speed = 0;
			int damage = 0;
			int heat = 0;
			int luck = 0;
			int bane = 0;
			int bleed = 0;
			for (ItemStack slotSelected : armor) {
				if (slotSelected.getItem() instanceof ForceArmorItem) {
					IToolModifier modifierCap = slotSelected.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						// Speed
						speed += modifierCap.getSpeedLevel();

						// Wing
						if (modifierCap.hasWing()) {
							wings++;
						}
						// Damage
						damage += modifierCap.getSharpLevel();
						// Heat
						if (modifierCap.hasHeat()) {
							heat++;
						}
					}
				}
			}
			// Checks Hotbar
			if (!player.world.isRemote) {
				if (!player.isCreative()) {
					//only WING is ignored for creative
					if (wings == 4) {
						if (!player.isSpectator() && !player.abilities.allowFlying) {
							player.abilities.allowFlying = true;
							player.sendPlayerAbilities();
							flightList.add(player);
						}
					} else {
						if (flightList.contains(player)) {
							player.abilities.allowFlying = false;
							player.abilities.isFlying = false;
							player.sendPlayerAbilities();
							flightList.remove(player);
						}
					}
				}
				// else not creative
				if (speed > 0) {
					EffectInstance speedEffect = new EffectInstance(Effects.SPEED, SPEED_DURATION, 0, false, false);
					if(!player.isPotionActive(Effects.SPEED) || (player.isPotionActive(Effects.SPEED) && player.getActivePotionEffect(Effects.SPEED).getDuration() <= 100)) {
						player.addPotionEffect(speedEffect);
					}
				}
				if (damage > 0) {
					float finalDamage = damage;
					player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> {
						cap.addAttackDamage(0.5F * finalDamage);
					});
				}
				if (heat != 0) {
					float finalHeat = heat;
					player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> {
						cap.addHeatDamage(0.5F * finalHeat);
					});
				}

			}
		}
	}
}
