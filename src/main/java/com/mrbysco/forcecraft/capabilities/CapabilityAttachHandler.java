package com.mrbysco.forcecraft.capabilities;

import com.mrbysco.forcecraft.capabilities.banemodifier.BaneModifierCapability;
import com.mrbysco.forcecraft.capabilities.playermodifier.IPlayerModifier;
import com.mrbysco.forcecraft.capabilities.playermodifier.PlayerModifierCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.BANE_CAP;
import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_PLAYERMOD;
import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.PLAYER_CAP;

public class CapabilityAttachHandler {

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EnderMan || event.getObject() instanceof Creeper) {
			event.addCapability(BANE_CAP, new BaneModifierCapability());
		}

		if (event.getObject() instanceof Player) {
			event.addCapability(PLAYER_CAP, new PlayerModifierCapability());
		}
	}

	@SubscribeEvent
	public void onDeath(PlayerEvent.Clone event) {
		// If not dead, player is returning from the End
		if (!event.isWasDeath()) return;

		Player original = event.getOriginal();
		Player clone = event.getEntity();

		final Capability<IPlayerModifier> capability = CAPABILITY_PLAYERMOD;
		original.getCapability(capability).ifPresent(dataOriginal ->
				clone.getCapability(capability).ifPresent(dataClone -> {
					CompoundTag tag = PlayerModifierCapability.writeNBT(dataOriginal);
					PlayerModifierCapability.readNBT(dataClone, tag);
				})
		);
	}
}
