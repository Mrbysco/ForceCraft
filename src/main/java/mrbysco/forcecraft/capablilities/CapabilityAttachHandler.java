package mrbysco.forcecraft.capablilities;

import mrbysco.forcecraft.capablilities.banemodifier.BaneProvider;
import mrbysco.forcecraft.capablilities.playermodifier.PlayerModifierProvider;
import mrbysco.forcecraft.capablilities.shearable.ShearableProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.BANE_CAP;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.PLAYER_CAP;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.SHEAR_CAP;

public class CapabilityAttachHandler {

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EndermanEntity){
                event.addCapability(BANE_CAP, new BaneProvider());
        }

        if(event.getObject() instanceof PlayerEntity){
            event.addCapability(PLAYER_CAP, new PlayerModifierProvider());
        }

        if(event.getObject() instanceof CowEntity || event.getObject() instanceof ChickenEntity){
            event.addCapability(SHEAR_CAP, new ShearableProvider());
        }
    }
}
