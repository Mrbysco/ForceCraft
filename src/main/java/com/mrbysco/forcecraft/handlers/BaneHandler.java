package com.mrbysco.forcecraft.handlers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_BANE;

public class BaneHandler {
    @SubscribeEvent
    public void onEnderTeleportEvent(EntityTeleportEvent.EnderEntity event){
        if(event.getEntity() instanceof EnderMan enderman){
            enderman.getCapability(CAPABILITY_BANE).ifPresent((cap) -> {
                if(!cap.canTeleport()){
                    event.setCanceled(true);
                }
            });
        }
    }

    @SubscribeEvent
    public void onEntityCreation(EntityJoinWorldEvent event) {
        if(!event.getWorld().isClientSide()) {
            Entity entity = event.getEntity();
            if(entity instanceof Creeper creeper){
                creeper.getCapability(CAPABILITY_BANE).ifPresent((entityCap) -> {
                    if(!entityCap.canExplode()){
                        creeper.setSwellDir(-1);
                        creeper.getEntityData().set(Creeper.DATA_IS_IGNITED, false);
                        entityCap.setExplodeAbility(false);
                        creeper.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof SwellGoal);
                    }
                });
            }
        }
    }
}
