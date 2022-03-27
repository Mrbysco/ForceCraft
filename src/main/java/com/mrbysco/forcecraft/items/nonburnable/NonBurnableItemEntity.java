package com.mrbysco.forcecraft.items.nonburnable;

import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;

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

	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return source.getMsgId().equals(DamageSource.OUT_OF_WORLD.msgId);
	}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public static class EventHandler {
		public static void onExpire(ItemExpireEvent event) {
			if (event.getEntityItem() instanceof NonBurnableItemEntity) {
				event.setCanceled(true);
			}
		}
	}
}
