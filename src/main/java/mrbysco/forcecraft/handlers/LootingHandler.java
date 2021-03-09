package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.capablilities.playermodifier.IPlayerModifier;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;

public class LootingHandler {

    @SubscribeEvent
    public void onLooting(LootingLevelEvent event) {
        int level = event.getLootingLevel();

        IPlayerModifier playerModifier = event.getDamageSource().getTrueSource().getCapability(CAPABILITY_PLAYERMOD).orElse(null);
        if(playerModifier != null) {
            level += playerModifier.getLuckLevel();
        }

        event.setLootingLevel(level);
    }
}
