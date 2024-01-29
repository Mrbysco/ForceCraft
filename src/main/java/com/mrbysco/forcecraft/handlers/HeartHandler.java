package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class HeartHandler {
	static final double CHANCE = 0.05; // 5%

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Level level = event.getEntity().level();
		if (level.isClientSide || event.getSource() == null || ConfigHandler.COMMON.disableRecoveryHearts.get() || level.random.nextDouble() >= CHANCE) {
			return;
		}
		if (event.getSource().getEntity() instanceof Player && !(event.getSource().getEntity() instanceof FakePlayer)) {
			// killed by a real player
			MobCategory classification = event.getEntity().getType().getCategory();
			if (classification == MobCategory.MONSTER) {
				// and its a monster, not sheep or squid or something
				BlockPos pos = event.getEntity().blockPosition();
				level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(ForceRegistry.RECOVERY_HEART.get())));
			}
		}
	}
}
