package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.registry.ForceTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.GrindstoneEvent;

public class GrindstoneHandler {
	@SubscribeEvent
	public void onPlace(GrindstoneEvent.OnPlaceItem event) {
		if (event.getBottomItem().is(ForceTags.TOOLS) || event.getTopItem().is(ForceTags.TOOLS)) {
			event.setCanceled(true);
		}
	}
}
