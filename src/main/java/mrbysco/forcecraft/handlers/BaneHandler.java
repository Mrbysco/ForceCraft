package mrbysco.forcecraft.handlers;

import net.minecraft.entity.monster.EndermanEntity;
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
}
