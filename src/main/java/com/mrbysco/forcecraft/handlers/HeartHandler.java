package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HeartHandler {
	static final double CHANCE = 0.05; // 5%

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Level world = event.getEntity().level;
		if (world.isClientSide || event.getSource() == null || ConfigHandler.COMMON.disableRecoveryHearts.get() || world.random.nextDouble() >= CHANCE) {
			return;
		}
		if (event.getSource().getEntity() instanceof Player && !(event.getSource().getEntity() instanceof FakePlayer)) {
			// killed by a real player
			MobCategory classification = event.getEntityLiving().getType().getCategory();
			if (classification == MobCategory.MONSTER) {
				// and its a monster, not sheep or squid or something
				BlockPos pos = event.getEntity().blockPosition();
				world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(ForceRegistry.RECOVERY_HEART.get())));
			}
		}
	}
}
