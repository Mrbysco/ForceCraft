package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.potion.effects.TickableEffect;
import mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_BANE;
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
		LivingEntity target = event.getEntityLiving();
		
		player.getHeldItemMainhand().getCapability(CAPABILITY_TOOLMOD).ifPresent((cap) -> {
			if(cap.hasBane()) {
				if(target instanceof CreeperEntity){
					CreeperEntity creeper = ((CreeperEntity) target);
					creeper.getCapability(CAPABILITY_BANE).ifPresent((entityCap) -> {
						if(entityCap.canExplode()){
							entityCap.setExplodeAbility(false);
							creeper.goalSelector.goals.removeIf(goal -> goal.getGoal() instanceof CreeperSwellGoal);
						}
					});
					ForceCraft.LOGGER.info("Added Bane to " + target.getName());
				}
				if(event.getEntity() instanceof EndermanEntity){
					EndermanEntity enderman = ((EndermanEntity) event.getEntity());
					enderman.getCapability(CAPABILITY_BANE).ifPresent((entityCap) -> {
						if(entityCap.canTeleport()){
							entityCap.setTeleportAbility(false);
						}
					});
					ForceCraft.LOGGER.info("Added Bane to " + target.getName());
				}
			}
			if(cap.hasBleed()) {
				EffectInstance bleedingEffect = new EffectInstance(ForceEffects.BLEEDING.get(), 20 * BLEEDING_SECONDS, cap.getBleedLevel());
			 
				target.addPotionEffect(bleedingEffect);
				ForceCraft.LOGGER.info("Added BLEEDING to " + target.getName());
			}
		});
	}
}
