package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.potion.effects.TickableEffect;
import mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ToolModifierHandler {
	
	// 10 seconds at 20 ticks/sec
	private static final int BLEEDING_SECONDS = 10;

	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event) {

		TickableEffect bleedingEffect = (TickableEffect) ForceEffects.BLEEDING.get();
		if(event.getEntityLiving().isPotionActive(bleedingEffect)) {
			bleedingEffect.tick(event);
		}
	}
	
	@SubscribeEvent
	public void onLivingDamageEvent(LivingDamageEvent event) {
		
		if(event.getSource() == null || event.getSource().getTrueSource() == null
				|| !(event.getSource().getTrueSource() instanceof PlayerEntity)
				|| !( event.getEntity() instanceof LivingEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity)event.getSource().getTrueSource();
		LivingEntity target = (LivingEntity)event.getEntity();
		
		player.getHeldItemMainhand().getCapability(CAPABILITY_TOOLMOD).ifPresent((cap) -> {
			if(cap.hasBleed()) {
				EffectInstance BaneEffect = new EffectInstance(ForceEffects.BLEEDING.get(), 20 * BLEEDING_SECONDS, cap.getBleedLevel());
			 
				target.addPotionEffect(BaneEffect);
				ForceCraft.LOGGER.info("Added BLEEDING to " + target.getName());
			 
			}
		});
	}

}
