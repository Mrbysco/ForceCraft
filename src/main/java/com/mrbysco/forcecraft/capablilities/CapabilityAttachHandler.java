package com.mrbysco.forcecraft.capablilities;

import com.mrbysco.forcecraft.capablilities.banemodifier.BaneProvider;
import com.mrbysco.forcecraft.capablilities.playermodifier.IPlayerModifier;
import com.mrbysco.forcecraft.capablilities.playermodifier.PlayerModifierProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.BANE_CAP;
import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;
import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.PLAYER_CAP;

public class CapabilityAttachHandler {

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EndermanEntity || event.getObject() instanceof CreeperEntity){
            event.addCapability(BANE_CAP, new BaneProvider());
        }

        if(event.getObject() instanceof PlayerEntity){
            event.addCapability(PLAYER_CAP, new PlayerModifierProvider());
        }
    }

    @SubscribeEvent
    public void onDeath(PlayerEvent.Clone event) {
        // If not dead, player is returning from the End
        if (!event.isWasDeath()) return;

        PlayerEntity original = event.getOriginal();
        PlayerEntity clone = event.getPlayer();

        final Capability<IPlayerModifier> capability = CAPABILITY_PLAYERMOD;
        original.getCapability(capability).ifPresent(dataOriginal ->
                clone.getCapability(capability).ifPresent(dataClone -> {
                    INBT nbt = capability.getStorage().writeNBT(capability, dataOriginal, null);
                    capability.getStorage().readNBT(capability, dataClone, null, nbt);
                })
        );
    }
}
