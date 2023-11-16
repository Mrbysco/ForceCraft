package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HeartHandler {
	static final double CHANCE = 0.05; // 5%

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		World world = event.getEntity().level;
		if (world.isClientSide || event.getSource() == null || ConfigHandler.COMMON.disableRecoveryHearts.get() || world.random.nextDouble() >= CHANCE) {
			return;
		}
		if (event.getSource().getEntity() instanceof PlayerEntity && !(event.getSource().getEntity() instanceof FakePlayer)) {
			// killed by a real player
			EntityClassification classification = event.getEntityLiving().getType().getCategory();
			if (classification == EntityClassification.MONSTER) {
				// and its a monster, not sheep or squid or something
				BlockPos pos = event.getEntity().blockPosition();
				world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(ForceRegistry.RECOVERY_HEART.get())));
			}
		}
	}
}
