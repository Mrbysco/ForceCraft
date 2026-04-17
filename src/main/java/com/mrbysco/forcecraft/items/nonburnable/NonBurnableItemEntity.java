package com.mrbysco.forcecraft.items.nonburnable;

import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;

public class NonBurnableItemEntity extends ItemEntity {

	public NonBurnableItemEntity(EntityType<? extends ItemEntity> entityType, Level level) {
		super(entityType, level);
	}

	public NonBurnableItemEntity(Level level, double x, double y, double z, ItemStack stack) {
		super(level, x, y, z, stack);
	}

	@Override
	public EntityType<?> getType() {
		return ForceEntities.NON_BURNABLE_ITEM.get();
	}

	public static class EventHandler {
		public static void onExpire(ItemExpireEvent event) {
			if (event.getEntity() instanceof NonBurnableItemEntity) {
				event.setExtraLife(Integer.MAX_VALUE);
			}
		}
	}
}
