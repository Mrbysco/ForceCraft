package com.mrbysco.forcecraft.item;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MobChunkItem extends Item {
    public MobChunkItem(Properties properties) {
        super(properties);
    }

	@Override
	public Component getName(ItemStack stack) {
		if (stack.has(ForceComponents.BOUND_ID)) {
			Identifier boundId = stack.get(ForceComponents.BOUND_ID);
			if (boundId != null) {
				String entityKey = Util.makeDescriptionId("entity", boundId);
				return Component.translatable("item.forcecraft.mob_chunk.bound", Component.translatable(entityKey));
			}
		}
		return super.getName(stack);
	}
}
