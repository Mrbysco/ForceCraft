package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ToolModifierHandler {
	@SubscribeEvent
	public void AttackEntityEvent(AttackEntityEvent event) {
		//Bane/Bleed
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = event.getPlayer();
			player.getHeldItemMainhand().getCapability(CAPABILITY_TOOLMOD).ifPresent((cap) -> {
				if(cap.hasBleed()) {
					Effect Bane = ForceEffects.BLEEDING.get();
					EffectInstance BaneEffect = new EffectInstance(Bane, 200, cap.getBleedLevel());
					if(event.getTarget() instanceof LivingEntity) {
						((LivingEntity) event.getTarget()).addPotionEffect(BaneEffect);
						ForceCraft.LOGGER.info("Added Bane to " + event.getTarget().getName());
					}
				}
			});
		}
	}


}
