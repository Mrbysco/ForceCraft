package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.items.tools.ForceAxeItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class LumberHandler {

	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event) {
		final ItemStack stack = event.getPlayer().getMainHandItem();
		if (stack.has(ForceComponents.TOOL_LUMBERJACK)) {
			ForceAxeItem.fellTree(stack, event.getPos(), event.getPlayer());
		}
	}
}
