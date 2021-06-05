package mrbysco.forcecraft.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_BANE;

public class BaneHandler {
    @SubscribeEvent
    public void onEnderTeleportEvent(EnderTeleportEvent event){
        if(event.getEntity() instanceof EndermanEntity){
            EndermanEntity enderman = ((EndermanEntity) event.getEntity());
            enderman.getCapability(CAPABILITY_BANE).ifPresent((cap) -> {
                if(!cap.canTeleport()){
                    event.setCanceled(true);
                }
            });
        }
    }

    @SubscribeEvent
    public void onEntityCreation(EntityJoinWorldEvent event) {
        if(!event.getWorld().isRemote()) {
            Entity entity = event.getEntity();
            if(entity instanceof CreeperEntity){
                CreeperEntity creeper = ((CreeperEntity) entity);
                creeper.getCapability(CAPABILITY_BANE).ifPresent((entityCap) -> {
                    if(entityCap.canExplode()){
                        creeper.setCreeperState(-1);
                        creeper.getDataManager().set(CreeperEntity.IGNITED, false);
                        entityCap.setExplodeAbility(false);
                        creeper.goalSelector.goals.removeIf(goal -> goal.getGoal() instanceof CreeperSwellGoal);
                    }
                });
            }
        }
    }
}
